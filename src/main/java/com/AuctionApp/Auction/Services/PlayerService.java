package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.Component.Style;
import com.AuctionApp.Auction.DTO.PlayerDTO;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.entites.*;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import com.AuctionApp.Auction.repositories.CategoryRepository;
import com.AuctionApp.Auction.repositories.PlayerRepository;
import com.AuctionApp.Auction.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private void addPlayerCategory(UUID categoryId, Player player){
        Optional<Category> category = categoryRepository.findById(categoryId);
        System.out.println(category.get().getCategoryId());
        System.out.println(player.getPlayerName());
        if(category.isPresent()){
            category.get().getPlayers().add(player);
            categoryRepository.save(category.get());
        }
    }


    public Player join(PlayerDTO newPlayer,long auctionId){

        //current user will be here after authentication
        User user = userRepository.findByUserId(15L);

        //The auction id we get from url of frontend
        Optional<Auction> auction = auctionRepository.findById(auctionId);


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
                newPlayer.getDetails()
        );
            Player savedPlayer = playerRepository.save(player);
            if(newPlayer.getCategoryId() != null){
                addPlayerCategory(newPlayer.getCategoryId(), savedPlayer);
            }
            auction.get().getAuctionPlayers().add(savedPlayer);
            auctionRepository.save(auction.get());
            return savedPlayer;
        }
        throw new CustomException("Invalid auction id", HttpStatus.BAD_REQUEST,"Provide valid auction details");
    }

    public List<Player> show(long auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            return auction.get().getAuctionPlayers();
        }
        throw new CustomException("Invalid auctionID",HttpStatus.BAD_REQUEST,"Provide valid auctionID");
    }


    public void edit(PlayerDTO playerRequest,long playerId){
        playerRepository.updatePlayerById(playerId,
                playerRequest.getPlayerAge(),
                playerRequest.getPlayerName(),
                playerRequest.getMobileNo(),
                playerRequest.getPlayerStyle(),
                playerRequest.getT_ShirtSize(),
                playerRequest.getJersseyName(),
                playerRequest.getJersseyNumber(),
                playerRequest.getTrouserSize(),
                playerRequest.getDetails()
        );
        if(playerRequest.getCategoryId() != null){
            Optional<Player> player = playerRepository.findById(playerId);
            System.out.println(player.get().getPlayerName());
            player.ifPresent(value -> addPlayerCategory(playerRequest.getCategoryId(), value));
        }
    }

    public void delete(long playerId) {
        playerRepository.deletePlayerFromAuction(playerId);
        playerRepository.deleteById(playerId);
    }
}
