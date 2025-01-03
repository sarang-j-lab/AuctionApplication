package com.AuctionApp.Auction.entites;

import com.AuctionApp.Auction.Component.Style;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "player_tbl",  uniqueConstraints = @UniqueConstraint(columnNames = "mobileNo"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long playerId;

//    private String playerPic;

    private String playerName;

    @Column(unique = true)
    private String mobileNo;

    private int playerAge;

    private int jersseyNumber;

    private String  jersseyName;

    private String tShirtSize;

    private String trouserSize;

    private Style playerStyle;

    private int formNo;

    private String details;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auctionId")
    private Auction auction;

//    private String teamId;
}

