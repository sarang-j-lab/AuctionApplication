package com.AuctionApp.Auction.entites;

import com.AuctionApp.Auction.Component.Status;
import com.AuctionApp.Auction.Component.Style;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "player_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    private String playerId;


    private String playerName;


    private String mobileNo;

    private int playerAge;

    private int jersseyNumber;

    private String  jersseyName;

    private String tShirtSize;

    private String trouserSize;

    private Style playerStyle;

    private int formNo;

    private String isUser;

    private Status status;

    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "categoryId")
    private Category categoryId;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    private Bid bid;

    public Player(String playerId, String playerName, String mobileNo, int playerAge, int jersseyNumber, String jersseyName, String tShirtSize, String trouserSize, Style playerStyle, Status status ,int formNo) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.mobileNo = mobileNo;
        this.playerAge = playerAge;
        this.jersseyNumber = jersseyNumber;
        this.jersseyName = jersseyName;
        this.tShirtSize = tShirtSize;
        this.trouserSize = trouserSize;
        this.playerStyle = playerStyle;
        this.status = status;
        this.formNo = formNo;
    }
}

