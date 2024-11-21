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
public class Timezone {
    private String offset;
    private String description;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timezone timezone = (Timezone) o;
        return Objects.equals(offset, timezone.offset) &&
                Objects.equals(description, timezone.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, description);
    }
}
