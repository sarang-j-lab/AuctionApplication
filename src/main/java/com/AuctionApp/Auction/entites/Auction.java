package com.AuctionApp.Auction.entites;

import com.AuctionApp.Auction.Component.AdditinalIncrements;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "auction_tbl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auction {

    @Id
    private String auctionId;

//    private String auctionLogo;

    private String auctionName;

    private int season;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date auctionDate;


    private String auctionTime;

    private long pointsPerTeam;

    private long baseBid;

    private long bidIncreaseBy;

    @ElementCollection
    @CollectionTable(name = "additionalIncrements", joinColumns = @JoinColumn(name = "auction_id"))
    private List<AdditinalIncrements> additionalIncrements = new ArrayList<>();


    @OneToMany(targetEntity = Category.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "AC_FK",referencedColumnName = "auctionId")
    private List<Category> categories = new ArrayList<>();

    private long maxPlayerPerTeam;

    private long minPlayerPerTeam;

    private long reserve;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "AUCTION_PLAYER_TBL",
            joinColumns = {
                    @JoinColumn(name = "auctionId",referencedColumnName = "auctionId")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "player_id", referencedColumnName = "playerId")
            })
    @JsonIgnore
    private List<Player> auctionPlayers;

    @OneToMany(targetEntity = Team.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "AT_fk",referencedColumnName = "auctionId")
    @JsonIgnore
    private List<Team> teams;

    private ArrayList<String> shortcutKeys = new ArrayList<>(
            Arrays.asList(
                    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
                    "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
            )
    );


    public Auction(String auctionId, String auctionName, int season, Date auctionDate, String auctionTime, long pointsPerTeam, long baseBid, long bidIncreaseBy, int maxPlayerPerTeam, int minPlayerPerTeam,long reserve) {
        this.auctionId = auctionId;
        this.auctionName = auctionName;
        this.season = season;
        this.auctionDate = auctionDate;
        this.auctionTime = auctionTime;
        this.pointsPerTeam = pointsPerTeam;
        this.baseBid = baseBid;
        this.bidIncreaseBy = bidIncreaseBy;
        this.maxPlayerPerTeam = maxPlayerPerTeam;
        this.minPlayerPerTeam = minPlayerPerTeam;
        this.reserve = reserve;
    }
}
