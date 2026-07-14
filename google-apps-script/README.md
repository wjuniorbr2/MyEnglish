# Weekly general report tab

`WeeklySummary.gs` is an additive helper for the existing MyEnglish Google Apps Script. It does not replace or alter the detailed student tabs or the `Bug Reports` tab.

## Install it in the existing Apps Script project

1. Open the Apps Script project currently used by the app.
2. Add a new script file named `WeeklySummary` and paste the contents of `WeeklySummary.gs`.
3. In the existing `doPost(e)`, after the detailed student report has been written and before the response is returned, add:

   ```javascript
   try {
     updateWeeklySummaryFromPost_(e);
   } catch (summaryError) {
     console.error(summaryError);
   }
   ```

4. Save the project.
5. Open **Deploy > Manage deployments**, edit the current web-app deployment, select **New version**, and deploy it. Updating the current deployment keeps the endpoint already used by the Android app.

## Result

The helper creates a `General` tab only when the first homework or practice report arrives for a Sunday-Saturday week. Each block contains the fixed students Vinícius, Ayla, Yuri, Kalil, Beatriz D., and Junior.

- Written homework: green circled lesson number
- Listening homework: blue circled lesson number
- Spoken homework: red circled lesson number
- Practices: all colored circled lesson numbers in one wider cell

Unknown student names and bug reports are ignored by the `General` tab while the existing detailed-report logic continues normally.
