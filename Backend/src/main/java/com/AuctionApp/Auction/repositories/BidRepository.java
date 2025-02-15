package com.AuctionApp.Auction.repositories;


import com.AuctionApp.Auction.entites.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid,String>{

}
