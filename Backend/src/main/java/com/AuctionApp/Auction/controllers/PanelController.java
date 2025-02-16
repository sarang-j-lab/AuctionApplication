package com.AuctionApp.Auction.controllers;

import com.AuctionApp.Auction.Component.CategoryRequirements;
import com.AuctionApp.Auction.Component.Status;
import com.AuctionApp.Auction.DTO.BidDTO;
import com.AuctionApp.Auction.Services.BidService;
import com.AuctionApp.Auction.Services.JWTService;
import com.AuctionApp.Auction.entites.*;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import com.AuctionApp.Auction.repositories.CategoryRepository;
import com.AuctionApp.Auction.repositories.PlayerRepository;
import com.AuctionApp.Auction.repositories.TeamRepository;
import com.AuctionApp.Auction.util.Generate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
public class PanelController {

        @Autowired
        private JWTService jwtService;

        @Autowired
        private PlayerRepository playerRepository;

        @Autowired
        private TeamRepository teamRepository;

        @Autowired
        private AuctionRepository auctionRepository;

        @Autowired
        private CategoryRepository categoryRepository;

        @Autowired
        private BidService bidService;

        private final SimpMessagingTemplate messagingTemplate;

        public PanelController(SimpMessagingTemplate messagingTemplate) {
                this.messagingTemplate = messagingTemplate;
        }


        @MessageMapping("/player/{auctionId}")
        public void handlePlayerSelection(@DestinationVariable String auctionId,@Payload String selectedPlayer){
            String destination = "/live/player/" + auctionId;

            messagingTemplate.convertAndSend(destination, selectedPlayer);
        }

        @MessageMapping("/bid/{auctionId}")
        public void handleBid(@DestinationVariable String auctionId, @Payload String bid ){

            String destination = "/live/bid/" + auctionId;

            messagingTemplate.convertAndSend(destination, bid);
        }


        @Transactional
        @PostMapping("/panel/noneCategory-sold/{auctionId}")
        public ResponseEntity<String> onSold(@RequestBody BidDTO bidDTO, @PathVariable String auctionId){
                Optional<Player> player = playerRepository.findById(bidDTO.getPlayer());
                Optional<Team> team = teamRepository.findById(bidDTO.getTeam());
                Optional<Auction> auction = auctionRepository.findById(auctionId);
                if(bidDTO.getCategory() == null && team.isPresent() && player.isPresent() && auction.isPresent()){
                        team.get().setTotalPoints(team.get().getTotalPoints() - bidDTO.getAmount());
                        team.get().setNoneCategoryPlayerBought(team.get().getNoneCategoryPlayerBought() + 1);


                        if(team.get().getNoneCategoryPlayerBought() <= auction.get().getNoneCategoryPlayerRequired()){
                                team.get().setReserve(team.get().getReserve() - auction.get().getBaseBid());
                                team.get().setNoneCategoryPlayerReserve(team.get().getNoneCategoryPlayerReserve() - auction.get().getBaseBid());
                        }

                        team.get().setMaxBid(team.get().getTotalPoints() - team.get().getReserve());

                        player.get().setStatus(Status.SOLD);
                        Bid savedBid = bidService.saveBid(new Bid(Generate.generateId(),player.get().getPlayerId(),team.get().getTeamId(),bidDTO.getAmount(),null));
                        player.get().setBid(savedBid);
                        playerRepository.save(player.get());

                        team.get().getTeamPlayers().add(player.get());



                        teamRepository.save(team.get());
                        String destination = "/live/sold/" + auctionId;
                        messagingTemplate.convertAndSend(destination,"Player Sold successfully!");
                     return ResponseEntity.ok().body("player sold successfully");
                }else{
                    return ResponseEntity.ok("null");
                }
        }


        @Transactional
        @PostMapping("/panel/category-sold/{auctionId}")
        public ResponseEntity<String> onCategorySold(@RequestBody BidDTO bidDTO,@PathVariable String auctionId){
                Optional<Category> category = categoryRepository.findById(bidDTO.getCategory());
                Optional<Team> team = teamRepository.findById(bidDTO.getTeam());
                Optional<Player> player = playerRepository.findById(bidDTO.getPlayer());
                if(category.isPresent() && team.isPresent() && player.isPresent()){
                        CategoryRequirements categoryRequirement = null;
                        for(CategoryRequirements categoryRequ:team.get().getPlayerRequirement()){
                                if(categoryRequ.getCategory().equals(bidDTO.getCategory())){
                                        categoryRequirement = categoryRequ;
                                        break;
                                }
                        }
                        if(categoryRequirement != null){
                                       categoryRequirement.setBought(categoryRequirement.getBought() + 1);
                                       team.get().setTotalPoints(team.get().getTotalPoints() - bidDTO.getAmount());
                                       if(categoryRequirement.getBought() <= categoryRequirement.getPlayerRequired()){
                                               categoryRequirement.setReserve(categoryRequirement.getReserve() - category.get().getBaseBid());
                                               team.get().setReserve(team.get().getReserve() - category.get().getBaseBid());
                                       }
                                       team.get().setMaxBid(team.get().getTotalPoints() - team.get().getReserve());
                                       Bid savedBid = bidService.saveBid(new Bid(Generate.generateId(),player.get().getPlayerId(),team.get().getTeamId(),bidDTO.getAmount(),bidDTO.getCategory()));
                                       player.get().setStatus(Status.SOLD);
                                       player.get().setBid(savedBid);
                                       playerRepository.save(player.get());
                                       team.get().getTeamPlayers().add(player.get());
                                       teamRepository.save(team.get());
                                       String destination = "/live/sold/" + auctionId;
                                       messagingTemplate.convertAndSend(destination,"Player Sold successfully!");
                            return ResponseEntity.ok().body("player sold successfully");
                        }
                        throw new CustomException("Something went wrong please try again",HttpStatus.BAD_REQUEST,"Error");
                }
            throw new CustomException("Something went wrong please try again",HttpStatus.BAD_REQUEST,"Error");
        }

