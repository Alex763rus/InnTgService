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
import java.util.List;

import static com.example.inntgservice.constant.Constant.NEW_LINE;
import static com.example.inntgservice.constant.Constant.STAR;
import static com.example.inntgservice.enums.State.WAIT_INN;
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

    protected List<PartialBotApiMethod> waitInnLogic(User user, Update update) {
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

        return List.of(SendMessageWrap.init()
                .setChatIdLong(user.getChatId())
                .setText("Запись в БД найдена:" + NEW_LINE + getInnInfoString(innInfo))
                .build().createSendMessage());
    }

    private String getInnInfoString(InnInfo innInfo) {
        val innInfoString = new StringBuilder();
        innInfoString
                .append(STAR).append("Номер: ").append(STAR).append(innInfo.getNumber_col()).append(NEW_LINE)
                .append(STAR).append("Наименование: ").append(STAR).append(innInfo.getBrief()).append(NEW_LINE)
                .append(STAR).append("Регистрационный номер: ").append(STAR).append(innInfo.getRegistrationNumber()).append(NEW_LINE)
                .append(STAR).append("Адрес (место нахождения):").append(STAR).append(innInfo.getAddress()).append(NEW_LINE)
                .append(STAR).append("Руководитель - ФИО: ").append(STAR).append(innInfo.getHeadFio()).append(NEW_LINE)
                .append(STAR).append("Руководитель - ИНН: ").append(STAR).append(innInfo.getHeadInn()).append(NEW_LINE)
                .append(STAR).append("Телефон: ").append(STAR).append(innInfo.getPhone()).append(NEW_LINE)
                .append(STAR).append("Электронный адрес: ").append(STAR).append(innInfo.getMail()).append(NEW_LINE)
                .append(STAR).append("Сайт в сети Интернет: ").append(STAR).append(innInfo.getWebsite()).append(NEW_LINE)
                .append(STAR).append("Дата регистрации: ").append(STAR).append(innInfo.getRegisteredDate()).append(NEW_LINE)
                .append(STAR).append("Возраст компании, лет: ").append(STAR).append(innInfo.getCompanyAge()).append(NEW_LINE)
                .append(STAR).append("Совладельцы, Приоритетный: ").append(STAR).append(innInfo.getCoOwnersPriority()).append(NEW_LINE)
                .append(STAR).append("Вид деятельности/отрасль: ").append(STAR).append(innInfo.getTypeOfActivity()).append(NEW_LINE)
                .append(STAR).append("Сводный индикатор: ").append(STAR).append(innInfo.getSummaryIndicator()).append(NEW_LINE)
                .append(STAR).append("ИНН Победителя: ").append(STAR).append(innInfo.getInnWinner()).append(NEW_LINE)
                .append(STAR).append("Кредитный лимит, тыс. RUB: ").append(STAR).append(innInfo.getCreditLimit()).append(NEW_LINE)
                .append(STAR).append("Сумма незавершенных исков в роли ответчика, тыс. RUB: ").append(STAR).append(innInfo.getAmountPendingClaims()).append(NEW_LINE)
                .append(STAR).append("Предмет поставки: ").append(STAR).append(innInfo.getDeliveryItem()).append(NEW_LINE)
                .append(STAR).append("2022, Среднесписочная численность работников: ").append(STAR).append(innInfo.getAverageNumberOfEmployees2022()).append(NEW_LINE)
                .append(STAR).append("Комментарий: ").append(STAR).append(innInfo.getComment()).append(NEW_LINE)
                .append(STAR).append("Важная информация: ").append(STAR).append(innInfo.getImportantInfo()).append(NEW_LINE)
                .append(STAR).append("Мои списки: ").append(STAR).append(innInfo.getMyLists()).append(NEW_LINE)
                .append(STAR).append("Реестры СПАРКа: ").append(STAR).append(innInfo.getSparkRegistry()).append(NEW_LINE)
                .append(STAR).append("2022, Выручка, RUB: ").append(STAR).append(innInfo.getTypeOfActivity()).append(NEW_LINE)
                .append(STAR).append("индикатор: ").append(STAR).append(innInfo.getRevenue2022()).append(NEW_LINE)
                .append(STAR).append("2022, Чистая прибыль (убыток), RUB: ").append(STAR).append(innInfo.getNetProfit2022()).append(NEW_LINE)
                .append(STAR).append("2022, Капитал и резервы, RUB: ").append(STAR).append(innInfo.getCapitalAndReserves()).append(NEW_LINE)
                .append(STAR).append("2022, Основные средства , RUB: ").append(STAR).append(innInfo.getFixedAssets2022()).append(NEW_LINE)
                .append(STAR).append("2022, Заёмные средства (краткосрочные), RUB: ").append(STAR).append(innInfo.getCreditAssetsShort2022()).append(NEW_LINE)
                .append(STAR).append("2022, Заёмные средства (долгосрочные), RUB: ").append(STAR).append(innInfo.getCreditAssetsLong2022()).append(NEW_LINE)
                .append(STAR).append("2022, Краткосрочные обязательства, RUB: ").append(STAR).append(innInfo.getLiabilitiesShort2022()).append(NEW_LINE)
                .append(STAR).append("2022, Прочие долгосрочные обязательства, RUB: ").append(STAR).append(innInfo.getLiabilitiesOtherLong2022()).append(NEW_LINE)
                .append(STAR).append("2022, Запасы, RUB: ").append(STAR).append(innInfo.getReserv2022()).append(NEW_LINE)
                .append(STAR).append("2022, Дебиторская задолженность, RUB: ").append(STAR).append(innInfo.getAccountsReceivable2022()).append(NEW_LINE)
                .append(STAR).append("2022, Кредиторская задолженность, RUB: ").append(STAR).append(innInfo.getAccountsPayable2022()).append(NEW_LINE)
                .append(STAR).append("2022, Собственный оборотный капитал, RUB: ").append(STAR).append(innInfo.getOwnWorkingCapital2022()).append(NEW_LINE)
                .append(STAR).append("2022, Оплата труда, RUB: ").append(STAR).append(innInfo.getSalary2022()).append(NEW_LINE)
                .append(STAR).append("2022, Платежи по процентам, RUB: ").append(STAR).append(innInfo.getInterestPayments2022()).append(NEW_LINE)
                .append(STAR).append("2022, Валюта баланса, RUB: ").append(STAR).append(innInfo.getBalanceCurrency2022()).append(NEW_LINE)
        ;
        return innInfoString.toString();
    }
}
