package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.Component.Style;
import com.AuctionApp.Auction.DTO.PlayerDTO;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.entites.Auction;
import com.AuctionApp.Auction.entites.Player;
import com.AuctionApp.Auction.entites.User;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import com.AuctionApp.Auction.repositories.PlayerRepository;
import com.AuctionApp.Auction.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    public Player join(PlayerDTO newPlayer){

        //current user will be here after authentication
        User user = userRepository.findByUserId(15L);

        //The auction id we get from url of frontend
        Optional<Auction> auction = auctionRepository.findById(newPlayer.getAuctionId());


        if(auction.isPresent()){
        Player player = new Player(0,
                newPlayer.getPlayerName(),
                newPlayer.getMobileNo(),
                newPlayer.getPlayerAge(),
                newPlayer.getJersseyNumber(),
                newPlayer.getJersseyName(),
                newPlayer.getT_ShirtSize(),
                newPlayer.getTrouserSize(),
                newPlayer.getPlayerStyle(),
                1,
                newPlayer.getDetails(),
                auction.get());
            return playerRepository.save(player);
        }
        throw new CustomException("Invalid auction id", HttpStatus.BAD_REQUEST,"Provide valid auction details");
    }

    public List<Player> show(long auctionId){
       return playerRepository.findByAuction(auctionId);
    }


    public void edit(PlayerDTO playerRequest,long playerId){
        playerRepository.updatePlayerById(playerId,playerRequest.getPlayerAge(), playerRequest.getPlayerName(), playerRequest.getMobileNo(),playerRequest.getPlayerStyle(), playerRequest.getT_ShirtSize(), playerRequest.getJersseyName(), playerRequest.getJersseyNumber(), playerRequest.getTrouserSize(), playerRequest.getDetails());
    }


    public void delete(long playerId) {
        playerRepository.deletePlayerByPlayerId(playerId);
    }
}
