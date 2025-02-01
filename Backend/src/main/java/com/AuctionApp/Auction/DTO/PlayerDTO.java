package com.AuctionApp.Auction.DTO;

import com.AuctionApp.Auction.Component.Style;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class PlayerDTO {

    private String playerId;

//    private String playerPic;

    @NotNull
    @NotBlank
    private String playerName;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\d{10}$",message = "Invalid mobile number")
    private String mobileNo;

    @NotNull
    @Positive(message = "Player age should be positive")
    private int playerAge;

    @NotNull
    @Positive(message = "Jerssey number should be positive")
    private int jersseyNumber;


    @NotNull
    @NotBlank
    private String jersseyName;


    private String tShirtSize;

    @NotNull
    @NotBlank
    private String trouserSize;

    @NotNull
    private Style playerStyle;


    private String categoryId;


    public PlayerDTO(String playerId,String playerName, String mobileNo, int playerAge, int jersseyNumber, String jersseyName, String tShirtSize, String trouserSize, Style playerStyle, String categoryId) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.mobileNo = mobileNo;
        this.playerAge = playerAge;
        this.jersseyNumber = jersseyNumber;
        this.jersseyName = jersseyName;
        this.tShirtSize = tShirtSize;
        this.trouserSize = trouserSize;
        this.playerStyle = playerStyle;
        this.categoryId = categoryId;
    }
}
