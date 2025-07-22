package com.BankProject.BankApplication.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BankProject.BankApplication.Entity.VerificationToken;
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
     VerificationToken findByToken(String token);
}
