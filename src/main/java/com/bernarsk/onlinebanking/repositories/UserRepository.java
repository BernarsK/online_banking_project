package com.bernarsk.onlinebanking.repositories;

import com.bernarsk.onlinebanking.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
    @Query("SELECT u.userLevel FROM User u WHERE u.email = :email")
    Integer findUserLevelByEmail(@Param("email") String email);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Integer findUserIdByEmail(@Param("email") String email);
}