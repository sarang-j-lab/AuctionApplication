package com.AuctionApp.Auction.entites;

import com.AuctionApp.Auction.Component.Style;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "player_tbl",  uniqueConstraints = @UniqueConstraint(columnNames = "mobileNo"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    private String playerId;

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

    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "categoryId")
    private Category categoryId;

    public Player(String playerId, String playerName, String mobileNo, int playerAge, int jersseyNumber, String jersseyName, String tShirtSize, String trouserSize, Style playerStyle, int formNo) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.mobileNo = mobileNo;
        this.playerAge = playerAge;
        this.jersseyNumber = jersseyNumber;
        this.jersseyName = jersseyName;
        this.tShirtSize = tShirtSize;
        this.trouserSize = trouserSize;
        this.playerStyle = playerStyle;
        this.formNo = formNo;
    }
}

