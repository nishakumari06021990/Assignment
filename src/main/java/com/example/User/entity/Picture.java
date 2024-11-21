package com.example.User.entity;

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
public class Picture {
    private String large;
    private String medium;
    private String thumbnail;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Objects.equals(large, picture.large) &&
                Objects.equals(medium, picture.medium) &&
                Objects.equals(thumbnail, picture.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(large, medium, thumbnail);
    }

}
