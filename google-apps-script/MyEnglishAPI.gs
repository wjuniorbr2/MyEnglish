const MY_ENGLISH_SPREADSHEET_ID = "1smP3D6WXZKk6X8_MEfSBbp03r4hE8oKu4tobeo5tfKw";
const MY_ENGLISH_GENERAL_SHEET = "General";
const MY_ENGLISH_BUG_REPORT_SHEET = "Bug Reports";
const MY_ENGLISH_PRACTICE_FIRST_COLUMN = 5;
const MY_ENGLISH_PRACTICE_SLOTS = 20;
const MY_ENGLISH_SUMMARY_TOTAL_COLUMNS =
  MY_ENGLISH_PRACTICE_FIRST_COLUMN + MY_ENGLISH_PRACTICE_SLOTS - 1;

const MY_ENGLISH_SUMMARY_STUDENTS = [
  { displayName: "Vinícius", firstNameKey: "vinicius" },
  { displayName: "Ayla", firstNameKey: "ayla" },
  { displayName: "Yuri", firstNameKey: "yuri" },
  { displayName: "Kalil", firstNameKey: "kalil" },
  { displayName: "Beatriz D.", firstNameKey: "beatriz" },
  { displayName: "Junior", firstNameKey: "junior" }
];

const MY_ENGLISH_SUMMARY_TYPES = {
  "written homework": { column: 2, color: "#2E7D32", isPractice: false },
  "listening homework": { column: 3, color: "#1565C0", isPractice: false },
  "spoken homework": { column: 4, color: "#C62828", isPractice: false },
  "written practice": { color: "#2E7D32", isPractice: true },
  "listening practice": { color: "#1565C0", isPractice: true },
  "spoken practice": { color: "#C62828", isPractice: true }
};

function doPost(e) {
  const lock = LockService.getScriptLock();
  let locked = false;
  try {
    lock.waitLock(30000);
    locked = true;

    const data = readMyEnglishRequest_(e);
    const submittedAt = new Date();
    const type = normalizeSummaryText_(data.homeworkType);
    const reportType = normalizeSummaryText_(data.reportType);
    const isBug = type.includes("bug") || reportType.includes("bug");

    if (isBug) {
      appendBugReport_(data, submittedAt);
    } else {
      appendDetailedStudentReport_(data, submittedAt);
      try {
        updateWeeklyGeneralSummary_(
          data.studentName,
          data.lessonName,
          data.homeworkType,
          submittedAt,
          data.scoreText,
          data.report
        );
      } catch (summaryError) {
        console.error("Weekly summary error: " + summaryError.stack);
      }
    }

    return jsonResponse_({ status: "success" });
  } catch (error) {
    return jsonResponse_({
      status: "error",
      message: error && error.message ? error.message : String(error)
    });
  } finally {
    if (locked) lock.releaseLock();
  }
}

function doGet() {
  return jsonResponse_({ status: "success", service: "MyEnglish report API" });
}

function readMyEnglishRequest_(e) {
  const data = {};
  const params = e && e.parameter ? e.parameter : {};
  Object.keys(params).forEach(function(key) { data[key] = params[key]; });

  const raw = e && e.postData && e.postData.contents
    ? String(e.postData.contents).trim()
    : "";
  if (raw.startsWith("{")) {
    const parsed = JSON.parse(raw);
    Object.keys(parsed).forEach(function(key) { data[key] = parsed[key]; });
  }

  return {
    studentName: String(data.studentName || "Unknown Student").trim(),
    lessonName: String(data.lessonName || "").trim(),
    homeworkType: String(data.homeworkType || data.reportType || "").trim(),
    reportType: String(data.reportType || "").trim(),
    scoreText: String(data.scoreText || "0 / 0").trim(),
    report: String(data.report || ""),
    currentScreen: String(data.currentScreen || "").trim(),
    bugText: String(data.bugText || data.report || "").trim()
  };
}

function jsonResponse_(value) {
  return ContentService
    .createTextOutput(JSON.stringify(value))
    .setMimeType(ContentService.MimeType.JSON);
}

function spreadsheet_() {
  return SpreadsheetApp.openById(MY_ENGLISH_SPREADSHEET_ID);
}

