package com.AuctionApp.Auction.entites;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.sql.Time;
import java.util.List;


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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date auctionDate;


    private String auctionTime;

    private long pointsPerTeam;

    private long baseBid;

    private long bidIncreaseBy;

    private int maxPlayerPerTeam;

    private int minPlayerPerTeam;

    @ManyToOne
    @JoinColumn(name = "auction_owner",nullable = true)
    private User auctionCreateBy;

    @OneToMany(mappedBy = "playerId")
    private List<Player> players;

}
