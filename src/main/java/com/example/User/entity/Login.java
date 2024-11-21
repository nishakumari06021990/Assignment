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
public class Login {
    private String uuid;
    private String username;
    private String password;
    private String salt;
    private String md5;
    private String sha1;
    private String sha256;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return Objects.equals(uuid, login.getUuid()) &&
                Objects.equals(username, login.getUsername()) &&
                Objects.equals(password, login.getPassword()) &&
                Objects.equals(salt, login.getSalt()) &&
                Objects.equals(md5, login.getMd5()) &&
                Objects.equals(sha1, login.getSha1()) &&
                Objects.equals(sha256, login.getSha256()) ;

    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, username, password, salt, md5, sha1, sha256);
    }
}
