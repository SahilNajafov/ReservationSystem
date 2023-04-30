package com.example.ReservationSystem.repository;

import com.example.ReservationSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User,Long> {

    Optional<User> findUserByEmail(String email);

    @Query("SELECT n from User n where n.id=:xxx")
    User findUserById(@Param("xxx") Long id);

    @Query("SELECT n.id from User n where n.email=:xxx")
    Long findIdByEmail(@Param("xxx") String email);

    Optional<User> findUserByNumber(String number);

}