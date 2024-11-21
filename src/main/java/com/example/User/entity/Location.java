package com.example.User.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
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
public class Location {
    @Embedded
    private Street street;
    private String city;
    private String state;
    private String country;
    private String postcode;

    @Embedded
    private Coordinates coordinates;

    @Embedded
    private Timezone timezone;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return   Objects.equals(postcode,location.postcode)&&
                Objects.equals(street, location.street) &&
                Objects.equals(city, location.city) &&
                Objects.equals(state, location.state) &&
                Objects.equals(country, location.country) &&
                Objects.equals(coordinates, location.coordinates) &&
                Objects.equals(timezone, location.timezone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, country, postcode, coordinates, timezone);
    }
}
