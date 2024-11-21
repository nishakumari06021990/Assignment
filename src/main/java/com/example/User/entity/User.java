package com.example.User.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
public class User {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Embedded
    private Id id;
    private String gender;

    @Embedded
    private Name name;

    @Embedded
    private Location location;
    private String email;
    @Embedded
    private Login login;
    @Embedded
    private Dob dob;
    @Embedded
    private Registered registered;
    private String phone;
    private String cell;
    @Embedded
    private Picture picture;
    private String nat;

    public User(User other) {
        if (other != null) {
            this.userId = other.userId;
            this.id = other.id != null ? new Id(other.id.getIdName(),other.getId().getValue()) : null;
            this.gender = other.gender;
            this.name = other.name != null ? new Name(other.name.getTitle(),other.name.getFirst(),other.name.getLast()) : null;
            this.location = other.location != null ? new Location(other.location.getStreet(), other.location.getCity(), other.location.getState(), other.location.getCountry(), other.location.getPostcode(), other.location.getCoordinates(), other.location.getTimezone()) : null;
            this.email = other.email;
            this.login = other.login != null ? new Login(other.login.getUuid(), other.login.getUsername(), other.login.getPassword(), other.login.getSalt(), other.login.getMd5(), other.login.getSha1(), other.login.getSha256()) : null;
            this.dob = other.dob != null ? new Dob(other.dob.getDobDate(),other.dob.getDobAge()) : null;
            this.registered = other.registered != null ? new Registered(other.registered.getDate(),other.registered.getAge()) : null;
            this.phone = other.phone;
            this.cell = other.cell;
            this.picture = other.picture != null ? new Picture(other.picture.getLarge(),other.picture.getMedium(),other.picture.getThumbnail()) : null;
            this.nat = other.nat;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return
                Objects.equals(gender, user.gender) &&
                Objects.equals(name, user.name) &&
                        Objects.equals(registered, user.registered) &&
                Objects.equals(location, user.location) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(cell, user.cell) &&
                Objects.equals(login, user.login) &&
                Objects.equals(dob, user.dob) &&
                Objects.equals(id, user.id) &&
                Objects.equals(picture, user.picture) &&
                Objects.equals(nat, user.nat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gender, name, location, email, phone, cell, login, dob,registered, picture, nat);
    }
}
