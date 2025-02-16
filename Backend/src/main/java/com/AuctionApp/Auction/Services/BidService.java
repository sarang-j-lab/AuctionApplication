package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.entites.Bid;
import com.AuctionApp.Auction.repositories.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Service;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    public Bid saveBid(Bid bid){
        return bidRepository.save(bid);
    }

    public void deleteBid(String bidId){
        bidRepository.deleteById(bidId);
    }


}
