package com.example.inntgservice.model.jpa;

import com.example.inntgservice.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity(name = "statistic")
public class Statistic {

    @Id
    @Column(name = "statistic_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statisticId;

    @Column(name = "inn")
    private Long inn;

    @Column(name = "inn_head")
    private Long innHead;

    @Column(name = "mail")
    private String mail;

    @Column(name = "phone")
    private String phone;

    @Column(name = "website")
    private String website;
    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "chatId")
    private User user;

    @Override
    public String toString() {
        return "Statistic{" +
                "statisticId=" + statisticId +
                ", inn=" + inn +
                ", registeredAt=" + registeredAt +
                '}';
    }

}
