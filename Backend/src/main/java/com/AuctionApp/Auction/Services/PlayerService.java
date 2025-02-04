package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.DTO.PlayerDTO;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.entites.*;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import com.AuctionApp.Auction.repositories.CategoryRepository;
import com.AuctionApp.Auction.repositories.PlayerRepository;
import com.AuctionApp.Auction.repositories.UserRepository;
import com.AuctionApp.Auction.util.Generate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    private final Random random = new Random();
    private String generateId() {
        // Generate a random 7-digit number for the first part
        long firstPart = 1000000 + random.nextInt(9000000); // Ensures it's always 7 digits
        long secondPart = 1000 + random.nextInt(9000); // Ensures it's always 7 digits

        return firstPart+"-"+secondPart;
    }

    private Category addPlayerCategory(String categoryId, Player player){
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isPresent()){
            category.get().getPlayers().add(player);
            return categoryRepository.save(category.get());
        }
        throw new CustomException("Category not found",HttpStatus.BAD_REQUEST,"Category not found with this id");
    }

    @Transactional
    public Player add(PlayerDTO newPlayer,String auctionId){

        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            auction.get().setCounter(auction.get().getCounter() + 1);

        Player player = new Player(Generate.generateId(),
                newPlayer.getPlayerName(),
                newPlayer.getMobileNo(),
                newPlayer.getPlayerAge(),
                newPlayer.getJersseyNumber(),
                newPlayer.getJersseyName(),
                newPlayer.getTShirtSize(),
                newPlayer.getTrouserSize(),
                newPlayer.getPlayerStyle(),
                false,
                auction.get().getCounter()
        );
        player.setIsUser(null);
            Player savedPlayer = playerRepository.save(player);
            if(newPlayer.getCategoryId() != null){
               Category category = addPlayerCategory(newPlayer.getCategoryId(), player);
               savedPlayer.setCategoryId(category);
               playerRepository.save(savedPlayer);
            }
            auction.get().getAuctionPlayers().add(savedPlayer);
            auctionRepository.save(auction.get());
            return player;
        }
        throw new CustomException("Invalid auction id", HttpStatus.BAD_REQUEST,"Provide valid auction details");
    }


    public Player join(PlayerDTO userAsPlayerReq, String auctionId,String userId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        Optional<User> user = userRepository.findById(userId);
        if(auction.isPresent() && user.isPresent()){
            if(user.get().getUserAsPlayerInAuction().contains(auction.get())){
                throw new CustomException("You have already joined this auction",HttpStatus.BAD_REQUEST,"Cannot join again");
            }
            auction.get().setCounter(auction.get().getCounter() + 1);
            Player player = new Player(Generate.generateId(),
                    userAsPlayerReq.getPlayerName(),
                    userAsPlayerReq.getMobileNo(),
                    userAsPlayerReq.getPlayerAge(),
                    userAsPlayerReq.getJersseyNumber(),
                    userAsPlayerReq.getJersseyName(),
                    userAsPlayerReq.getTShirtSize(),
                    userAsPlayerReq.getTrouserSize(),
                    userAsPlayerReq.getPlayerStyle(),
                    false,
                    auction.get().getCounter()
            );
            player.setIsUser(userId);
            Player savedPlayer = playerRepository.save(player);
            if(userAsPlayerReq.getCategoryId() != null){
                Category category = addPlayerCategory(userAsPlayerReq.getCategoryId(), player);
                savedPlayer.setCategoryId(category);
                playerRepository.save(savedPlayer);
            }
            auction.get().getAuctionPlayers().add(savedPlayer);
            user.get().getUserAsPlayerInAuction().add(auction.get());
            auctionRepository.save(auction.get());
            userRepository.save(user.get());
            return player;
        }
        throw new CustomException("Something went wrong please try again!",HttpStatus.BAD_REQUEST,"provide valid details");
    }




    public List<Player> show(String auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            return auction.get().getAuctionPlayers();
        }
        throw new CustomException("Invalid auctionID",HttpStatus.BAD_REQUEST,"Provide valid auctionID");
    }


    public void edit(PlayerDTO playerRequest,String playerId){
        Optional<Player> player = playerRepository.findById(playerId);
        if(player.isPresent()){
            player.get().setPlayerAge(playerRequest.getPlayerAge());
            player.get().setPlayerName(playerRequest.getPlayerName());
            player.get().setMobileNo(playerRequest.getMobileNo());
            player.get().setPlayerStyle(playerRequest.getPlayerStyle());
            player.get().setTShirtSize(playerRequest.getTShirtSize());
            player.get().setTrouserSize(playerRequest.getTrouserSize());
            player.get().setJersseyName(playerRequest.getJersseyName());
            player.get().setJersseyNumber(playerRequest.getJersseyNumber());
            if(playerRequest.getCategoryId() != null){
                Category category = addPlayerCategory(playerRequest.getCategoryId(),player.get());
                player.get().setCategoryId(category);
                playerRepository.save(player.get());
            }else{
                player.get().setCategoryId(null);
                playerRepository.save(player.get());
            }
        }
        else{
            throw new CustomException("Player not found",HttpStatus.BAD_REQUEST,"Player not found");
        }
    }


    public String deletePlayer(String playerId, String auctionId){
        Optional<Player> player = playerRepository.findById(playerId);
        if(player.isPresent()){
            if(player.get().getIsUser() != null){
                Optional<User> user = userRepository.findById(player.get().getIsUser());
                Optional<Auction> auction = auctionRepository.findById(auctionId);
                if(user.isPresent() && auction.isPresent()){
                    user.get().getUserAsPlayerInAuction().remove(auction.get());
                    playerRepository.deleteById(playerId);
                    return "Player deleted successfully";
                }else{
                    throw new RuntimeException("Something went wrong!Please try again.");
                }
            }else{
                playerRepository.deleteById(playerId);
                return "Player deleted successfully";
            }
        }
        throw new CustomException("Something went wrong please try again",HttpStatus.BAD_REQUEST,"Something went wrong please try again");


    }
}
