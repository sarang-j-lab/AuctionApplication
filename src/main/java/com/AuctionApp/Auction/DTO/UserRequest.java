package com.AuctionApp.Auction.DTO;

import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
public class UserRequest {

    @NotNull(message = "Name is required")
    @NotBlank
    private String name;

    @NotNull(message = "Email is required")
    @Email(message = "Invalid email")
    @NotBlank
    private String email;

    @NotNull(message = "Passsword is required")
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String city;

    @NotNull(message = "Mobile no is required")
    @Pattern(regexp = "^\\d{10}$",message = "Invalid mobile number")
    @NotBlank
    private String mobileNo;


}
