package com.example.inntgservice.model.menu;

import com.example.inntgservice.model.jpa.InnInfo;
import com.example.inntgservice.model.jpa.Statistic;
import com.example.inntgservice.model.jpa.User;
import com.example.inntgservice.model.wpapper.SendMessageWrap;
import jakarta.persistence.MappedSuperclass;
import lombok.val;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import static com.example.inntgservice.constant.Constant.NEW_LINE;
import static com.example.inntgservice.constant.Constant.STAR;
import static com.example.inntgservice.enums.State.FREE;
import static com.example.inntgservice.enums.State.WAIT_INN;
import static com.example.inntgservice.utils.DateConverter.TEMPLATE_DATE_DOT;
import static com.example.inntgservice.utils.DateConverter.convertDateFormat;
import static com.example.inntgservice.utils.StringUtils.convertDoubleFormat;
import static com.example.inntgservice.utils.StringUtils.prepareShield;

@MappedSuperclass
public abstract class MenuSearchByInnBase extends Menu {

    protected List<PartialBotApiMethod> inputInnLogic(User user, Update update) {
        stateService.setState(user, WAIT_INN);
        return List.of(SendMessageWrap.init()
                .setChatIdLong(update.getMessage().getChatId())
                .setText("Введите ИНН:")
                .build().createSendMessage());
    }

    protected List<PartialBotApiMethod> waitInnLogic(User user, Update update) throws ParseException {
        if (!update.hasMessage()) {
            return errorMessageDefault(update);
        }
        val message = update.getMessage().getText();
        Long inn = 0L;
        try {
            inn = Long.parseLong(message);
        } catch (Exception ex) {
            return errorMessage(update, "Некорректный ИНН, невозможно преобразовать к Long: " + message);
        }
        val innInfo = innInfoRepository.findById(inn).orElse(null);
        if (innInfo == null) {
            return errorMessage(update, "По указанному ИНН записей не найдено: " + inn);
        }
        val statistic = new Statistic();
        statistic.setUser(user);
        statistic.setInn(inn);
        statistic.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
        statisticRepository.save(statistic);
        stateService.setState(user, FREE);
        return List.of(SendMessageWrap.init()
                .setChatIdLong(user.getChatId())
                .setText("Запись в БД найдена:" + NEW_LINE + getInnInfoString(innInfo))
                .build().createSendMessage());
    }

    private String getPrepareParameter(String brief, String value) {
        val prepareParametr = new StringBuilder();
        prepareParametr
                .append(STAR).append(brief).append(STAR).append(prepareShield(value)).append(NEW_LINE);
        return prepareParametr.toString();
    }

    private String getPrepareParameterWithoutShield(String brief, String value) {
        val prepareParametr = new StringBuilder();
        prepareParametr
                .append(STAR).append(brief).append(STAR).append(value).append(NEW_LINE);
        return prepareParametr.toString();
    }

    private String getPrepareParameter(String brief, Long value) {
        val prepareParametr = new StringBuilder();
        prepareParametr
                .append(STAR).append(brief).append(STAR).append(value).append(NEW_LINE);
        return prepareParametr.toString();
    }

