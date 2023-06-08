package com.example.inntgservice.model.jpa;

import com.example.inntgservice.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity(name = "inn_info")
public class InnInfo {

    @Column(name = "number_col")
    private Long number_col;

    @Column(name = "brief", length = 1024)
    private String brief;

    @Column(name = "registration_number")
    private Long registrationNumber;

    @Column(name = "address", length = 1024)
    private String address;

    @Column(name = "head_fio", length = 1024)
    private String headFio;

    @Column(name = "head_inn")
    private Long headInn;
    @Column(name = "phone", length = 1024)
    private String phone;

    @Column(name = "mail", length = 1024)
    private String mail;
    @Column(name = "website", length = 1024)
    private String website;

    @Column(name = "registered_date")
    private Date registeredDate;

    @Column(name = "company_age")
    private Double companyAge;

    @Column(name = "co_owners_priority", columnDefinition="TEXT")
    private String coOwnersPriority;

    @Column(name = "type_of_activity", length = 1024)
    private String typeOfActivity;

    @Column(name = "summary_indicator", length = 1024)
    private String SummaryIndicator;

    @Id
    @Column(name = "inn_winner")
    private Long innWinner;

    @Column(name = "credit_limit")
    private Double creditLimit;

    @Column(name = "amount_pending_claims")
    private Double amountPendingClaims;

    @Column(name = "delivery_item", columnDefinition="TEXT")
    private String deliveryItem;

    @Column(name = "average_number_of_employees_2022")
    private Long averageNumberOfEmployees2022;

    @Column(name = "comment", length = 1024)
    private String comment;

    @Column(name = "important_info", length = 1024)
    private String importantInfo;

    @Column(name = "my_lists", columnDefinition="TEXT")
    private String myLists;
    @Column(name = "spark_registry", length = 1024)
    private String sparkRegistry;

    @Column(name = "revenue_2022")
    private Double revenue2022;

    @Column(name = "net_profit_2022")
    private Double netProfit2022;

    @Column(name = "capital_and_reserves_2022")
    private Double capitalAndReserves;
    @Column(name = "fixed_assets_2022")
    private Double fixedAssets2022;

    @Column(name = "credit_assets_short_2022")
    private Double creditAssetsShort2022;

    @Column(name = "credit_assets_long_2022")
    private Double creditAssetsLong2022;

    @Column(name = "liabilities_short_2022")
    private Double liabilitiesShort2022;

    @Column(name = "liabilities_other_long_2022")
    private Double liabilitiesOtherLong2022;

    @Column(name = "reserv_2022")
    private Double reserv2022;

    @Column(name = "accounts_receivable_2022")
    private Double accountsReceivable2022;

    @Column(name = "accounts_payable_2022")
    private Double accountsPayable2022;

    @Column(name = "own_working_capital_2022")
    private Double ownWorkingCapital2022;

    @Column(name = "salary_2022")
    private Double salary2022;

    @Column(name = "interest_payments_2022")
    private Double interestPayments2022;

    @Column(name = "balance_currency_2022")
    private Double balanceCurrency2022;

    @Override
    public String toString() {
        return "InnInfo{" +
                "\nnumber_col=" + number_col +
                "\n, brief='" + brief + '\'' +
                "\n, registrationNumber=" + registrationNumber +
                "\n, address='" + address + '\'' +
                "\n, headFio='" + headFio + '\'' +
                "\n, headInn=" + headInn +
                "\n, phone='" + phone + '\'' +
                "\n, mail='" + mail + '\'' +
                "\n, website='" + website + '\'' +
                "\n, registeredDate=" + registeredDate +
                "\n, companyAge=" + companyAge +
                "\n, coOwnersPriority='" + coOwnersPriority + '\'' +
                "\n, typeOfActivity='" + typeOfActivity + '\'' +
                "\n, SummaryIndicator='" + SummaryIndicator + '\'' +
                "\n, innWinner=" + innWinner +
                "\n, creditLimit=" + creditLimit +
                "\n, amountPendingClaims=" + amountPendingClaims +
                "\n, deliveryItem='" + deliveryItem + '\'' +
                "\n, averageNumberOfEmployees2022=" + averageNumberOfEmployees2022 +
                "\n, comment='" + comment + '\'' +
                "\n, importantInfo='" + importantInfo + '\'' +
                "\n, myLists='" + myLists + '\'' +
                "\n, sparkRegistry='" + sparkRegistry + '\'' +
                "\n, revenue2022=" + revenue2022 +
                "\n, netProfit2022=" + netProfit2022 +
                "\n, capitalAndReserves=" + capitalAndReserves +
                "\n, fixedAssets2022=" + fixedAssets2022 +
                "\n, creditAssetsShort2022=" + creditAssetsShort2022 +
                "\n, creditAssetsLong2022=" + creditAssetsLong2022 +
                "\n, liabilitiesShort2022=" + liabilitiesShort2022 +
                "\n, liabilitiesOtherLong2022=" + liabilitiesOtherLong2022 +
                "\n, reserv2022=" + reserv2022 +
                "\n, accountsReceivable2022=" + accountsReceivable2022 +
                "\n, accountsPayable2022=" + accountsPayable2022 +
                "\n, ownWorkingCapital2022=" + ownWorkingCapital2022 +
                "\n, salary2022=" + salary2022 +
                "\n, interestPayments2022=" + interestPayments2022 +
                "\n, balanceCurrency2022=" + balanceCurrency2022 +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InnInfo innInfo = (InnInfo) o;
        return Objects.equals(innWinner, innInfo.innWinner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(innWinner);
    }
}
