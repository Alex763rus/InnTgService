package com.example.inntgservice.service.excel;

import com.example.inntgservice.exception.UploadProcessingException;
import com.example.inntgservice.model.jpa.InnInfo;
import com.example.inntgservice.model.jpa.InnInfoRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
@Slf4j
public class InnUploaderService extends ConvertServiceBase {
    private final int START_ROW = 1;

    private int LAST_ROW;

    @Autowired
    private InnInfoRepository innInfoRepository;

    public void uploadBookToDb(XSSFWorkbook book) {
        int row = START_ROW;
        sheet = book.getSheetAt(0);
        InnInfo innInfo;
        int column = -1;

        LAST_ROW = getLastRow(START_ROW);
        log.info("Строк в файле:" + LAST_ROW);
        LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
        val innInfoList = new LinkedList<InnInfo>();
        for (; row <= LAST_ROW; ++row) {
            if (row % 5000 == 0) {
                innInfoRepository.saveAll(innInfoList);
                innInfoList.clear();
                log.info("Успешно сохранились еще 5 к записей. row = " + row);
            }
            try {
                innInfo = new InnInfo();
                innInfo.setNumber_col(getCellValueLong(row, ++column));
                innInfo.setBrief(getCellValue(row, ++column));
                innInfo.setRegistrationNumber(getCellValueLong(row, ++column));
                innInfo.setAddress(getCellValue(row, ++column));
                innInfo.setHeadFio(getCellValue(row, ++column));
                innInfo.setHeadInn(getCellValueLong(row, ++column));
                innInfo.setPhone(getCellValue(row, ++column));
                innInfo.setMail(getCellValue(row, ++column));
                innInfo.setWebsite(getCellValue(row, ++column));
                innInfo.setRegisteredDate(getCellDate(row, ++column));
                innInfo.setCompanyAge(getCellValueDouble(row, ++column));
                innInfo.setCoOwnersPriority(getCellValue(row, ++column));
                innInfo.setTypeOfActivity(getCellValue(row, ++column));
                innInfo.setSummaryIndicator(getCellValue(row, ++column));
                innInfo.setInnWinner(getCellValueLong(row, ++column));
                innInfo.setCreditLimit(getCellValueDouble(row, ++column));
                innInfo.setAmountPendingClaims(getCellValueDouble(row, ++column));
                innInfo.setDeliveryItem(getCellValue(row, ++column));
                innInfo.setAverageNumberOfEmployees2022(getCellValueLong(row, ++column));
                innInfo.setComment(getCellValue(row, ++column));
                innInfo.setImportantInfo(getCellValue(row, ++column));
                innInfo.setMyLists(getCellValue(row, ++column));
                innInfo.setSparkRegistry(getCellValue(row, ++column));
                innInfo.setRevenue2022(getCellValueDouble(row, ++column));
                innInfo.setNetProfit2022(getCellValueDouble(row, ++column));
                innInfo.setCapitalAndReserves(getCellValueDouble(row, ++column));
                innInfo.setFixedAssets2022(getCellValueDouble(row, ++column));
                innInfo.setCreditAssetsShort2022(getCellValueDouble(row, ++column));
                innInfo.setCreditAssetsLong2022(getCellValueDouble(row, ++column));
                innInfo.setLiabilitiesShort2022(getCellValueDouble(row, ++column));
                innInfo.setLiabilitiesOtherLong2022(getCellValueDouble(row, ++column));
                innInfo.setReserv2022(getCellValueDouble(row, ++column));
                innInfo.setAccountsReceivable2022(getCellValueDouble(row, ++column));
                innInfo.setAccountsPayable2022(getCellValueDouble(row, ++column));
                innInfo.setOwnWorkingCapital2022(getCellValueDouble(row, ++column));
                innInfo.setSalary2022(getCellValueDouble(row, ++column));
                innInfo.setInterestPayments2022(getCellValueDoubleFromString(row, ++column));
                innInfo.setBalanceCurrency2022(getCellValueDouble(row, ++column));
                //innInfoRepository.save(innInfo);
                innInfoList.add(innInfo);
            } catch (Exception e) {
                log.error("не удалось обработать строку:" + row
                        + " , столбец:" + column
                        + " , значение:" + getCellValue(row, column)
                        + " , инн:" + getCellValue(row, 14)
                        + ". Ошибка:" + e.getMessage());
            } finally {
                column = -1;
            }
        }
        innInfoRepository.saveAll(innInfoList);
        log.info("Успешно сохранились остатки. Всего сохранено записей:" + (row - 1));
    }

}