function parseScore_(text) {
  const match = String(text || "").match(/(\d+)\s*\/\s*(\d+)/);
  return {
    correct: match ? Number(match[1]) : 0,
    total: match ? Number(match[2]) : 0
  };
}

function summarySection_(report) {
  return (String(report || "").split(/\n\s*\n/)[0] || "").trim();
}

function totalHints_(report) {
  const text = String(report || "");
  let match = summarySection_(text).match(
    /^(?:Total hints|Hints used):\s*(\d+)/im
  );
  if (match) return Number(match[1]);

  let total = 0;
  const regex = /^(?:Hints used|hints)\s*(?:=|:)\s*(\d+)/gim;
  while ((match = regex.exec(text)) !== null) total += Number(match[1] || 0);
  return total;
}

function totalPlays_(report, type) {
  const text = String(report || "");
  let match = summarySection_(text).match(
    /^(?:Total plays|Times played|Times heard):\s*(\d+)/im
  );
  if (match) return Number(match[1]);

  let total = 0;
  const regex = /^(?:Plays|plays|Times heard)\s*(?:=|:)\s*(\d+)/gim;
  while ((match = regex.exec(text)) !== null) total += Number(match[1] || 0);

  return normalizeSummaryText_(type).includes("written") ? 0 : total;
}

function phrasesPracticed_(report, fallback) {
  const match = String(report || "").match(/^Phrases practiced:\s*(\d+)/im);
  return match ? Number(match[1]) : Number(fallback || 0);
}

function safeSheetName_(name) {
  return (
    String(name || "Unknown Student")
      .trim()
      .replace(/[\\\/\?\*\[\]:]/g, "")
      .substring(0, 90) || "Unknown Student"
  );
}

function studentSheet_(studentName) {
  const book = spreadsheet_();
  const name = safeSheetName_(studentName);
  let sheet = book.getSheetByName(name);
  if (!sheet) sheet = book.insertSheet(name);
  ensureStudentHeaders_(sheet);
  return sheet;
}

function ensureStudentHeaders_(sheet) {
  const headers = [
    "Timestamp",
    "Lesson ID",
    "Homework Title",
    "Correct",
    "Total",
    "Hints Used",
    "Answers",
    "Times Played"
  ];

  if (sheet.getLastRow() === 0) {
    sheet.getRange(1, 1, 1, headers.length).setValues([headers]);
  } else {
    const current = sheet.getRange(1, 1, 1, headers.length)
      .getDisplayValues()[0];

    if (current[0] !== "Timestamp" || current[1] !== "Lesson ID") {
      sheet.insertRowBefore(1);
      sheet.getRange(1, 1, 1, headers.length).setValues([headers]);
    } else {
      headers.forEach(function(header, index) {
        if (!current[index]) sheet.getRange(1, index + 1).setValue(header);
      });
    }
  }

  sheet.getRange(1, 1, 1, headers.length)
    .setFontWeight("bold")
    .setBackground("#D9EAF7")
    .setHorizontalAlignment("center")
    .setVerticalAlignment("middle");
  sheet.setFrozenRows(1);
}

function appendDetailedStudentReport_(data, submittedAt) {
  const sheet = studentSheet_(data.studentName);
  const score = parseScore_(data.scoreText);

  sheet.appendRow([
    submittedAt,
    data.lessonName,
    data.homeworkType,
    score.correct,
    score.total,
    totalHints_(data.report),
    data.report,
    totalPlays_(data.report, data.homeworkType)
  ]);

  const row = sheet.getLastRow();
  sheet.getRange(row, 1).setNumberFormat("yyyy-mm-dd hh:mm:ss");
  sheet.getRange(row, 7).setWrap(true);
  sheet.autoResizeColumns(1, 6);
  sheet.setColumnWidth(7, 560);
  sheet.setColumnWidth(8, 110);
}

