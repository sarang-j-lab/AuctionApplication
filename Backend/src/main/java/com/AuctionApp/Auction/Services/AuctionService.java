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

import com.AuctionApp.Auction.util.Generate;
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
            if(auctionRequest.getBaseBid() * auctionRequest.getMinPlayerPerTeam() > auctionRequest.getPointsPerTeam()){
//                auctionRequest.setBaseBid(auctionRequest.getPointsPerTeam() / (auctionRequest.getMaxPlayerPerTeam() * 2));
                throw new CustomException("The Team's points are insufficient to meet the minimum player requirement for this base bid.",HttpStatus.BAD_REQUEST,"Please provide valid details");
            }
                Auction auction = new Auction(Generate.generateId(),auctionRequest.getAuctionName(),auctionRequest.getSeason(),auctionRequest.getAuctionDate(),
                    auctionRequest.getAuctionTime(),auctionRequest.getPointsPerTeam(),auctionRequest.getBaseBid(),auctionRequest.getBidIncreaseBy(),
                    auctionRequest.getMaxPlayerPerTeam(),auctionRequest.getMinPlayerPerTeam(), (auctionRequest.getMinPlayerPerTeam()) * auctionRequest.getBaseBid(),auctionRequest.isPlayerRegistration(),auctionRequest.getMinPlayerPerTeam(),2);

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


        auctionRepository.updateAuction(auctionId,
                auctionDTO.getAuctionName(),
                auctionDTO.getSeason(),
                auctionDTO.getAuctionTime(),
                auctionDTO.getAuctionDate(),
                auctionDTO.getBidIncreaseBy(),
                auctionDTO.getMaxPlayerPerTeam(),
                auctionDTO.isPlayerRegistration()
        );


        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            return auction.get();
        }
        throw new CustomException("Auction not found",HttpStatus.BAD_REQUEST,"Auction not found");
    }


    public void deleteAuction(String auctionId){
        auctionRepository.deleteUserAsAplayer(auctionId);
        auctionRepository.deleteById(auctionId);
    }


    public Auction addIncrements(AdditinalIncrements newIncrements, String auctionId) {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            newIncrements.setId(Generate.generateId());
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

    public Map<String,Object> getDetailedAuction(String auctionId) {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            Map<String, Object> detailedAuction = new HashMap<>();
            detailedAuction.put("auction",auction.get());
            detailedAuction.put("auctionCategories",auction.get().getCategories());
            detailedAuction.put("auctionTeams",auction.get().getTeams());
            detailedAuction.put("auctionPlayers",auction.get().getAuctionPlayers());
            detailedAuction.put("unsoldPlayers",auction.get().getUnsoldPlayers());
            return detailedAuction;
        }
        throw new CustomException("Auction Not Found!",HttpStatus.BAD_REQUEST,"Auction Not Found");
    }
}
