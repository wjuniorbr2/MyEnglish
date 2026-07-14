var MY_ENGLISH_SUMMARY_SPREADSHEET_ID = "1smP3D6WXZKk6X8_MEfSBbp03r4hE8oKu4tobeo5tfKw";
var MY_ENGLISH_SUMMARY_SHEET_NAME = "General";

var MY_ENGLISH_SUMMARY_STUDENTS = [
  { display: "Vinícius", keys: ["vinicius"] },
  { display: "Ayla", keys: ["ayla"] },
  { display: "Yuri", keys: ["yuri"] },
  { display: "Kalil", keys: ["kalil"] },
  { display: "Beatriz D.", keys: ["beatriz d", "beatriz"] },
  { display: "Junior", keys: ["junior"] }
];

var MY_ENGLISH_SUMMARY_TYPES = {
  "written homework": { column: 2, color: "#2E7D32", practice: false },
  "listening homework": { column: 3, color: "#1565C0", practice: false },
  "spoken homework": { column: 4, color: "#C62828", practice: false },
  "written practice": { column: 5, color: "#2E7D32", practice: true },
  "listening practice": { column: 5, color: "#1565C0", practice: true },
  "spoken practice": { column: 5, color: "#C62828", practice: true }
};

/**
 * Call this once from the existing doPost(e), after the detailed report
 * has been written and before doPost returns its response.
 *
 * Recommended safe call:
 * try {
 *   updateWeeklySummaryFromPost_(e);
 * } catch (summaryError) {
 *   console.error(summaryError);
 * }
 */
