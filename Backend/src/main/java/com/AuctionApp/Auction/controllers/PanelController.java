package com.AuctionApp.Auction.controllers;

import com.AuctionApp.Auction.Component.CategoryRequirements;
import com.AuctionApp.Auction.Component.Status;
import com.AuctionApp.Auction.DTO.BidDTO;
import com.AuctionApp.Auction.Services.BidService;
import com.AuctionApp.Auction.Services.JWTService;
import com.AuctionApp.Auction.Services.PanelService;
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
@CrossOrigin(origins = "http://localhost:3000")
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

        @Autowired
        private PanelService panelService;

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
                String message = panelService.onNoneCategorySold(bidDTO,auctionId);
                messagingTemplate.convertAndSend("/live/sold/" + auctionId, "Player Sold successfully!");
                return ResponseEntity.ok(message);
        }

        @Transactional
        @PostMapping("/panel/category-sold/{auctionId}")
        public ResponseEntity<String> onCategorySold(@RequestBody BidDTO bidDTO, @PathVariable String auctionId) {
            String message = panelService.categorySold(bidDTO,auctionId);
            messagingTemplate.convertAndSend("/live/sold/" + auctionId, "Player Sold successfully!");
            return ResponseEntity.ok("Player sold successfully");
        }


        @Transactional
        @PutMapping("/reauction-none-category-player/{auctionId}")
        public ResponseEntity<String> reauctionNoneCategoryPlayer(@RequestBody Bid bid, @PathVariable String auctionId) {
            return ResponseEntity.ok(panelService.reauctionNoneCategoryPlayer(bid,auctionId));
        }


        @PutMapping("/reauction-category-player")
        public ResponseEntity<String> reauctionCategoryPlayer(@RequestBody Bid bid) {
            return ResponseEntity.ok(panelService.reauctionCategoryPlayer(bid));
        }







        @PutMapping("/add-unsold-player/{auctionId}")
            public ResponseEntity<String> addUnsoldPlayer(@RequestBody Player player,@PathVariable String auctionId){
                String message = panelService.addToUnsold(player,auctionId);
                messagingTemplate.convertAndSend("/live/unsold/" + auctionId, "This player has gone unsold.");
                return ResponseEntity.ok(message);
        }


        @PutMapping("/reauction-unsold/{auctionId}")
        public ResponseEntity<String> reauctionUnsold(@PathVariable String auctionId){
                return ResponseEntity.ok(panelService.removeFromUnsold(auctionId));
        }
}
