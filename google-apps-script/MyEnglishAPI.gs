const MY_ENGLISH_SPREADSHEET_ID = "1smP3D6WXZKk6X8_MEfSBbp03r4hE8oKu4tobeo5tfKw";
const MY_ENGLISH_GENERAL_SHEET = "General";

const MY_ENGLISH_SUMMARY_STUDENTS = [
  { displayName: "Vinícius", firstNameKey: "vinicius" },
  { displayName: "Ayla", firstNameKey: "ayla" },
  { displayName: "Yuri", firstNameKey: "yuri" },
  { displayName: "Kalil", firstNameKey: "kalil" },
  { displayName: "Beatriz D.", firstNameKey: "beatriz" },
  { displayName: "Junior", firstNameKey: "junior" }
];

const MY_ENGLISH_SUMMARY_TYPES = {
  "written homework": {
    column: 2,
    color: "#2E7D32",
    isPractice: false
  },
  "listening homework": {
    column: 3,
    color: "#1565C0",
    isPractice: false
  },
  "spoken homework": {
    column: 4,
    color: "#C62828",
    isPractice: false
  },
  "written practice": {
    column: 5,
    color: "#2E7D32",
    isPractice: true
  },
  "listening practice": {
    column: 5,
    color: "#1565C0",
    isPractice: true
  },
  "spoken practice": {
    column: 5,
    color: "#C62828",
    isPractice: true
  }
};

function doPost(e) {
  const lock = LockService.getScriptLock();
  let lockAcquired = false;

  try {
    lock.waitLock(30000);
    lockAcquired = true;

    const data = e && e.parameter ? e.parameter : {};

    const studentName = data.studentName || "Unknown Student";
    const lessonName = data.lessonName || "";
    const homeworkType = data.homeworkType || "";
    const scoreText = data.scoreText || "";
    const report = data.report || "";

    const reportType = (data.reportType || "").toString().trim();
    const currentScreen = (data.currentScreen || "").toString().trim();
    const bugText = (data.bugText || report || "").toString().trim();

    const normalizedHomeworkType = homeworkType.toString().toLowerCase().trim();
    const normalizedReportType = reportType.toString().toLowerCase().trim();

    const isBugReport =
      normalizedReportType.includes("bug") ||
      normalizedHomeworkType.includes("bug");

    const submittedAt = new Date();

    if (isBugReport) {
      const bugSheet = getOrCreateBugReportSheet();

      bugSheet.appendRow([
        submittedAt,
        studentName,
        lessonName,
        currentScreen,
        "Bug report",
        bugText
      ]);

      return createJsonResponse_({ status: "success" });
    }

    const sheet = getOrCreateStudentSheet(studentName);
    const type = normalizedHomeworkType;

    let correctCount = 0;
    let totalQuestions = 0;

    const scoreMatch = scoreText.toString().match(/(\d+)\s*\/\s*(\d+)/);
    if (scoreMatch) {
      totalQuestions = Number(scoreMatch[2]);
    }

    let hintsUsed = 0;

    if (type.includes("spoken")) {
      correctCount = countFirstTryCorrectSpoken(report);
      hintsUsed = countHintsFromLines(report);

    } else if (type.includes("written")) {
      correctCount = countFirstTryCorrectWritten(report);
      hintsUsed = countHintsFromLines(report);

    } else if (type.includes("listening")) {
      correctCount = countFirstTryCorrectListening(report);
      hintsUsed = countListeningHints(report);

    } else {
      correctCount = countOriginalScore(report);
      hintsUsed = countHintsFromLines(report);
    }

    sheet.appendRow([
      submittedAt,
      lessonName,
      homeworkType,
      correctCount,
      totalQuestions,
      hintsUsed,
      report
    ]);

    // The detailed student report above remains the primary report.
    // A failure in the general summary must not make the app resend the
    // detailed report and accidentally create duplicate rows.
    try {
      updateWeeklyGeneralSummary_(
        studentName,
        lessonName,
        homeworkType,
        submittedAt
      );
    } catch (summaryError) {
      console.error("Weekly summary error: " + summaryError.stack);
    }

    return createJsonResponse_({ status: "success" });

  } catch (err) {
    return createJsonResponse_({
      status: "error",
      message: err && err.message ? err.message : String(err)
    });

  } finally {
    if (lockAcquired) {
      lock.releaseLock();
    }
  }
}

