package com.AuctionApp.Auction.DTO;

import com.AuctionApp.Auction.entites.Auction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
    @NotBlank
    @NotNull
    private String teamName;

    @NotNull
    @NotBlank
    private String shortName;

    @NotNull
    private String shortcutKey;

    @NotNull
    private long auction;


}
