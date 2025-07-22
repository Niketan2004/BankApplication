package com.BankProject.BankApplication.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.BankProject.BankApplication.Entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, String> {
      @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
      Optional<User> findUserByEmailIgnoreCase(@Param("email") String email);// @Param("email")
}