function createJsonResponse_(data) {
  return ContentService
    .createTextOutput(JSON.stringify(data))
    .setMimeType(ContentService.MimeType.JSON);
}

function countOriginalScore(report) {
  const match = report.toString().match(
    /Original score:\s*(\d+)\s*\/\s*(\d+)/i
  );

  if (match) {
    return Number(match[1]);
  }

  return 0;
}

function countFirstTryCorrectListening(report) {
  return countOriginalScore(report);
}

function countFirstTryCorrectWritten(report) {
  let correct = 0;

  const regex = /Expected English:\s*(.*?)\s*\nFirst written answer:\s*(.*?)\s*\nHints used:/g;
  let match;

  while ((match = regex.exec(report.toString())) !== null) {
    const expected = normalizeAnswer(match[1]);
    const firstWritten = normalizeAnswer(match[2]);

    if (expected && firstWritten && expected === firstWritten) {
      correct++;
    }
  }

  if (correct === 0 && report.toString().includes("Written homework")) {
    const originalScore = countOriginalScore(report);

    if (originalScore > 0) {
      return originalScore;
    }
  }

  return correct;
}

function countFirstTryCorrectSpoken(report) {
  let correct = 0;

  const regex = /Expected English:\s*(.*?)\s*\nFirst recognized speech:\s*(.*?)\s*\nAttempts:/g;
  let match;

  while ((match = regex.exec(report.toString())) !== null) {
    const expected = normalizeAnswer(match[1]);
    const firstRecognized = normalizeAnswer(match[2]);

    if (expected && firstRecognized && expected === firstRecognized) {
      correct++;
    }
  }

  return correct;
}

function countHintsFromLines(report) {
  let hintsUsed = 0;

  const hintRegex = /Hints used:\s*(\d+)/g;
  let match;

  while ((match = hintRegex.exec(report.toString())) !== null) {
    hintsUsed += Number(match[1]);
  }

  return hintsUsed;
}

function countListeningHints(report) {
  let hintsUsed = 0;

  const statRegex = /plays\s*=\s*(\d+),\s*hints\s*=\s*(\d+)/g;
  let match;

  while ((match = statRegex.exec(report.toString())) !== null) {
    hintsUsed += Number(match[2]);
  }

  return hintsUsed;
}

function normalizeAnswer(text) {
  return text
    .toString()
    .toLowerCase()
    .replace(/[’‘]/g, "'")
    .replace(/[.,!?;:]/g, "")
    .replace(/\s+/g, " ")
    .trim();
}

function getSpreadsheet() {
  return SpreadsheetApp.openById(MY_ENGLISH_SPREADSHEET_ID);
}

function getOrCreateStudentSheet(studentName) {
  const spreadsheet = getSpreadsheet();

  const safeName = studentName
    .toString()
    .trim()
    .replace(/[\\\/\?\*\[\]:]/g, "")
    .substring(0, 90) || "Unknown Student";

  let sheet = spreadsheet.getSheetByName(safeName);

  if (!sheet) {
    sheet = spreadsheet.insertSheet(safeName);

    sheet.appendRow([
      "Timestamp",
      "Lesson ID",
      "Homework Title",
      "Correct",
      "Total",
      "Hints Used",
      "Answers"
    ]);
  }

  return sheet;
}

function getOrCreateBugReportSheet() {
  const spreadsheet = getSpreadsheet();

  let sheet = spreadsheet.getSheetByName("Bug Reports");

  if (!sheet) {
    sheet = spreadsheet.insertSheet("Bug Reports");

    sheet.appendRow([
      "Timestamp",
      "Student Name",
      "Lesson ID",
      "Current Screen",
      "Report Type",
      "Bug Text"
    ]);
  }

  return sheet;
}

function updateWeeklyGeneralSummary_(
  studentName,
  lessonName,
  homeworkType,
  submittedAt
) {
  const normalizedType = normalizeSummaryText_(homeworkType);
  const typeConfig = MY_ENGLISH_SUMMARY_TYPES[normalizedType];

  if (!typeConfig) {
    return;
  }

  const studentIndex = findSummaryStudentIndex_(studentName);
  if (studentIndex < 0) {
    return;
  }

  const lessonMatch = lessonName.toString().match(/\d+/);
  if (!lessonMatch) {
    return;
  }

  const lessonNumber = Number(lessonMatch[0]);
  if (!lessonNumber || lessonNumber < 1) {
    return;
  }

  const spreadsheet = getSpreadsheet();
  const timeZone =
    spreadsheet.getSpreadsheetTimeZone() ||
    Session.getScriptTimeZone() ||
    "America/Sao_Paulo";

  const week = getSummaryWeek_(submittedAt, timeZone);
  const generalSheet = getOrCreateGeneralSheet_(spreadsheet);
  const titleRow = findOrCreateWeekBlock_(generalSheet, week);
  const studentRow = titleRow + 2 + studentIndex;
  const targetCell = generalSheet.getRange(
    studentRow,
    typeConfig.column
  );

  addSummaryMarker_(
    targetCell,
    normalizedType,
    lessonNumber,
    typeConfig.color,
    typeConfig.isPractice
  );
}

