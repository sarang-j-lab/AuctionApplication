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

import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    public Auction create(AuctionDTO auctionRequest,long userId){
        Optional<User> user = userRepository.findById(userId);


        if(user.isPresent()){
            this.timeValidation(auctionRequest.getAuctionTime());

            //if baseBid * maxPlayerPerTeam is greather than pointsPerTeam then we have change with point / maxPlayerPerTeam for equal distribution
            if(auctionRequest.getBaseBid() * auctionRequest.getMaxPlayerPerTeam() > auctionRequest.getPointsPerTeam()){
                auctionRequest.setBaseBid(auctionRequest.getPointsPerTeam() / auctionRequest.getMaxPlayerPerTeam());
            }

            Auction auction = new Auction(0,auctionRequest.getAuctionName(),auctionRequest.getSeason(),auctionRequest.getAuctionDate(),
                    auctionRequest.getAuctionTime(),auctionRequest.getPointsPerTeam(),auctionRequest.getBaseBid(),auctionRequest.getBidIncreaseBy(),
                    auctionRequest.getMaxPlayerPerTeam(),auctionRequest.getMinPlayerPerTeam(), (auctionRequest.getMinPlayerPerTeam() - 1) * auctionRequest.getBaseBid());

            Auction newAuction = auctionRepository.save(auction);
            user.get().getAuctions().add(newAuction);
            userRepository.save(user.get());

            return newAuction;
        }
        throw new CustomException("User not found",HttpStatus.BAD_REQUEST,"User Not found with this id");

    }

    public List<Auction> getAll(long userId){
        User user = userRepository.findByUserId(userId);
        if(user == null){
            throw new CustomException("User not found", HttpStatus.NOT_FOUND,"User not fount");
        }
        return user.getAuctions();
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

        //if baseBid * maxPlayerPerTeam is greather than pointsPerTeam then we have change with point / maxPlayerPerTeam
        if((auctionDTO.getBaseBid() * auctionDTO.getMaxPlayerPerTeam()) > auctionDTO.getPointsPerTeam()){
            auctionDTO.setBaseBid(auctionDTO.getPointsPerTeam() / auctionDTO.getMaxPlayerPerTeam());
        }

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


    public void deleteAuction(long auctionId){
        auctionRepository.deleteById(auctionId);
    }


    public String addIncrements(AdditinalIncrements newIncrements, long auctionId) {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            newIncrements.setId(UUID.randomUUID());
            auction.get().getAdditionalIncrements().add(newIncrements);
            auctionRepository.save(auction.get());
            return "Increment added successfully";
        }else{
            throw new CustomException("Auction not found",HttpStatus.BAD_REQUEST,"Auction not found with this id");
        }
    }

    public String deleteIncrement(long auctionId,UUID incrementId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            for(AdditinalIncrements increments : auction.get().getAdditionalIncrements()){
                if(increments.getId().equals(incrementId)){
                    auction.get().getAdditionalIncrements().remove(increments);
                    auctionRepository.save(auction.get());
                    return "Increment deleted successfully";
                }
            }
                return "Increment deleted successfully";
        } else{
            throw new CustomException("Auction not found",HttpStatus.BAD_REQUEST,"Auction not found with this id");
        }
    }
}
