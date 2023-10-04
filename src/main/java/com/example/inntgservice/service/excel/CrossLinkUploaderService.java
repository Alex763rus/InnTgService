package com.example.inntgservice.service.excel;

import com.example.inntgservice.enums.CrossLinkType;
import com.example.inntgservice.exception.UploadProcessingException;
import com.example.inntgservice.model.jpa.InnCrossLinkRepository;
import com.example.inntgservice.model.jpa.InnCrosslink;
import com.example.inntgservice.model.jpa.InnCrosslinkKey;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.example.inntgservice.enums.CrossLinkType.LINK_HIGH;
import static com.example.inntgservice.utils.StringUtils.prepareShield;
import static org.example.tgcommons.constant.Constant.TextConstants.EMPTY;

@Component
@Slf4j
public class CrossLinkUploaderService extends ConvertServiceBase {
    private final int START_ROW = 4;
    private int LAST_ROW;

    private Map<String, CrossLinkType> linkTypeMap;

    @PostConstruct
    void init() {
        linkTypeMap = new HashMap<>();
        Arrays.stream(CrossLinkType.values()).forEach(link -> linkTypeMap.put(link.getTitle(), link));
    }

    @Autowired
    private InnCrossLinkRepository innCrossLinkRepository;

    public void uploadBookToDb(XSSFWorkbook book) {
        int row = START_ROW;
        sheet = book.getSheetAt(0);

        LAST_ROW = getLastRow(START_ROW);
        log.info("Строк в файле:" + LAST_ROW);
        LAST_COLUMN_NUMBER = sheet.getRow(START_ROW).getLastCellNum();
        InnCrosslink innCrosslink = new InnCrosslink();
        val innCrossLinkInfoList = new LinkedList<InnCrosslink>();
        for (; row <= LAST_ROW; ++row) {
            if (row % 12000 == 0) {
                innCrossLinkRepository.saveAll(innCrossLinkInfoList);
                innCrossLinkInfoList.clear();
                log.info("Успешно сохранились еще 4 к записей. row = " + row);
            }
            try {
                val linkType = linkTypeMap.getOrDefault(getCellValue(row, 2), null);
                if (linkType == null) {
                    throw new UploadProcessingException("Не смогли определить тип связи: " + getCellValue(row, 2));
                }
                innCrosslink = new InnCrosslink();
                val innCrosslinkKey = new InnCrosslinkKey();
                innCrosslinkKey.setInnFirst(getCellValueLong(row, 1));
                innCrosslinkKey.setInnSecond(getCellValueLong(row, 4));
                innCrosslink.setInnCrosslinkKey(innCrosslinkKey);
                innCrosslink.setCrossLinkType(linkType);

                innCrossLinkInfoList.add(innCrosslink);
            } catch (Exception e) {
                val message = "не удалось обработать строку:" + row
                        + " , значение:" + getCellValue(row, 1)
                        + " , инн:" + getCellValue(row, 4)
                        + ". Ошибка:" + e.getMessage();
                log.error(message);
                throw new UploadProcessingException(prepareShield(message));
            }
        }
        innCrossLinkRepository.saveAll(innCrossLinkInfoList);
        log.info("Успешно сохранились остатки. Всего сохранено записей:" + (row - START_ROW));
    }

    @Override
    protected int getLastRow(int startRow) {
        for (int i = startRow; ; i++) {
            if (getCellValue(i, 0).equals(EMPTY)) {
                return i - 1;
            }
        }
    }

}