function normalizeSummaryText_(value) {
  return value
    .toString()
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "")
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, " ")
    .trim()
    .replace(/\s+/g, " ");
}

function findSummaryStudentIndex_(studentName) {
  const normalizedName = normalizeSummaryText_(studentName);
  const firstName = normalizedName.split(" ")[0] || "";

  for (let i = 0; i < MY_ENGLISH_SUMMARY_STUDENTS.length; i++) {
    if (MY_ENGLISH_SUMMARY_STUDENTS[i].firstNameKey === firstName) {
      return i;
    }
  }

  return -1;
}

function getSummaryWeek_(date, timeZone) {
  const year = Number(Utilities.formatDate(date, timeZone, "yyyy"));
  const month = Number(Utilities.formatDate(date, timeZone, "MM"));
  const day = Number(Utilities.formatDate(date, timeZone, "dd"));

  const localDateOnly = new Date(Date.UTC(year, month - 1, day));
  const daysSinceSunday = localDateOnly.getUTCDay();

  const start = new Date(localDateOnly.getTime());
  start.setUTCDate(start.getUTCDate() - daysSinceSunday);

  const end = new Date(start.getTime());
  end.setUTCDate(end.getUTCDate() + 6);

  return {
    key: Utilities.formatDate(start, "UTC", "yyyy-MM-dd"),
    title: formatSummaryWeekTitle_(start, end)
  };
}

function formatSummaryWeekTitle_(start, end) {
  const months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
  ];

  const startMonth = months[start.getUTCMonth()];
  const endMonth = months[end.getUTCMonth()];
  const startDay = start.getUTCDate();
  const endDay = end.getUTCDate();

  if (startMonth === endMonth) {
    return startMonth + " " + ordinalSummaryDay_(startDay) +
      " to " + startMonth + " " + ordinalSummaryDay_(endDay);
  }

  return startMonth + " " + ordinalSummaryDay_(startDay) +
    " to " + endMonth + " " + ordinalSummaryDay_(endDay);
}

function ordinalSummaryDay_(day) {
  const mod100 = day % 100;

  if (mod100 >= 11 && mod100 <= 13) {
    return day + "th";
  }

  switch (day % 10) {
    case 1:
      return day + "st";
    case 2:
      return day + "nd";
    case 3:
      return day + "rd";
    default:
      return day + "th";
  }
}

function getOrCreateGeneralSheet_(spreadsheet) {
  let sheet = spreadsheet.getSheetByName(MY_ENGLISH_GENERAL_SHEET);

  if (!sheet) {
    sheet = spreadsheet.insertSheet(MY_ENGLISH_GENERAL_SHEET, 0);
    sheet.setHiddenGridlines(true);
  }

  return sheet;
}

