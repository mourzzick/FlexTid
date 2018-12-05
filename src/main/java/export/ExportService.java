package export;

import model.TimeLog;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class ExportService {

    public Workbook exportToExcel(List<TimeLog> timeLogs) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tidsregistreringar");
        Row row = sheet.createRow(0);

        /*
        Creates and sets bold font for header
         */
        CellStyle headerStyle = workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        headerStyle.setFont(boldFont);

        Cell id = row.createCell(0);
        id.setCellStyle(headerStyle);
        id.setCellValue("ID");

        Cell day = row.createCell(1);
        day.setCellStyle(headerStyle);
        day.setCellValue("Datum");

        Cell comment = row.createCell(2);
        comment.setCellStyle(headerStyle);
        comment.setCellValue("Kommentar");

        Cell workedHours = row.createCell(3);
        workedHours.setCellStyle(headerStyle);
        workedHours.setCellValue("Arbetade timmar");

        Cell dueHours = row.createCell(4);
        dueHours.setCellStyle(headerStyle);
        dueHours.setCellValue("Schemalagda timmar");

        Cell balance = row.createCell(5);
        balance.setCellStyle(headerStyle);
        balance.setCellValue("Dagssaldo");

        /*
        Creates grey cell background
         */
        CellStyle greyBackground = workbook.createCellStyle();
        greyBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        greyBackground.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        /*
        Iterates and creates rows and cells from list of timelogs.
         */
        for (int i = 0; i < timeLogs.size(); i++){
            row = sheet.createRow(i+1);
            id = row.createCell(0);
            id.setCellValue(timeLogs.get(i).getTimeLogID());
            day = row.createCell(1);
            day.setCellValue(timeLogs.get(i).getWorkDay().toString());
            comment = row.createCell(2);
            comment.setCellValue(timeLogs.get(i).getComment());
            workedHours = row.createCell(3);
            workedHours.setCellValue(timeLogs.get(i).getWorkedHours());
            dueHours = row.createCell(4);
            dueHours.setCellValue(timeLogs.get(i).getDueHours());
            balance = row.createCell(5);
            balance.setCellValue(timeLogs.get(i).getBalance());
            if(i % 2 != 0){ //Sets every second row with different background
                id.setCellStyle(greyBackground);
                day.setCellStyle(greyBackground);
                comment.setCellStyle(greyBackground);
                workedHours.setCellStyle(greyBackground);
                dueHours.setCellStyle(greyBackground);
                balance.setCellStyle(greyBackground);
            }
        }
        //Sets siza of column based on longest value
        for (int columnIndex = 0; columnIndex < 5; columnIndex++ ){
            sheet.autoSizeColumn(columnIndex);
        }
        return workbook;
    }
}
