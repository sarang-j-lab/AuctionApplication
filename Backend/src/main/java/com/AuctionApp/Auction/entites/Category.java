package com.AuctionApp.Auction.entites;

import com.AuctionApp.Auction.Component.CategoryAdditionalIncrements;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    private String categoryId;


    private String categoryName;

    private int maxPlayerPerTeam;

    private int minPlayerPerTeam;

    private int baseBid;

    private int increment;

    @ElementCollection
    @CollectionTable(name = "category_increments",joinColumns = @JoinColumn(name = "category_id"))
    private List<CategoryAdditionalIncrements> categoryAdditionalIncrements = new ArrayList<>();

    @OneToMany(mappedBy = "categoryId")
    @JsonIgnore
    private List<Player> players = new ArrayList<>();

    public Category( String categoryId,String categoryName, int maxPlayerPerTeam, int minPlayerPerTeam, int baseBid, int increment) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.maxPlayerPerTeam = maxPlayerPerTeam;
        this.minPlayerPerTeam = minPlayerPerTeam;
        this.baseBid = baseBid;
        this.increment = increment;
    }
}
