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
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;


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

    protected double getCellValueDouble(int row, int col) {
        if (sheet.getRow(row) == null) {
            return 0;
        }
        if (sheet.getRow(row).getCell(col) == null) {
            return 0;
        }
        return sheet.getRow(row).getCell(col).getNumericCellValue();
    }

    protected Long getCellValueLong(int row, int col) {
        val value = getCellValue(row, col).replaceAll(" ", "");
        return value.equals("") ? 0 : Long.parseLong(value);
    }

    protected Double getCellValueDoubleFromString(int row, int col) {
        val value = getCellValue(row, col);
        return value.equals("") ? 0 : Double.parseDouble(value);
    }

    protected String getCellValue(XSSFCell xssfCell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(xssfCell);
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
        for (int i = startRow; ; i++) {
            if (getCellValue(i, 14).equals(EMPTY)) {
                return i - 1;
            }
        }
    }
}
