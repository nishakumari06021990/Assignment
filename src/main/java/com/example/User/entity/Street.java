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
public class Street {
    private int number;

    @JsonProperty("name")
    private String streetName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Street street = (Street) o;
        return Objects.equals(number, street.getNumber()) &&
                Objects.equals(streetName, street.getStreetName()) ;

    }

    @Override
    public int hashCode() {
        return Objects.hash(number, streetName);
    }
}
