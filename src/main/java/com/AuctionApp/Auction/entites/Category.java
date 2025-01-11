package com.AuctionApp.Auction.entites;

import com.AuctionApp.Auction.Component.CategoryAdditionalIncrements;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "category_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    private UUID categoryId;

    private String categoryName;

    private long maxPlayerPerTeam;

    private long minPlayerPerTeam;

    private long baseBid;

    private long increment;

    @ElementCollection
    @CollectionTable(name = "category_increments",joinColumns = @JoinColumn(name = "category_id"))
    private List<CategoryAdditionalIncrements> categoryAdditionalIncrements = new ArrayList<>();

    @OneToMany(targetEntity = Player.class)
    @JoinColumn(name = "CP_FK",referencedColumnName = "categoryId")
    private List<Player> players;

    public Category( UUID categoryId,String categoryName, long maxPlayerPerTeam, long minPlayerPerTeam, long baseBid, long increment) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.maxPlayerPerTeam = maxPlayerPerTeam;
        this.minPlayerPerTeam = minPlayerPerTeam;
        this.baseBid = baseBid;
        this.increment = increment;
    }
}
