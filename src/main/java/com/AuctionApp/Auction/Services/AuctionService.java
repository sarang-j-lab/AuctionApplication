package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.DTO.AuctionDTO;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.entites.Auction;
import com.AuctionApp.Auction.entites.Player;
import com.AuctionApp.Auction.entites.User;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import com.AuctionApp.Auction.repositories.PlayerRepository;
import com.AuctionApp.Auction.repositories.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private PlayerRepository playerRepository;


    private void timeValidation(String time){
        String timeRegex = "^(0[1-9]|1[0-2]):[0-5]\\d\\s?(AM|PM)$";
        Pattern pattern = Pattern.compile(timeRegex);

        Matcher matcher = pattern.matcher(time);
         if(!matcher.matches()){
             throw new CustomException("Provide valid time patter : hh:mm AM/PM",HttpStatus.BAD_REQUEST,"Provided time pattern did'nt matched");
         }
    }



    @Transactional
    public Auction create(AuctionDTO auctionRequest){
        User user = userRepository.findByUserId(15);
        List<Long> auctionIds = new ArrayList<>();

        this.timeValidation(auctionRequest.getAuctionTime());


        Auction auction = new Auction(0,auctionRequest.getAuctionName(),auctionRequest.getSeason(),auctionRequest.getAuctionDate(),
                auctionRequest.getAuctionTime(),auctionRequest.getPointsPerTeam(),auctionRequest.getBaseBid(),auctionRequest.getBidIncreaseBy(),
                auctionRequest.getMaxPlayerPerTeam(),auctionRequest.getMinPlayerPerTeam(),user,new ArrayList<>());

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
        this.timeValidation(auctionDTO.getAuctionTime());
        auctionRepository.updateAuction(auctionId,
                auctionDTO.getAuctionName(),
                auctionDTO.getSeason(),
                auctionDTO.getAuctionTime(),
                auctionDTO.getAuctionDate(),
                auctionDTO.getPointsPerTeam(),
                auctionDTO.getBaseBid(),
                auctionDTO.getBidIncreaseBy(),
                auctionDTO.getMaxPlayerPerTeam(),
                auctionDTO.getMinPlayerPerTeam());
    }

    @Transactional
    public void deleteAuction(long auctionId){
        List<Player> players = playerRepository.findByAuction(auctionId);
        playerRepository.deleteAllInBatch(players);
        auctionRepository.deleteAuctionfromUser(auctionId);
        auctionRepository.deleteByAuctionId(auctionId);
    }


}
