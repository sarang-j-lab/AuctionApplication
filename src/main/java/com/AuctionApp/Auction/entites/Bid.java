package com.AuctionApp.Auction.entites;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bid_tbl")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    private String playerId;


    private String bidderId;

    private BigInteger amount;

    private boolean status;

    public boolean getStatus(){
        return status;
    }
}
