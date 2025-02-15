package com.AuctionApp.Auction.DTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @FutureOrPresent(message = "Auction date must date in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date auctionDate;

    @NotNull
    @Positive(message = "Season should be greather than 0")
    private int season;


    @NotNull
    @NotBlank
    private String auctionTime;

    @NotNull
    @Positive(message = "Points per team must be grather than 0")
    private int pointsPerTeam;


    @NotNull
    @PositiveOrZero(message = "Base bid should be positive")
    private int baseBid;


    @NotNull
    @Positive(message = "bid incrementment should be positive")
    private int bidIncreaseBy;


    @NotNull
    @PositiveOrZero(message = "Max player should be positive")
    private int maxPlayerPerTeam;


    @NotNull
    @PositiveOrZero(message = "Min player should be positive")
    private int minPlayerPerTeam;

    @NotNull
    private boolean playerRegistration;



}
