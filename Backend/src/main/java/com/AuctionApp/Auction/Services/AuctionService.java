package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.Component.AdditinalIncrements;
import com.AuctionApp.Auction.DTO.AuctionDTO;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.entites.Auction;
import com.AuctionApp.Auction.entites.Player;
import com.AuctionApp.Auction.entites.User;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import com.AuctionApp.Auction.repositories.PlayerRepository;
import com.AuctionApp.Auction.repositories.UserRepository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuctionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private PlayerRepository playerRepository;

    private final Random random = new Random();
    private String generateId() {
        // Generate a random 7-digit number for the first part
        long firstPart = 1000000 + random.nextInt(9000000); // Ensures it's always 7 digits
        long secondPart = 1000 + random.nextInt(9000); // Ensures it's always 7 digits

        return firstPart+"-"+secondPart;
    }

    private void timeValidation(String time){
        String timeRegex = "^([01]\\d|2[0-3]):[0-5]\\d$";
        Pattern pattern = Pattern.compile(timeRegex);

        Matcher matcher = pattern.matcher(time);
         if(!matcher.matches()){
             throw new CustomException("Provide valid time patter : hh:mm",HttpStatus.BAD_REQUEST,"Provided time pattern did'nt matched");
         }
    }



    @Transactional
    public Auction create(AuctionDTO auctionRequest,String userId){
        Optional<User> user = userRepository.findById(userId);

        if(auctionRequest.getMinPlayerPerTeam() > auctionRequest.getMaxPlayerPerTeam()){
            throw new ValidationException("Min player per team should be less than max player per team");
        }

        if(user.isPresent()){
            this.timeValidation(auctionRequest.getAuctionTime());

            //if baseBid * maxPlayerPerTeam is greather than pointsPerTeam then we have change with point / maxPlayerPerTeam for equal distribution
            if(auctionRequest.getBaseBid() * auctionRequest.getMaxPlayerPerTeam() > auctionRequest.getPointsPerTeam()){
                auctionRequest.setBaseBid(auctionRequest.getPointsPerTeam() / (auctionRequest.getMaxPlayerPerTeam() - 1));
            }
                Auction auction = new Auction(generateId(),auctionRequest.getAuctionName(),auctionRequest.getSeason(),auctionRequest.getAuctionDate(),
                    auctionRequest.getAuctionTime(),auctionRequest.getPointsPerTeam(),auctionRequest.getBaseBid(),auctionRequest.getBidIncreaseBy(),
                    auctionRequest.getMaxPlayerPerTeam(),auctionRequest.getMinPlayerPerTeam(), (auctionRequest.getMaxPlayerPerTeam() - 1) * auctionRequest.getBaseBid());

            Auction newAuction = auctionRepository.save(auction);
            user.get().getAuctions().add(newAuction);
            userRepository.save(user.get());

            return newAuction;
        }
        throw new CustomException("User not found",HttpStatus.BAD_REQUEST,"User Not found with this id");

    }

    public List<Auction> getAll(String userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return user.get().getAuctions();
        }
            throw new CustomException("User not found", HttpStatus.NOT_FOUND,"User not fount");
    }

    public Auction getAuction(String auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            return auction.get();
        }
        throw new CustomException("Auction not found",HttpStatus.NOT_FOUND,"Auction with this auctionId is not present");
    }

    public Auction updateAuction(String auctionId,AuctionDTO auctionDTO) {
        this.timeValidation(auctionDTO.getAuctionTime());

        //if baseBid * maxPlayerPerTeam is greather than pointsPerTeam then we have change with point / maxPlayerPerTeam
        if((auctionDTO.getBaseBid() * auctionDTO.getMaxPlayerPerTeam()) > auctionDTO.getPointsPerTeam()){
            auctionDTO.setBaseBid(auctionDTO.getPointsPerTeam() / (auctionDTO.getMaxPlayerPerTeam() - 1));
        }

        auctionRepository.updateAuction(auctionId,
                auctionDTO.getAuctionName(),
                auctionDTO.getSeason(),
                auctionDTO.getAuctionTime(),
                auctionDTO.getAuctionDate(),
                auctionDTO.getBidIncreaseBy(),
                auctionDTO.getMinPlayerPerTeam());

        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            return auction.get();
        }
        throw new CustomException("Auction not found",HttpStatus.BAD_REQUEST,"Auction not found");
    }


    public void deleteAuction(String auctionId){
        auctionRepository.deleteById(auctionId);
    }


    public Auction addIncrements(AdditinalIncrements newIncrements, String auctionId) {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            newIncrements.setId(generateId());
            auction.get().getAdditionalIncrements().add(newIncrements);
            return auctionRepository.save(auction.get());
        }else{
            throw new CustomException("Auction not found",HttpStatus.BAD_REQUEST,"Auction not found with this id");
        }
    }

    public Auction deleteIncrement(String auctionId,String incrementId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            for(AdditinalIncrements increments : auction.get().getAdditionalIncrements()){
                if(increments.getId().equals(incrementId)){
                    auction.get().getAdditionalIncrements().remove(increments);
                    auctionRepository.save(auction.get());
                    return auction.get();
                }
            }
        } else{
            throw new CustomException("Auction not found",HttpStatus.BAD_REQUEST,"Auction not found with this id");
        }
        return auction.get();

    }

    public List<AdditinalIncrements> getIncrements(String auctionId) {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            return auction.get().getAdditionalIncrements();
        }
        throw new CustomException("Auction Not found",HttpStatus.BAD_REQUEST,"Auction not found with this id");
    }
}
