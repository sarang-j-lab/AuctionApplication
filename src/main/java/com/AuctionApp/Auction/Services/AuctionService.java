package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.DTO.AuctionDTO;
import com.AuctionApp.Auction.advise.CustomException;
import com.AuctionApp.Auction.entites.Auction;
import com.AuctionApp.Auction.entites.User;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import com.AuctionApp.Auction.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    public Auction create(AuctionDTO auctionRequest){
        User user = userRepository.findByUserId(12);
        List<Long> auctionIds = new ArrayList<>();

        Auction auction = new Auction(0,auctionRequest.getAuctionName(),auctionRequest.getSeason(),auctionRequest.getAuctionDate(),
                auctionRequest.getAuctionTime(),auctionRequest.getPointsPerTeam(),auctionRequest.getBaseBid(),auctionRequest.getBidIncreaseBy(),
                auctionRequest.getMaxPlayerPerTeam(),auctionRequest.getMinPlayerPerTeam(),auctionRequest.getAuctionCreateBy());

        Auction newAuction = auctionRepository.save(auction);
        auctionIds.addAll(user.getAuctions());
        auctionIds.add(newAuction.getAuctionId());
        user.setAuctions(auctionIds);
        userRepository.save(user);

        return auction;
    }

    public List<Auction> getAll(long userId){
        User user = userRepository.findByUserId(userId);
        if(user == null){
            throw new CustomException("User not found", HttpStatus.NOT_FOUND,"User not fount");
        }
        List<Auction> auctions = auctionRepository.findByAuctionIdIn(user.getAuctions());
        return auctions;
    }

    public Auction getAuction(long auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            return auction.get();
        }
        throw new CustomException("Auction not found",HttpStatus.NOT_FOUND,"Auction with this auctionId is not present");
    }

    public void updateAuction(long auctionId,AuctionDTO auctionDTO) {
        auctionRepository.updateAuction(auctionId,auctionDTO.getAuctionName(), auctionDTO.getSeason(),
                auctionDTO.getPointsPerTeam(), auctionDTO.getBaseBid(), auctionDTO.getBidIncreaseBy(), auctionDTO.getMaxPlayerPerTeam(),
                auctionDTO.getMinPlayerPerTeam());
    }

    public void deleteAuction(long auctionId,long userId){
        User user = userRepository.findByUserId(userId);
        user.getAuctions().remove(auctionId);
        auctionRepository.deleteById(auctionId);
    }


}