    private String getInnInfoString(InnInfo innInfo) throws ParseException {
        val innInfoString = new StringBuilder();
        innInfoString
                .append(getPrepareParameter("Номер: ", innInfo.getNumber_col()))
                .append(getPrepareParameter("Наименование: ", innInfo.getBrief()))
                .append(getPrepareParameter("Регистрационный номер: ", innInfo.getRegistrationNumber()))
                .append(getPrepareParameter("Адрес (место нахождения): ", innInfo.getAddress()))
                .append(getPrepareParameter("Руководитель - ФИО: ", innInfo.getHeadFio()))
                .append(getPrepareParameter("Руководитель - ИНН: ", innInfo.getHeadInn()))
                .append(getPrepareParameterWithoutShield("Телефон: ", innInfo.getPhone()))
                .append(getPrepareParameter("Электронный адрес: ", innInfo.getMail()))
                .append(getPrepareParameter("Сайт в сети Интернет: ", innInfo.getWebsite()))
                .append(getPrepareParameter("Дата регистрации: ", convertDateFormat(innInfo.getRegisteredDate(), TEMPLATE_DATE_DOT)))
                .append(getPrepareParameter("Возраст компании, лет: ", String.valueOf(innInfo.getCompanyAge())))
                .append(getPrepareParameter("Совладельцы, Приоритетный: ", innInfo.getCoOwnersPriority()))
                .append(getPrepareParameter("Вид деятельности/отрасль: ", innInfo.getTypeOfActivity()))
                .append(getPrepareParameter("Сводный индикатор: ", innInfo.getSummaryIndicator()))
                .append(getPrepareParameter("ИНН Победителя: ", innInfo.getInnWinner()))
                .append(getPrepareParameter("Кредитный лимит, тыс. RUB: ", String.valueOf(innInfo.getCreditLimit())))
                .append(getPrepareParameter("Сумма незавершенных исков в роли ответчика, тыс. RUB: ", String.valueOf(innInfo.getAmountPendingClaims())))
                .append(getPrepareParameter("Предмет поставки: ", innInfo.getDeliveryItem()))
                .append(getPrepareParameter("2022, Среднесписочная численность работников: ", innInfo.getAverageNumberOfEmployees2022()))
                .append(getPrepareParameter("Комментарий: ", innInfo.getComment()))
                .append(getPrepareParameter("Важная информация: ", innInfo.getImportantInfo()))
                .append(getPrepareParameter("Мои списки: ", innInfo.getMyLists()))
                .append(getPrepareParameter("Реестры СПАРКа: ", innInfo.getSparkRegistry()))
                .append(getPrepareParameter("2022, Выручка, RUB: ", convertDoubleFormat(innInfo.getRevenue2022())))
                .append(getPrepareParameter("2022, Чистая прибыль (убыток), RUB: ", convertDoubleFormat(innInfo.getNetProfit2022())))
                .append(getPrepareParameter("2022, Капитал и резервы, RUB: ", convertDoubleFormat(innInfo.getCapitalAndReserves())))
                .append(getPrepareParameter("2022, Основные средства , RUB: ", convertDoubleFormat(innInfo.getFixedAssets2022())))
                .append(getPrepareParameter("2022, Заёмные средства (краткосрочные), RUB: ", convertDoubleFormat(innInfo.getCreditAssetsShort2022())))
                .append(getPrepareParameter("2022, Заёмные средства (долгосрочные), RUB: ", convertDoubleFormat(innInfo.getCreditAssetsLong2022())))
                .append(getPrepareParameter("2022, Краткосрочные обязательства, RUB: ", convertDoubleFormat(innInfo.getLiabilitiesShort2022())))
                .append(getPrepareParameter("2022, Прочие долгосрочные обязательства, RUB: ", convertDoubleFormat(innInfo.getLiabilitiesOtherLong2022())))
                .append(getPrepareParameter("2022, Запасы, RUB: ", convertDoubleFormat(innInfo.getReserv2022())))
                .append(getPrepareParameter("2022, Дебиторская задолженность, RUB: ", convertDoubleFormat(innInfo.getAccountsReceivable2022())))
                .append(getPrepareParameter("2022, Кредиторская задолженность, RUB: ", convertDoubleFormat(innInfo.getAccountsPayable2022())))
                .append(getPrepareParameter("2022, Собственный оборотный капитал, RUB: ", convertDoubleFormat(innInfo.getOwnWorkingCapital2022())))
                .append(getPrepareParameter("2022, Оплата труда, RUB: ", convertDoubleFormat(innInfo.getSalary2022())))
                .append(getPrepareParameter("2022, Платежи по процентам, RUB: ", convertDoubleFormat(innInfo.getInterestPayments2022())))
                .append(getPrepareParameter("2022, Валюта баланса, RUB: ", convertDoubleFormat(innInfo.getBalanceCurrency2022())))
        ;
        return innInfoString.toString();
    }
}
