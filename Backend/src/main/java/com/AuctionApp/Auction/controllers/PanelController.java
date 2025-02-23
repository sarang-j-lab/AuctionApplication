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
                Optional<Player> playerOpt = playerRepository.findById(bidDTO.getPlayer());
                Optional<Team> teamOpt = teamRepository.findById(bidDTO.getTeam());
                Optional<Auction> auctionOpt = auctionRepository.findById(auctionId);

                if(bidDTO.getCategory() != null || teamOpt.isEmpty() || playerOpt.isEmpty() || auctionOpt.isEmpty()){
                    return ResponseEntity.ok("null");
                }

                Player player = playerOpt.get();
                Team team = teamOpt.get();
                Auction auction = auctionOpt.get();

                team.setTotalPoints(team.getTotalPoints() - bidDTO.getAmount());
                team.setNoneCategoryPlayerBought(team.getNoneCategoryPlayerBought() + 1);

                if(team.getNoneCategoryPlayerBought() <= auction.getNoneCategoryPlayerRequired()){
                    int baseBid = auction.getBaseBid();
                    team.setReserve(team.getReserve() - baseBid);
                    team.setNoneCategoryPlayerReserve(team.getNoneCategoryPlayerReserve() - baseBid);
                }
                team.setMaxBid(team.getTotalPoints() - team.getReserve());

                player.setStatus(Status.SOLD);
                Bid savedBid = bidService.saveBid(new Bid(Generate.generateId(),player.getPlayerId(),team.getTeamId(),bidDTO.getAmount(),null));
                player.setBid(savedBid);
                playerRepository.save(player);

                team.getTeamPlayers().add(player);
                teamRepository.save(team);

                messagingTemplate.convertAndSend("/live/sold/" + auctionId, "Player Sold successfully!");
                return ResponseEntity.ok("Player sold successfully");
        }

        @Transactional
        @PostMapping("/panel/category-sold/{auctionId}")
        public ResponseEntity<String> onCategorySold(@RequestBody BidDTO bidDTO, @PathVariable String auctionId) {
            Optional<Category> categoryOpt = categoryRepository.findById(bidDTO.getCategory());
            Optional<Team> teamOpt = teamRepository.findById(bidDTO.getTeam());
            Optional<Player> playerOpt = playerRepository.findById(bidDTO.getPlayer());

            if (categoryOpt.isEmpty() || teamOpt.isEmpty() || playerOpt.isEmpty()) {
                throw new CustomException("Something went wrong please try again", HttpStatus.BAD_REQUEST, "Error");
            }

            Category category = categoryOpt.get();
            Team team = teamOpt.get();
            Player player = playerOpt.get();

            CategoryRequirements categoryRequirement = team.getPlayerRequirement().stream()
                    .filter(req -> req.getCategory().equals(bidDTO.getCategory()))
                    .findFirst()
                    .orElseThrow(() -> new CustomException("Category requirement not found", HttpStatus.BAD_REQUEST, "Error"));

            categoryRequirement.setBought(categoryRequirement.getBought() + 1);
            team.setTotalPoints(team.getTotalPoints() - bidDTO.getAmount());

            if (categoryRequirement.getBought() <= categoryRequirement.getPlayerRequired()) {
                int baseBid = category.getBaseBid();
                categoryRequirement.setReserve(categoryRequirement.getReserve() - baseBid);
                team.setReserve(team.getReserve() - baseBid);
            }

            team.setMaxBid(team.getTotalPoints() - team.getReserve());

            Bid savedBid = bidService.saveBid(new Bid(Generate.generateId(), player.getPlayerId(), team.getTeamId(), bidDTO.getAmount(), bidDTO.getCategory()));
            player.setStatus(Status.SOLD);
            player.setBid(savedBid);
            playerRepository.save(player);

            team.getTeamPlayers().add(player);
            teamRepository.save(team);

            messagingTemplate.convertAndSend("/live/sold/" + auctionId, "Player Sold successfully!");
            return ResponseEntity.ok("Player sold successfully");
        }

        @PutMapping("/reauction-category-player")
        public ResponseEntity<String> reauctionCategoryPlayer(@RequestBody Bid bid) {
            Optional<Player> playerOpt = playerRepository.findById(bid.getPlayer());
            Optional<Team> teamOpt = teamRepository.findById(bid.getTeam());
            Optional<Category> categoryOpt = categoryRepository.findById(bid.getCategory());

            if (playerOpt.isEmpty() || teamOpt.isEmpty() || categoryOpt.isEmpty()) {
                throw new CustomException("Something went wrong please try again!!", HttpStatus.BAD_REQUEST, "error");
            }

            Player player = playerOpt.get();
            Team team = teamOpt.get();
            Category category = categoryOpt.get();

            CategoryRequirements categoryRequirement = team.getPlayerRequirement().stream()
                    .filter(req -> req.getCategory().equals(bid.getCategory()))
                    .findFirst()
                    .orElseThrow(() -> new CustomException("Category not found!", HttpStatus.BAD_REQUEST, "error"));

            categoryRequirement.setBought(categoryRequirement.getBought() - 1);
            team.getTeamPlayers().remove(player);
            team.setTotalPoints(team.getTotalPoints() + bid.getAmount());

            if (categoryRequirement.getBought() < categoryRequirement.getPlayerRequired()) {
                int baseBid = category.getBaseBid();
                categoryRequirement.setReserve(categoryRequirement.getReserve() + baseBid);
                team.setReserve(team.getReserve() + baseBid);
            }

            team.setMaxBid(team.getTotalPoints() - team.getReserve());
            player.setStatus(Status.PENDING);
            player.setBid(null);

            bidService.deleteBid(bid.getId());
            teamRepository.save(team);
            playerRepository.save(player);

            return ResponseEntity.ok("Player can be re-auctioned now!");
        }


        @Transactional
        @PutMapping("/reauction-none-category-player/{auctionId}")
        public ResponseEntity<String> reauctionNoneCategoryPlayer(@RequestBody Bid bid, @PathVariable String auctionId) {
        Optional<Player> playerOpt = playerRepository.findById(bid.getPlayer());
        Optional<Team> teamOpt = teamRepository.findById(bid.getTeam());
        Optional<Auction> auctionOpt = auctionRepository.findById(auctionId);

        if (playerOpt.isEmpty() || teamOpt.isEmpty() || auctionOpt.isEmpty()) {
            throw new CustomException("Something went wrong please try again", HttpStatus.BAD_REQUEST, "Missing credentials");
        }

        Player player = playerOpt.get();
        Team team = teamOpt.get();
        Auction auction = auctionOpt.get();

        team.setNoneCategoryPlayerBought(team.getNoneCategoryPlayerBought() - 1);
        team.getTeamPlayers().remove(player);
        team.setTotalPoints(team.getTotalPoints() + bid.getAmount());

        if (team.getNoneCategoryPlayerBought() < auction.getNoneCategoryPlayerRequired()) {
            int baseBid = auction.getBaseBid();
            team.setReserve(team.getReserve() + baseBid);
            team.setNoneCategoryPlayerReserve(team.getNoneCategoryPlayerReserve() + baseBid);
        }

        team.setMaxBid(team.getTotalPoints() - team.getReserve());
        player.setStatus(Status.PENDING);
        player.setBid(null);

        bidService.deleteBid(bid.getId());
        teamRepository.save(team);
        playerRepository.save(player);

        return ResponseEntity.ok("Player can be re-auctioned now");
    }




        @PutMapping("/add-unsold-player/{auctionId}")
            public ResponseEntity<String> addUnsoldPlayer(@RequestBody Player player,@PathVariable String auctionId){
                Optional<Auction> auction = auctionRepository.findById(auctionId);
                if(auction.isPresent() && player != null){
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
