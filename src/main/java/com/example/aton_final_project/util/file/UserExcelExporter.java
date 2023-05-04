package com.example.aton_final_project.util.file;

import com.example.aton_final_project.model.dto.AccessTokenDto;
import com.example.aton_final_project.model.dto.FilesDto;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.aton_final_project.util.constants.FontConstants.CUSTOM_FONT;
import static com.example.aton_final_project.util.parse.Parsing.parsingFileName;

public class UserExcelExporter {
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public UserExcelExporter() {
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine(String username) {
        sheet = workbook.createSheet(username);

        Row row = sheet.createRow(1);

        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        font.setBold(true);
        font.setFontHeight(12);
        font.setFontName(CUSTOM_FONT.getValue());
        style.setFont(font);

        createCell(row, 1, "암호화키", style);
        createCell(row, 2, "접속키", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        sheet.setColumnWidth(columnCount, (sheet.getColumnWidth(columnCount)) + (short) 1024 * 8); //너비 더 넓게
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines(AccessTokenDto accessToken) {

        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        font.setFontHeight(12);
        font.setFontName("맑은 고딕");
        style.setFont(font);

        Row row = sheet.createRow(2);
        int columnCount = 1;

        createCell(row, columnCount, accessToken.getEncryptKey(), style);
        createCell(row, columnCount + 1, accessToken.getAuthorization(), style);
    }

    public void download(HttpServletResponse response, String username, AccessTokenDto accessToken) throws IOException {
        writeHeaderLine(username);
        writeDataLines(accessToken);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    public FilesDto export(HttpServletResponse response, String username, AccessTokenDto accessToken) throws IOException {
        writeHeaderLine(username);
        writeDataLines(accessToken);

        String fileName = parsingFileName(username);
        File file = new File("src/main/resources/uploaded_files/excel/" + fileName + ".xlsx");
        FileOutputStream fileOut = new FileOutputStream(file);
        workbook.write(fileOut);
        workbook.close();

        fileOut.close();

        return FilesDto.builder()
                .filename(file.getName())
                .fileUrl(file.getPath())
                .build();
    }
}
