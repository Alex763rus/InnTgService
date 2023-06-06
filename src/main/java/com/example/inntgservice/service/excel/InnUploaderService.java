package com.example.inntgservice.service.excel;

import com.example.inntgservice.model.dictionary.excel.Header;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.inntgservice.utils.DateConverter.*;

@Component
public class InnUploaderService extends ConvertServiceBase {
    private int START_ROW;

    private int LAST_ROW;

    public List<List<String>> getConvertedBook(XSSFWorkbook book) {
        val data = new ArrayList<List<String>>();
        data.add(Header.headersOutput);
        sheet = book.getSheetAt(0);
        if(getCellValue(1, 0).equals("")){
            START_ROW = 3;
        } else{
            START_ROW = 1;
        }
        int row = START_ROW;
        int counterCopy = 1;
        ArrayList<String> dataLine = new ArrayList();
        try {

            LAST_ROW = getLastRow(START_ROW);
            LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
            for (; row <= LAST_ROW; ++row) {
                val isStart = isStart(row);
                if (isStart) {
                    counterCopy = 1;
                }
                dataLine = new ArrayList<>();
                dataLine.add(getCellValue(row, 1));
                dataLine.add(getCurrentDate(TEMPLATE_DATE_DOT));
                dataLine.add("ООО ''ЛЕНТА''");
                dataLine.add(getCellValue(row, 8));
                dataLine.add("");
                dataLine.add("Рефрижератор");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("5000");
                dataLine.add("1");
                dataLine.add("2");
                dataLine.add("6");
                dataLine.add("2");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add(isStart ? "Погрузка" : "Разгрузка");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add(getCellValue(row, 5).replaceAll(" ", ""));
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");
                dataLine.add("");

                ++counterCopy;
                data.add(dataLine);
            }
        } catch (Exception e) {
//            throw new ConvertProcessingException("не удалось обработать строку:" + row
//                    + " , после значения:" + dataLine + ". Ошибка:" + e.getMessage());
        }
        return data;
    }

    private String getValueOrDefault(int row, int slippage, int col) {
        row = row + slippage;
        if (row < START_ROW || row > LAST_ROW) {
            return "";
        }
        if (col < 0 || col > LAST_COLUMN_NUMBER || sheet.getRow(row) == null) {
            return "";
        }
        return getCellValue(sheet.getRow(row).getCell(col));
    }

    private boolean isStart(int row) {
        if (row == START_ROW) {
            return true;
        }
        val cur = getValueOrDefault(row, 0, 1);
        val prev1 = getValueOrDefault(row, -1, 1);
        return !(cur.equals(prev1) || row == (START_ROW + 1) || row == (START_ROW) || prev1.equals(""));
    }
}