function appendBugReport_(data, submittedAt) {
  const book = spreadsheet_();
  let sheet = book.getSheetByName(MY_ENGLISH_BUG_REPORT_SHEET);
  if (!sheet) sheet = book.insertSheet(MY_ENGLISH_BUG_REPORT_SHEET);

  if (sheet.getLastRow() === 0) {
    sheet.getRange(1, 1, 1, 6).setValues([[
      "Timestamp",
      "Student Name",
      "Lesson ID",
      "Current Screen",
      "Report Type",
      "Bug Text"
    ]]).setFontWeight("bold").setBackground("#F4CCCC");
    sheet.setFrozenRows(1);
  }

  sheet.appendRow([
    submittedAt,
    data.studentName,
    data.lessonName,
    data.currentScreen,
    "Bug report",
    data.bugText
  ]);

  const row = sheet.getLastRow();
  sheet.getRange(row, 1).setNumberFormat("yyyy-mm-dd hh:mm:ss");
  sheet.getRange(row, 6).setWrap(true);
  sheet.setColumnWidth(6, 560);
}

function normalizeSummaryText_(value) {
  return String(value || "")
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "")
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, " ")
    .trim()
    .replace(/\s+/g, " ");
}

function summaryStudentIndex_(studentName) {
  const first = (normalizeSummaryText_(studentName).split(" ")[0] || "");
  for (let i = 0; i < MY_ENGLISH_SUMMARY_STUDENTS.length; i++) {
    if (MY_ENGLISH_SUMMARY_STUDENTS[i].firstNameKey === first) return i;
  }
  return -1;
}

function summaryWeek_(date, timeZone) {
  const year = Number(Utilities.formatDate(date, timeZone, "yyyy"));
  const month = Number(Utilities.formatDate(date, timeZone, "MM"));
  const day = Number(Utilities.formatDate(date, timeZone, "dd"));
  const local = new Date(Date.UTC(year, month - 1, day));
  const start = new Date(local.getTime());
  start.setUTCDate(start.getUTCDate() - local.getUTCDay());
  const end = new Date(start.getTime());
  end.setUTCDate(end.getUTCDate() + 6);

  return {
    key: Utilities.formatDate(start, "UTC", "yyyy-MM-dd"),
    title: weekTitle_(start, end)
  };
}

function weekTitle_(start, end) {
  const months = [
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
  ];
  return (
    months[start.getUTCMonth()] + " " + ordinal_(start.getUTCDate()) +
    " to " +
    months[end.getUTCMonth()] + " " + ordinal_(end.getUTCDate())
  );
}

function ordinal_(day) {
  if (day % 100 >= 11 && day % 100 <= 13) return day + "th";
  if (day % 10 === 1) return day + "st";
  if (day % 10 === 2) return day + "nd";
  if (day % 10 === 3) return day + "rd";
  return day + "th";
}

function generalSheet_(book) {
  let sheet = book.getSheetByName(MY_ENGLISH_GENERAL_SHEET);
  if (!sheet) {
    sheet = book.insertSheet(MY_ENGLISH_GENERAL_SHEET, 0);
    sheet.setHiddenGridlines(true);
  }
  return sheet;
}

function weekBlockRow_(sheet, week) {
  const lastRow = sheet.getLastRow();
  const expected = "myenglish-week:" + week.key;

  if (lastRow > 0) {
    const notes = sheet.getRange(1, 1, lastRow, 1).getNotes();
    for (let i = 0; i < notes.length; i++) {
      if (notes[i][0] === expected) return i + 1;
    }
  }

  const row = lastRow === 0 ? 1 : lastRow + 2;
  sheet.getRange(row, 1).setNote(expected);
  return row;
}

