package com.AuctionApp.Auction.Component;


import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CategoryAdditionalIncrements {

    private UUID id;

    @NotNull
    private long increment;

    @NotNull
    private long after;


}