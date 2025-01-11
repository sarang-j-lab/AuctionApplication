package com.AuctionApp.Auction.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "team_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

//    private String teamLogo;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long teamId;

    private String teamName;


    private String shortcutKey;

    private String shortName;


    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "TEAM_PLAYER_TABLE",
        joinColumns = {
            @JoinColumn(name = "team_id",referencedColumnName = "teamId")
        },
            inverseJoinColumns = {
                    @JoinColumn(name = "player_id", referencedColumnName = "playerId")
            })
    private List<Player> teamPlayers;

    public Team(long teamId, String teamName, String shortcutKey,String shortName) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.shortcutKey = shortcutKey;
        this.shortName = shortName;
    }
}
