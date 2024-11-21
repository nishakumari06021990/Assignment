package com.example.User.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Embeddable
public class Dob {

    @JsonProperty("date")
    private String dobDate;

    @JsonProperty("age")
    private int dobAge;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dob dob = (Dob) o;
        return Objects.equals(dobDate, dob.getDobDate()) &&
                Objects.equals(dobAge, dob.getDobAge()) ;

    }

    @Override
    public int hashCode() {
        return Objects.hash(dobDate, dobAge);
    }
}
