package com.AuctionApp.Auction.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class BidDTO {

    private String player;

    private String team;

    private int amount;

    private String category;
}
