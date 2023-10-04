package com.example.inntgservice.model.jpa;

import com.example.inntgservice.enums.CrossLinkType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity(name = "inn_cross_link")
public class InnCrosslink {

    @EmbeddedId
    private InnCrosslinkKey innCrosslinkKey;

    @Column(name = "cross_link_type")
    private CrossLinkType crossLinkType;

    @Override
    public String toString() {
        return "InnCrosslink{" +
                "innCrosslinkKey=" + innCrosslinkKey +
                ", crossLinkType=" + crossLinkType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InnCrosslink that = (InnCrosslink) o;
        return Objects.equals(innCrosslinkKey, that.innCrosslinkKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(innCrosslinkKey);
    }
}
