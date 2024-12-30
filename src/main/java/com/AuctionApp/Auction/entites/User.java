package com.AuctionApp.Auction.entites;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_tbl")
@Data
@ToString
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;


    private String name;


    private String email;


    private String password;

    private String city;

    private String mobileNo;

    @ElementCollection
    @CollectionTable(name = "auctions", joinColumns = @JoinColumn(name = "auction_owner"))
    @Column(name = "auction_id")
    private List<Long> auctions;
}
