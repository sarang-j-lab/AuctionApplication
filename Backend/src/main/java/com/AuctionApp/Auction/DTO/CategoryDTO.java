package com.AuctionApp.Auction.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    @NotNull
    @NotBlank
    private String categoryName;

    @NotNull
    @Positive
    private int maxPlayerPerTeam;

    @NotNull
    @PositiveOrZero
    private int minPlayerPerTeam;

    @NotNull
    @Positive
    private int baseBid;

    @NotNull
    @Positive
    private int increment;

}
