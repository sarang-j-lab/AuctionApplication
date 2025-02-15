package com.AuctionApp.Auction.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CategoryRequirements {


    private String category;

    private int bought;

    private int playerRequired;

    private int maxPlayerRequired;

    private int reserve;


}
