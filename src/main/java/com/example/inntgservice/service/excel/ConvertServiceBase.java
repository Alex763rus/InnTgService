package com.example.inntgservice.service.excel;

import lombok.val;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.inntgservice.utils.DateConverter.convertDateFormat;


@Component
public class ConvertServiceBase {

    protected XSSFSheet sheet;

    protected int LAST_COLUMN_NUMBER;

    protected String getCellValue(int row, int col) {
        if (sheet.getRow(row) == null) {
            return "";
        }
        if (sheet.getRow(row).getCell(col) == null) {
            return "";
        }
        return getCellValue(sheet.getRow(row).getCell(col));
    }

    protected String getCellValue(XSSFCell xssfCell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(xssfCell);
    }
    protected String getCellWindowValue(XSSFSheet sheet, int row, int col) {
        if (sheet.getRow(row) == null) {
            return "";
        }
        if (sheet.getRow(row).getCell(col) == null) {
            return "";
        }
        return getCellValue(sheet.getRow(row).getCell(col));
    }

    protected List<List<String>> getDataFromSheet(XSSFWorkbook book, String sheetName, final int colStart, final int countColumns) {
        val data = new ArrayList<List<String>>();
        val windowSheet = book.getSheet(sheetName);
        val colEnd = colStart + countColumns;
        for (int row = 0; ; row++) {
            val cellValue = getCellWindowValue(windowSheet, row, 0);
            val nextValue = getCellWindowValue(windowSheet, row + 1, 0);
            if (cellValue.equals("") && nextValue.equals("")) {
                --row;
                break;
            }
            val line = new ArrayList<String>();
            for (int column = colStart; column < colEnd; ++ column){
                line.add(getCellWindowValue(windowSheet, row, column));
            }
            data.add(line);
        }
        return data;
    }
    protected Date getCellDate(int row, int col) {
        if (sheet.getRow(row) == null) {
            return null;
        }
        if (sheet.getRow(row).getCell(col) == null) {
            return null;
        }
        return sheet.getRow(row).getCell(col).getDateCellValue();
    }

    protected int getLastRow(int startRow) {
        int i = startRow;
        for (; ; i++) {
            if (getCellValue(i, 0).equals("")) {
                if (getCellValue(i + 1, 0).equals("")) {
                    break;
                }
            }
        }
        return i - 1;
    }

    protected String getCurrentDate(String format) throws ParseException {
        return convertDateFormat(new Date(), format);
    }
}
