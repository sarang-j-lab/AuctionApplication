package com.AuctionApp.Auction.entites;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bid_tbl")
@ToString
public class Bid {

    @Id
    private String id;


    private String player;

    private String team;

    private int amount;

    private String category;

}