function findOrCreateWeekBlock_(sheet, week) {
  const lastRow = sheet.getLastRow();

  if (lastRow > 0) {
    const notes = sheet.getRange(1, 1, lastRow, 1).getNotes();
    const expectedNote = "myenglish-week:" + week.key;

    for (let i = 0; i < notes.length; i++) {
      if (notes[i][0] === expectedNote) {
        return i + 1;
      }
    }
  }

  const titleRow = lastRow === 0 ? 1 : lastRow + 2;
  const titleRange = sheet.getRange(titleRow, 1, 1, 5);

  titleRange.merge();
  titleRange
    .setValue(week.title)
    .setNote("myenglish-week:" + week.key)
    .setFontWeight("bold")
    .setFontSize(13)
    .setHorizontalAlignment("center")
    .setVerticalAlignment("middle")
    .setBackground("#0D3D7A")
    .setFontColor("#FFFFFF");

  sheet.setRowHeight(titleRow, 32);

  const headerRow = titleRow + 1;
  sheet.getRange(headerRow, 1, 1, 5)
    .setValues([[
      "Student",
      "Written homework",
      "Listening homework",
      "Spoken homework",
      "Practices"
    ]])
    .setFontWeight("bold")
    .setHorizontalAlignment("center")
    .setVerticalAlignment("middle")
    .setWrap(true)
    .setBackground("#D9EAF7");

  sheet.setRowHeight(headerRow, 38);

  const studentRows = MY_ENGLISH_SUMMARY_STUDENTS.map(function(student) {
    return [student.displayName, "", "", "", ""];
  });

  const studentStartRow = titleRow + 2;
  const studentsRange = sheet.getRange(
    studentStartRow,
    1,
    MY_ENGLISH_SUMMARY_STUDENTS.length,
    5
  );

  studentsRange
    .setValues(studentRows)
    .setVerticalAlignment("middle")
    .setWrap(true)
    .setBorder(
      true,
      true,
      true,
      true,
      true,
      true,
      "#B7C9D6",
      SpreadsheetApp.BorderStyle.SOLID
    );

  sheet.getRange(
    studentStartRow,
    1,
    MY_ENGLISH_SUMMARY_STUDENTS.length,
    1
  ).setFontWeight("bold");

  for (
    let row = studentStartRow;
    row < studentStartRow + MY_ENGLISH_SUMMARY_STUDENTS.length;
    row++
  ) {
    sheet.setRowHeight(row, 34);
  }

  sheet.setColumnWidth(1, 125);
  sheet.setColumnWidth(2, 120);
  sheet.setColumnWidth(3, 120);
  sheet.setColumnWidth(4, 120);
  sheet.setColumnWidth(5, 330);

  return titleRow;
}

function addSummaryMarker_(
  cell,
  type,
  lessonNumber,
  color,
  isPractice
) {
  let events = readSummaryEvents_(cell);

  if (!isPractice) {
    const alreadyExists = events.some(function(event) {
      return event.type === type && event.lesson === lessonNumber;
    });

    if (alreadyExists) {
      return;
    }
  }

  events.push({
    type: type,
    lesson: lessonNumber,
    color: color
  });

  cell.setNote("myenglish-events:" + JSON.stringify(events));
  renderSummaryMarkers_(cell, events);
}

function readSummaryEvents_(cell) {
  const note = cell.getNote();

  if (!note || !note.startsWith("myenglish-events:")) {
    return [];
  }

  try {
    const parsed = JSON.parse(
      note.substring("myenglish-events:".length)
    );

    return Array.isArray(parsed) ? parsed : [];
  } catch (ignored) {
    return [];
  }
}

function renderSummaryMarkers_(cell, events) {
  let text = "";
  const styleRanges = [];

  for (let i = 0; i < events.length; i++) {
    if (i > 0) {
      text += "  ";
    }

    const marker = circledSummaryNumber_(events[i].lesson);
    const start = text.length;
    text += marker;
    const end = text.length;

    styleRanges.push({
      start: start,
      end: end,
      color: events[i].color
    });
  }

  const builder = SpreadsheetApp.newRichTextValue().setText(text);

  for (let i = 0; i < styleRanges.length; i++) {
    const textStyle = SpreadsheetApp.newTextStyle()
      .setForegroundColor(styleRanges[i].color)
      .setBold(true)
      .setFontSize(16)
      .build();

    builder.setTextStyle(
      styleRanges[i].start,
      styleRanges[i].end,
      textStyle
    );
  }

  cell
    .setRichTextValue(builder.build())
    .setHorizontalAlignment(cell.getColumn() === 5 ? "left" : "center")
    .setVerticalAlignment("middle")
    .setWrap(true);
}

function circledSummaryNumber_(number) {
  const circled = [
    "",
    "①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨", "⑩",
    "⑪", "⑫", "⑬", "⑭", "⑮", "⑯", "⑰", "⑱", "⑲", "⑳",
    "㉑", "㉒", "㉓", "㉔", "㉕", "㉖", "㉗", "㉘", "㉙", "㉚",
    "㉛", "㉜", "㉝", "㉞", "㉟", "㊱", "㊲", "㊳", "㊴", "㊵",
    "㊶", "㊷", "㊸", "㊹", "㊺", "㊻", "㊼", "㊽", "㊾", "㊿"
  ];

  if (number >= 1 && number < circled.length) {
    return circled[number];
  }

  return "(" + number + ")";
}
