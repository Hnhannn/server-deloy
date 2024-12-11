package com.yuki.repositoty;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yuki.entity.User;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findUserByUsername(@Param("username") String username);

    @Query("SELECT u.email FROM User u WHERE u.username = :username")
    Optional<String> getEmailByUsername(@Param("username") String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.status <> 'DELETED'")
    List<User> findAllActiveUsers();

    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:fullname%")
    List<User> findUsersByFullnameContaining(@Param("fullname") String fullname);
}