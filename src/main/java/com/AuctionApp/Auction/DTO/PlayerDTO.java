package com.AuctionApp.Auction.DTO;

import com.AuctionApp.Auction.Component.Style;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {


//    private String playerPic;

    @NotNull
    @NotBlank
    private String playerName;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\d{10}$",message = "Invalid mobile number")
    private String mobileNo;

    @NotNull
    @Positive
    private int playerAge;

    @NotNull
    @Positive
    private int jersseyNumber;


    @NotNull
    @NotBlank
    private String  jersseyName;

    @NotNull
    @NotBlank
    private String t_ShirtSize;

    @NotNull
    @NotBlank
    private String trouserSize;

    @NotNull
    private Style playerStyle;

    @NotNull
    @NotBlank
    private String details;

    @NotNull
    @NotBlank
    private long auctionId;

}
