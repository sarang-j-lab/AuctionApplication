package com.AuctionApp.Auction.entites;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.sql.Time;


@Entity
@Table(name = "auction_tbl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long auctionId;

//    private String auctionLogo;

    private String auctionName;

    private int season;

    @JsonFormat(pattern = "dd-mm-yyyy")
    private Date auctionDate;

    @JsonFormat(pattern = "HH:mm:ss")
    private Time auctionTime;

    private long pointsPerTeam;

    private long baseBid;

    private long bidIncreaseBy;

    private int maxPlayerPerTeam;

    private int minPlayerPerTeam;

    @ManyToOne
    @JoinColumn(name = "auction_owner",nullable = false)
    private User auctionCreateBy;

}
