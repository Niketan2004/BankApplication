package com.BankProject.BankApplication.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {
     @Autowired
     private JavaMailSender javaMailSender;

     @Async
     public void sendVerificationEmail(String to, String token) {
          log.info("Token is: {}", token);

          String subject = "Verify your account";
          String verificationUrl = "http://localhost:8080/user/verify?token=" + token;

          String htmlContent = "<html>"
                    + "<body>"
                    + "<h2>Thank you for registering! Into SecureBank</h2>"
                    + "<p>Please click the button below to verify your account. Please remeber that Link is only valid till 2hrs:</p>"
                    + "<a href='" + verificationUrl + "' style='display:inline-block;padding:10px 20px;"
                    + "background-color:#4CAF50;color:white;text-decoration:none;border-radius:5px;'>Verify Account</a>"
                    + "<p>If the button doesn't work, copy and paste this link into your browser:</p>"
                    + "<p><a href='" + verificationUrl + "'>" + verificationUrl + "</a></p>"
                    + "</body>"
                    + "</html>";

          try {
               MimeMessage mimeMessage = javaMailSender.createMimeMessage();
               MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

               helper.setTo(to);
               helper.setSubject(subject);
               helper.setText(htmlContent, true); // true = HTML

               javaMailSender.send(mimeMessage);

               log.info("Verification HTML email sent to: {}", to);
          } catch (MessagingException e) {
               log.error("Failed to send HTML email to: {}", to, e);
          }
     }

     // @Async
     // public void sendVerificationEmail(String to, String token) {
     // log.info("Token is: {}", token);
     // String subject = "Verify your account";
     // String verificationUrl = "http://localhost:8080/user/verify?token=" + token;

     // String message = "Thank you for registering.\nPlease click the link below to
     // verify your account:\n"
     // + verificationUrl;

     // SimpleMailMessage email = new SimpleMailMessage();
     // email.setTo(to);
     // email.setSubject(subject);
     // email.setText(message);

     // javaMailSender.send(email);
     // }
}