function ensureWeekLayout_(sheet, titleRow, week) {
  const title = sheet.getRange(
    titleRow, 1, 1, MY_ENGLISH_SUMMARY_TOTAL_COLUMNS
  );
  title.breakApart();
  title.merge();
  title.setValue(week.title)
    .setNote("myenglish-week:" + week.key)
    .setFontWeight("bold")
    .setFontSize(13)
    .setHorizontalAlignment("center")
    .setVerticalAlignment("middle")
    .setBackground("#0D3D7A")
    .setFontColor("#FFFFFF");
  sheet.setRowHeight(titleRow, 32);

  const headerRow = titleRow + 1;
  const header = sheet.getRange(
    headerRow, 1, 1, MY_ENGLISH_SUMMARY_TOTAL_COLUMNS
  );
  header.breakApart();
  sheet.getRange(headerRow, 1).setValue("Student");
  sheet.getRange(headerRow, 2).setValue("Written homework");
  sheet.getRange(headerRow, 3).setValue("Listening homework");
  sheet.getRange(headerRow, 4).setValue("Spoken homework");

  const practiceHeader = sheet.getRange(
    headerRow,
    MY_ENGLISH_PRACTICE_FIRST_COLUMN,
    1,
    MY_ENGLISH_PRACTICE_SLOTS
  );
  practiceHeader.merge();
  practiceHeader.setValue("Practices");

  header.setFontWeight("bold")
    .setHorizontalAlignment("center")
    .setVerticalAlignment("middle")
    .setWrap(true)
    .setBackground("#D9EAF7");
  sheet.setRowHeight(headerRow, 38);

  const startRow = titleRow + 2;
  const body = sheet.getRange(
    startRow,
    1,
    MY_ENGLISH_SUMMARY_STUDENTS.length,
    MY_ENGLISH_SUMMARY_TOTAL_COLUMNS
  );
  body.setVerticalAlignment("middle")
    .setWrap(true)
    .setBorder(
      true, true, true, true, true, true,
      "#B7C9D6",
      SpreadsheetApp.BorderStyle.SOLID
    );

  MY_ENGLISH_SUMMARY_STUDENTS.forEach(function(student, index) {
    const row = startRow + index;
    sheet.getRange(row, 1)
      .setValue(student.displayName)
      .setFontWeight("bold");
    sheet.setRowHeight(row, 40);
  });

  sheet.setColumnWidth(1, 125);
  sheet.setColumnWidth(2, 120);
  sheet.setColumnWidth(3, 120);
  sheet.setColumnWidth(4, 120);
  for (
    let column = MY_ENGLISH_PRACTICE_FIRST_COLUMN;
    column < MY_ENGLISH_PRACTICE_FIRST_COLUMN + MY_ENGLISH_PRACTICE_SLOTS;
    column++
  ) {
    sheet.setColumnWidth(column, 38);
  }
}

function updateWeeklyGeneralSummary_(
  studentName,
  lessonName,
  homeworkType,
  submittedAt,
  scoreText,
  report
) {
  const type = normalizeSummaryText_(homeworkType);
  const config = MY_ENGLISH_SUMMARY_TYPES[type];
  const studentIndex = summaryStudentIndex_(studentName);
  const lessonMatch = String(lessonName || "").match(/\d+/);

  if (!config || studentIndex < 0 || !lessonMatch) return;

  const lesson = Number(lessonMatch[0]);
  if (!lesson) return;

  const book = spreadsheet_();
  const timeZone =
    book.getSpreadsheetTimeZone() ||
    Session.getScriptTimeZone() ||
    "America/Sao_Paulo";
  const week = summaryWeek_(submittedAt, timeZone);
  const sheet = generalSheet_(book);
  const titleRow = weekBlockRow_(sheet, week);
  ensureWeekLayout_(sheet, titleRow, week);

  const studentRow = titleRow + 2 + studentIndex;
  const note = hoverNote_(
    studentName,
    lessonName,
    homeworkType,
    submittedAt,
    timeZone,
    scoreText,
    report
  );

  if (config.isPractice) {
    addPracticeMarker_(sheet, studentRow, type, lesson, config.color, note);
  } else {
    addHomeworkMarker_(
      sheet.getRange(studentRow, config.column),
      type,
      lesson,
      config.color,
      note
    );
  }
}

function hoverNote_(
  studentName,
  lessonName,
  homeworkType,
  submittedAt,
  timeZone,
  scoreText,
  report
) {
  const score = parseScore_(scoreText);
  const type = normalizeSummaryText_(homeworkType);
  const plays = type.includes("written")
    ? "0 (no audio)"
    : String(totalPlays_(report, homeworkType));

  const lines = [
    "Student: " + studentName,
    "Lesson: " + lessonName,
    "Activity: " + homeworkType,
    "Phrases practiced: " + phrasesPracticed_(report, score.total),
    "Correct answers: " + score.correct + " / " + score.total,
    "Times played: " + plays,
    "Hints used: " + totalHints_(report),
    "Report received: " +
      Utilities.formatDate(submittedAt, timeZone, "yyyy-MM-dd HH:mm:ss")
  ];

  const full = String(report || "").trim();
  let note = lines.join("\n");
  if (full) note += "\n\nFULL ACTIVITY REPORT\n" + full;
  return note.substring(0, 49000);
}

