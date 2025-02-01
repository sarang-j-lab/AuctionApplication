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


    private String shortcutKey;

    private String shortName;


    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "tp_fk",referencedColumnName = "teamId")
    @JsonIgnore
    private List<Player> teamPlayers;

    public Team(String teamId, String teamName, String shortcutKey,String shortName) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.shortcutKey = shortcutKey;
        this.shortName = shortName;
    }
}
