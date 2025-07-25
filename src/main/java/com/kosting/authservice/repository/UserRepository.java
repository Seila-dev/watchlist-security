package com.kosting.authservice.repository;

import com.kosting.authservice.core.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM Usuario u WHERE u.login = :login")
    UserDetails findByLogin(@Param("login") String login);

    boolean existsByLogin(@Email String login);
}
