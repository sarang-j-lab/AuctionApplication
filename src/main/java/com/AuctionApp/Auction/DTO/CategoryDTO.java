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
    private long maxPlayerPerTeam;

    @NotNull
    @PositiveOrZero
    private long minPlayerPerTeam;

    @NotNull
    @Positive
    private long baseBid;

    @NotNull
    @Positive
    private long increment;

}