function updateWeeklySummaryFromPost_(e) {
  var parameters = e && e.parameter ? e.parameter : {};
  var homeworkType = normalizeSummaryText_(parameters.homeworkType);
  var typeInfo = MY_ENGLISH_SUMMARY_TYPES[homeworkType];

  if (!typeInfo || homeworkType === "bug report") return;

  var studentIndex = findSummaryStudentIndex_(parameters.studentName);
  if (studentIndex < 0) return;

  var lessonMatch = String(parameters.lessonName || "").match(/\d+/);
  if (!lessonMatch) return;

  var lessonNumber = Number(lessonMatch[0]);
  if (!lessonNumber || lessonNumber < 1) return;

  var spreadsheet = SpreadsheetApp.openById(MY_ENGLISH_SUMMARY_SPREADSHEET_ID);
  var timeZone = spreadsheet.getSpreadsheetTimeZone() || Session.getScriptTimeZone() || "America/Sao_Paulo";
  var week = summaryWeekForDate_(new Date(), timeZone);
  var sheet = spreadsheet.getSheetByName(MY_ENGLISH_SUMMARY_SHEET_NAME);

  if (!sheet) {
    sheet = spreadsheet.insertSheet(MY_ENGLISH_SUMMARY_SHEET_NAME, 0);
    sheet.setHiddenGridlines(true);
  }

  var titleRow = findOrCreateSummaryWeekBlock_(sheet, week);
  var studentRow = titleRow + 2 + studentIndex;
  var targetCell = sheet.getRange(studentRow, typeInfo.column);

  addSummaryMarker_(
    targetCell,
    homeworkType,
    lessonNumber,
    typeInfo.color,
    typeInfo.practice
  );
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

function findSummaryStudentIndex_(studentName) {
  var normalized = normalizeSummaryText_(studentName);

  for (var i = 0; i < MY_ENGLISH_SUMMARY_STUDENTS.length; i++) {
    var keys = MY_ENGLISH_SUMMARY_STUDENTS[i].keys;
    for (var j = 0; j < keys.length; j++) {
      if (normalized === keys[j]) return i;
    }
  }

  return -1;
}

function summaryWeekForDate_(date, timeZone) {
  var year = Number(Utilities.formatDate(date, timeZone, "yyyy"));
  var month = Number(Utilities.formatDate(date, timeZone, "MM"));
  var day = Number(Utilities.formatDate(date, timeZone, "dd"));
  var localDateOnly = new Date(Date.UTC(year, month - 1, day));
  var daysSinceSunday = localDateOnly.getUTCDay();

  var start = new Date(localDateOnly.getTime());
  start.setUTCDate(start.getUTCDate() - daysSinceSunday);

  var end = new Date(start.getTime());
  end.setUTCDate(end.getUTCDate() + 6);

  return {
    key: Utilities.formatDate(start, "UTC", "yyyy-MM-dd"),
    title: formatSummaryWeekTitle_(start, end)
  };
}

function formatSummaryWeekTitle_(start, end) {
  var months = [
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
  ];

  var startMonth = months[start.getUTCMonth()];
  var endMonth = months[end.getUTCMonth()];
  var startDay = start.getUTCDate();
  var endDay = end.getUTCDate();

  if (startMonth === endMonth) {
    return startMonth + " " + ordinalSummaryDay_(startDay) +
      " to " + startMonth + " " + ordinalSummaryDay_(endDay);
  }

  return startMonth + " " + ordinalSummaryDay_(startDay) +
    " to " + endMonth + " " + ordinalSummaryDay_(endDay);
}

function ordinalSummaryDay_(day) {
  var mod100 = day % 100;
  if (mod100 >= 11 && mod100 <= 13) return day + "th";

  switch (day % 10) {
    case 1: return day + "st";
    case 2: return day + "nd";
    case 3: return day + "rd";
    default: return day + "th";
  }
}

function findOrCreateSummaryWeekBlock_(sheet, week) {
  var lastRow = sheet.getLastRow();

  for (var row = 1; row <= lastRow; row++) {
    if (sheet.getRange(row, 1).getNote() === "myenglish-week:" + week.key) {
      return row;
    }
  }

  var titleRow = lastRow === 0 ? 1 : lastRow + 2;
  var titleRange = sheet.getRange(titleRow, 1, 1, 5);
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

  var headerRow = titleRow + 1;
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

  var studentRows = [];
  for (var i = 0; i < MY_ENGLISH_SUMMARY_STUDENTS.length; i++) {
    studentRows.push([MY_ENGLISH_SUMMARY_STUDENTS[i].display, "", "", "", ""]);
  }

  var studentsRange = sheet.getRange(
    titleRow + 2,
    1,
    MY_ENGLISH_SUMMARY_STUDENTS.length,
    5
  );

  studentsRange
    .setValues(studentRows)
    .setVerticalAlignment("middle")
    .setWrap(true)
    .setBorder(true, true, true, true, true, true, "#B7C9D6", SpreadsheetApp.BorderStyle.SOLID);

  sheet.getRange(titleRow + 2, 1, MY_ENGLISH_SUMMARY_STUDENTS.length, 1)
    .setFontWeight("bold");

  for (var studentRow = titleRow + 2; studentRow < titleRow + 2 + MY_ENGLISH_SUMMARY_STUDENTS.length; studentRow++) {
    sheet.setRowHeight(studentRow, 34);
  }

  sheet.setColumnWidth(1, 125);
  sheet.setColumnWidth(2, 120);
  sheet.setColumnWidth(3, 120);
  sheet.setColumnWidth(4, 120);
  sheet.setColumnWidth(5, 330);

  return titleRow;
}

function addSummaryMarker_(cell, type, lessonNumber, color, isPractice) {
  var events = [];
  var note = cell.getNote();

  if (note && note.indexOf("myenglish-events:") === 0) {
    try {
      events = JSON.parse(note.substring("myenglish-events:".length));
    } catch (ignored) {
      events = [];
    }
  }

  if (!isPractice) {
    for (var i = 0; i < events.length; i++) {
      if (events[i].type === type && events[i].lesson === lessonNumber) return;
    }
  }

  events.push({ type: type, lesson: lessonNumber, color: color });
  cell.setNote("myenglish-events:" + JSON.stringify(events));
  renderSummaryMarkers_(cell, events);
}

function renderSummaryMarkers_(cell, events) {
  var text = "";
  var styleRanges = [];

  for (var i = 0; i < events.length; i++) {
    if (i > 0) text += "  ";

    var marker = circledSummaryNumber_(events[i].lesson);
    var start = text.length;
    text += marker;
    var end = text.length;

    styleRanges.push({
      start: start,
      end: end,
      color: events[i].color
    });
  }

  var builder = SpreadsheetApp.newRichTextValue().setText(text);

  for (var j = 0; j < styleRanges.length; j++) {
    var style = SpreadsheetApp.newTextStyle()
      .setForegroundColor(styleRanges[j].color)
      .setBold(true)
      .setFontSize(14)
      .build();

    builder.setTextStyle(styleRanges[j].start, styleRanges[j].end, style);
  }

  cell
    .setRichTextValue(builder.build())
    .setHorizontalAlignment("center")
    .setVerticalAlignment("middle")
    .setWrap(true);
}

function circledSummaryNumber_(number) {
  var circled = [
    "", "①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨", "⑩",
    "⑪", "⑫", "⑬", "⑭", "⑮", "⑯", "⑰", "⑱", "⑲", "⑳",
    "㉑", "㉒", "㉓", "㉔", "㉕", "㉖", "㉗", "㉘", "㉙", "㉚",
    "㉛", "㉜", "㉝", "㉞", "㉟", "㊱", "㊲", "㊳", "㊴", "㊵",
    "㊶", "㊷", "㊸", "㊹", "㊺", "㊻", "㊼", "㊽", "㊾", "㊿"
  ];

  return number >= 1 && number < circled.length ? circled[number] : "(" + number + ")";
}
