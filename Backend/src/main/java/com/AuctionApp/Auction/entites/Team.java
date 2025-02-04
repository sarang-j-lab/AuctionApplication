package com.AuctionApp.Auction.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "team_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

//    private String teamLogo;
    @Id
    private String teamId;

    private String teamName;


    private String shortName;

    private int totalPoints;

    private int reserve;

    private  int maxBid;


    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "tp_fk",referencedColumnName = "teamId")
    @JsonIgnore
    private List<Player> teamPlayers;

    public Team(String teamId, String teamName,  String shortName, int totalPoints, int reserve, int maxBid) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.shortName = shortName;
        this.totalPoints = totalPoints;
        this.reserve = reserve;
        this.maxBid = maxBid;
    }
}
