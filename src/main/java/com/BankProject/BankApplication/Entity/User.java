package com.BankProject.BankApplication.Entity;



import com.BankProject.BankApplication.Enum.Role;
import jakarta.persistence.*; // Import all from persistence
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users") // Good practice to explicitly name table if it conflicts with SQL keywords
public class User {
     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     private String userId;

     @NotNull(message = "Please Enter Your Full name")
     private String fullName;

     @Email(message = "Please Enter a Valid Email")
     @Column(unique = true,nullable = false)
     private String email;

     @NotNull(message = "Please Provide a Password")
     private String password;

     @Column(nullable = false)
     private Boolean isEnabled;

     @Enumerated(EnumType.STRING)
     private Role role;

     

     // One-to-one relationship with Account
     // User is the "owning" side here, meaning 'users' table will have a foreign key
     // to 'accounts'
     @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // CascadeType.ALL: Operations on User cascade to Account
     @JoinColumn(name = "account_id", referencedColumnName = "accountNumber", unique = true) // Foreign key in 'users'  table
     private Account account; // Field name is 'account'

}