function addPracticeMarker_(sheet, row, type, lesson, color, note) {
  const range = sheet.getRange(
    row,
    MY_ENGLISH_PRACTICE_FIRST_COLUMN,
    1,
    MY_ENGLISH_PRACTICE_SLOTS
  );
  const values = range.getDisplayValues()[0];

  for (let offset = 0; offset < values.length; offset++) {
    if (!values[offset]) {
      const cell = sheet.getRange(
        row,
        MY_ENGLISH_PRACTICE_FIRST_COLUMN + offset
      );
      setSingleMarker_(cell, lesson, color);
      cell.setNote(
        "myenglish-practice-event:" +
        JSON.stringify({ type: type, lesson: lesson, color: color }) +
        "\n\n" +
        note
      );
      return;
    }
  }

  const overflow = sheet.getRange(
    row,
    MY_ENGLISH_PRACTICE_FIRST_COLUMN + MY_ENGLISH_PRACTICE_SLOTS - 1
  );
  overflow.setNote(
    (overflow.getNote() ? overflow.getNote() + "\n\n" : "") +
    "Additional practice report:\n" +
    note
  );
}

function addHomeworkMarker_(cell, type, lesson, color, note) {
  const events = readEvents_(cell);
  const exists = events.some(function(event) {
    return event.type === type && event.lesson === lesson;
  });

  if (!exists) events.push({ type: type, lesson: lesson, color: color });
  renderMarkers_(cell, events);
  cell.setNote(
    "myenglish-events:" + JSON.stringify(events) + "\n\n" + note
  );
}

function readEvents_(cell) {
  const firstLine = String(cell.getNote() || "").split("\n")[0];
  if (!firstLine.startsWith("myenglish-events:")) return [];

  try {
    const value = JSON.parse(
      firstLine.substring("myenglish-events:".length)
    );
    return Array.isArray(value) ? value : [];
  } catch (ignored) {
    return [];
  }
}

function setSingleMarker_(cell, lesson, color) {
  const marker = circledNumber_(lesson);
  const style = SpreadsheetApp.newTextStyle()
    .setForegroundColor(color)
    .setBold(true)
    .setFontSize(16)
    .build();
  const rich = SpreadsheetApp.newRichTextValue()
    .setText(marker)
    .setTextStyle(0, marker.length, style)
    .build();

  cell.setRichTextValue(rich)
    .setHorizontalAlignment("center")
    .setVerticalAlignment("middle")
    .setWrap(false);
}

function renderMarkers_(cell, events) {
  let text = "";
  const ranges = [];

  events.forEach(function(event, index) {
    if (index > 0) text += "  ";
    const start = text.length;
    text += circledNumber_(event.lesson);
    ranges.push({ start: start, end: text.length, color: event.color });
  });

  const builder = SpreadsheetApp.newRichTextValue().setText(text);
  ranges.forEach(function(range) {
    const style = SpreadsheetApp.newTextStyle()
      .setForegroundColor(range.color)
      .setBold(true)
      .setFontSize(16)
      .build();
    builder.setTextStyle(range.start, range.end, style);
  });

  cell.setRichTextValue(builder.build())
    .setHorizontalAlignment("center")
    .setVerticalAlignment("middle")
    .setWrap(true);
}

function circledNumber_(number) {
  const values = [
    "",
    "①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨", "⑩",
    "⑪", "⑫", "⑬", "⑭", "⑮", "⑯", "⑰", "⑱", "⑲", "⑳",
    "㉑", "㉒", "㉓", "㉔", "㉕", "㉖", "㉗", "㉘", "㉙", "㉚",
    "㉛", "㉜", "㉝", "㉞", "㉟", "㊱", "㊲", "㊳", "㊴", "㊵",
    "㊶", "㊷", "㊸", "㊹", "㊺", "㊻", "㊼", "㊽", "㊾", "㊿"
  ];
  return number >= 1 && number < values.length
    ? values[number]
    : "(" + number + ")";
}
