package com.AuctionApp.Auction.controllers;

import com.AuctionApp.Auction.DTO.PlayerDTO;
import com.AuctionApp.Auction.Services.PlayerService;
import com.AuctionApp.Auction.entites.Player;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class PlayerController {

        @Autowired
        private PlayerService playerService;

        @GetMapping("/auction-player/{auctionId}")
        public ResponseEntity<List<Player>> showAuctionPlayers(@PathVariable long auctionId){
                return ResponseEntity.ok(playerService.show(auctionId));
        }

        @PostMapping("/join-auction")
        public ResponseEntity<Player> joinAuction(@RequestBody @Valid PlayerDTO playerRequest){
            return new ResponseEntity<>(playerService.join(playerRequest), HttpStatus.CREATED);
        }


        @PutMapping("/edit-auction-player/{playerId}")
        public ResponseEntity<String> editPlayer(@PathVariable long playerId,  @RequestBody @Valid PlayerDTO playerRequest){
                 playerService.edit(playerRequest, playerId);
                 return new ResponseEntity<>("User modified successfully",HttpStatus.OK);
        }

        @DeleteMapping("delete-player/{playerId}")
        public ResponseEntity<String> deletePlayer(@PathVariable long playerId){
                playerService.delete(playerId);
                return ResponseEntity.ok("Player deleted successfully");
        }
}
