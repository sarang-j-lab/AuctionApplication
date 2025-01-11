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
@AllArgsConstructor()
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;


    private String name;


    private String email;


    private String password;

    private String city;

    @Column(unique = true)
    private String mobileNo;


    @OneToMany(targetEntity = Auction.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "ua_fk",referencedColumnName = "userId")
    private List<Auction> auctions;
}
