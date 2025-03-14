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

    private int pointsPerTeam;

    private int baseBid;

    private int bidIncreaseBy;

    private int teamSize;

    @ElementCollection
    @CollectionTable(name = "additionalIncrements", joinColumns = @JoinColumn(name = "auction_id"))
    private List<AdditinalIncrements> additionalIncrements = new ArrayList<>();


    @OneToMany(targetEntity = Category.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "AC_FK",referencedColumnName = "auctionId")
    @JsonIgnore
    private List<Category> categories = new ArrayList<>();

    private int maxPlayerPerTeam;

    private int minPlayerPerTeam;

    private int reserve;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "ap_fk" ,referencedColumnName = "auctionId")
    @JsonIgnore
    private List<Player> auctionPlayers;

    @OneToMany
    private List<Player> unsoldPlayers = new ArrayList<>();

    @OneToMany(targetEntity = Team.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "AT_fk",referencedColumnName = "auctionId")
    @JsonIgnore
    private List<Team> teams;

    private  int counter = 0;

    private boolean playerRegistration;

    private int noneCategoryPlayerRequired;

    public Auction(String auctionId, String auctionName, int season, Date auctionDate, String auctionTime, int pointsPerTeam, int baseBid, int bidIncreaseBy, int maxPlayerPerTeam, int minPlayerPerTeam,int reserve,boolean playerRegistration,int noneCategoryPlayerRequired,int teamSize) {
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
        this.playerRegistration = playerRegistration;
        this.noneCategoryPlayerRequired = noneCategoryPlayerRequired;
        this.teamSize = teamSize;
    }
}
