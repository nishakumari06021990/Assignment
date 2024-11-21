package com.example.User.repository;

import com.example.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * gets the user by phone and cell number.
     *
     * @param phone The phone number of the user.
     * @param cell The cell number of the user.
     * @return The user object if found, empty otherwise.
     */
    Optional<User> findByPhoneAndCell(String phone, String cell);

}

