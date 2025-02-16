package com.AuctionApp.Auction.entites;


import com.AuctionApp.Auction.Component.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String userId;


    private String name;


    private String email;


    private String password;

    private String city;

    @Column(unique = true)
    private String mobileNo;

    private Role role;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_as_player_in_auction",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "auctionId")
    )
    @JsonIgnore
    private List<Auction> userAsPlayerInAuction ;


    @OneToMany(targetEntity = Auction.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "ua_fk",referencedColumnName = "userId")
    @JsonIgnore
    private List<Auction> auctions;

    public User(String userId, String name, String email, String password, String city, String mobileNo,Role role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.city = city;
        this.mobileNo = mobileNo;
        this.role = role;
    }
}
