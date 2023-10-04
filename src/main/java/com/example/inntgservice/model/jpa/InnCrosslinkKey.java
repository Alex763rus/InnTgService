package com.example.inntgservice.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class InnCrosslinkKey implements Serializable {

    @Column(name = "inn_first")
    private Long innFirst;

    @Column(name = "inn_second")
    private Long innSecond;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InnCrosslinkKey that = (InnCrosslinkKey) o;
        return Objects.equals(innFirst, that.innFirst) && Objects.equals(innSecond, that.innSecond);
    }

    @Override
    public int hashCode() {
        return Objects.hash(innFirst, innSecond);
    }
}