        @PutMapping("/reauction-category-player")
        public ResponseEntity<String> reauctionCategoryPlayer(@RequestBody Bid bid){
            Optional<Player> player = playerRepository.findById(bid.getPlayer());
            Optional<Team> team = teamRepository.findById(bid.getTeam());
            Optional<Category> category = categoryRepository.findById(bid.getCategory());
            if(player.isPresent() && team.isPresent() && category.isPresent()){
                CategoryRequirements cateRequ = null;
                for(CategoryRequirements categoryRequ : team.get().getPlayerRequirement()){
                    if(categoryRequ.getCategory().equals(bid.getCategory())){
                        cateRequ = categoryRequ;
                        break;
                    }
                }
                if(cateRequ != null){
                    cateRequ.setBought(cateRequ.getBought() - 1);
                    team.get().getTeamPlayers().remove(player.get());
                    team.get().setTotalPoints(team.get().getTotalPoints() + bid.getAmount());
                    if(cateRequ.getBought() < cateRequ.getPlayerRequired()){
                        cateRequ.setReserve(cateRequ.getReserve() + category.get().getBaseBid());
                        team.get().setReserve(team.get().getReserve() + category.get().getBaseBid());
                    }
                    team.get().setMaxBid(team.get().getTotalPoints() - team.get().getReserve());
                    player.get().setStatus(Status.PENDING);
                    player.get().setBid(null);
                    bidService.deleteBid(bid.getId());


                    teamRepository.save(team.get());
                    playerRepository.save(player.get());
                    return ResponseEntity.ok("Player can be re-auction now!");
                }else{
                    throw new CustomException("Category not found!",HttpStatus.BAD_REQUEST,"error");
                }
            }
            throw new CustomException("Something went wrong please try again!!",HttpStatus.BAD_REQUEST,"error");
        }


        @Transactional
        @PutMapping("/reauction-none-category-player/{auctionId}")
        public ResponseEntity<String> reauctionNoneCategoryPlayer(@RequestBody Bid bid,@PathVariable String auctionId){
                Optional<Player> player = playerRepository.findById(bid.getPlayer());
                Optional<Team> team = teamRepository.findById(bid.getTeam());
                Optional<Auction> auction = auctionRepository.findById(auctionId);
                if(team.isPresent() && player.isPresent() && auction.isPresent()){
                        team.get().setNoneCategoryPlayerBought(team.get().getNoneCategoryPlayerBought() - 1);
                        team.get().getTeamPlayers().remove(player.get());
                        team.get().setTotalPoints(team.get().getTotalPoints() + bid.getAmount());
                        if(team.get().getNoneCategoryPlayerBought() < auction.get().getNoneCategoryPlayerRequired()){
                                team.get().setReserve(team.get().getReserve() + auction.get().getBaseBid());
                                team.get().setNoneCategoryPlayerReserve(team.get().getNoneCategoryPlayerReserve() + auction.get().getBaseBid());
                        }
                        team.get().setMaxBid(team.get().getTotalPoints() - team.get().getReserve());
//                        player.get().setSold(false);
                        player.get().setStatus(Status.PENDING);
                        player.get().setBid(null);
                        bidService.deleteBid(bid.getId());


                        teamRepository.save(team.get());
                        playerRepository.save(player.get());

                        return ResponseEntity.ok("Player can be re-auction now");
                }
                throw new CustomException("Something went wrong please try again",HttpStatus.BAD_REQUEST,"Missing credencials");
        }

        @PutMapping("/add-unsold-player/{auctionId}")
        public ResponseEntity<String> addUnsoldPlayer(@RequestBody Player player,@PathVariable String auctionId){
                Optional<Auction> auction = auctionRepository.findById(auctionId);
                if(auction.isPresent() && player != null){
//                        player.setUnsold(true);
                    player.setStatus(Status.UNSOLD);
                        playerRepository.save(player);
                        auction.get().getUnsoldPlayers().add(player);
                        auctionRepository.save(auction.get());
                        String destination = "/live/unsold/" + auctionId;
                        messagingTemplate.convertAndSend(destination,"This player has gone unsold.");
                        return ResponseEntity.ok("Player remain unsold successfully");
                }
                throw new CustomException("Something went wrong please try again",HttpStatus.BAD_REQUEST,"Try again");
        }


        @PutMapping("/reauction-unsold/{auctionId}")
        public ResponseEntity<String> reauctionUnsold(@PathVariable String auctionId){
                Optional<Auction> auction = auctionRepository.findById(auctionId);
                if(auction.isPresent()){
                        for(Player player:auction.get().getUnsoldPlayers()){
//                                player.setUnsold(false);
                                player.setStatus(Status.PENDING);
                                playerRepository.save((player));
                        }
                        auction.get().getUnsoldPlayers().clear();
                        auctionRepository.save(auction.get());
                        return ResponseEntity.ok("Unsold player added to Reauction!");
                }
                throw new CustomException("Auction not found",HttpStatus.BAD_REQUEST,"Auction not found");

        }
}
