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
public class Registered {
    private String date;
    private int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registered registered = (Registered) o;
        return Objects.equals(date, registered.getDate()) &&
                Objects.equals(age, registered.getAge()) ;

    }

    @Override
    public int hashCode() {
        return Objects.hash(date, age);
    }
}
