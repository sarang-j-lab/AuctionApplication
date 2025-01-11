package com.AuctionApp.Auction.DTO;

import com.AuctionApp.Auction.entites.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;


@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class AuctionDTO {

//    private String auctionLogo;

    @NotBlank
    @NotNull
    private String auctionName;



    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date auctionDate;

    @NotNull
    @Positive
    private int season;


    @NotNull
    @NotBlank
    private String auctionTime;

    @NotNull
    @Positive
    private long pointsPerTeam;


    @NotNull
    @Positive
    private long baseBid;


    @NotNull
    @Positive
    private long bidIncreaseBy;


    @NotNull
    @Positive
    private int maxPlayerPerTeam;


    @NotNull
    @Positive
    private int minPlayerPerTeam;




}
