package com.AuctionApp.Auction.controllers;

import com.AuctionApp.Auction.DTO.PlayerDTO;
import com.AuctionApp.Auction.Services.PlayerService;
import com.AuctionApp.Auction.entites.Player;
import com.AuctionApp.Auction.Component.ExcelHelper;
import jakarta.validation.Valid;
import org.springframework.aop.BeforeAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@CrossOrigin
public class PlayerController {

        @Autowired
        private PlayerService playerService;

        @GetMapping("/auction-player/{auctionId}")
        public ResponseEntity<List<Player>> showAuctionPlayers(@PathVariable String auctionId){
                return ResponseEntity.ok(playerService.show(auctionId));
        }

        @PostMapping("/add-auction/{auctionId}")
        public ResponseEntity<Player> addAuction(@RequestBody @Valid PlayerDTO playerRequest,@PathVariable String auctionId){
            return new ResponseEntity<>(playerService.add(playerRequest,auctionId), HttpStatus.CREATED);
        }

        @PostMapping("/join-auction/{auctionId}/{userId}")
        public ResponseEntity<Player> joinAuction(@RequestBody PlayerDTO userAsPlayerReq, @PathVariable String auctionId, @PathVariable String userId){
                return ResponseEntity.ok().body(playerService.join(userAsPlayerReq,auctionId,userId));
        }

        @PutMapping("/edit-auction-player/{playerId}")
        public ResponseEntity<String> editPlayer(@PathVariable String playerId,  @RequestBody @Valid PlayerDTO playerRequest){
                 playerService.edit(playerRequest, playerId);
                 return new ResponseEntity<>("Player modified successfully",HttpStatus.OK);
        }



        @DeleteMapping("/delete-player/{playerId}/{auctionId}")
        public ResponseEntity<String> deleteUserAsPlayer(@PathVariable String playerId,@PathVariable String auctionId){
                playerService.deletePlayer(playerId,auctionId);
                return ResponseEntity.ok("Player deleted successfully");
        }

        @PostMapping("/player-file-upload/{auctionId}")
        public ResponseEntity<?> upload(@RequestParam("file")MultipartFile file, @PathVariable String auctionId) throws IOException {
                if(file.getContentType() == null){
                        return new ResponseEntity<>("File in not present!",HttpStatus.BAD_REQUEST);
                }
                if(ExcelHelper.checkExcelFormat(file)){
                        playerService.save(file,auctionId);
                        return ResponseEntity.ok().body("File uploaded");
                }else{
                        return new ResponseEntity<>("Only .xlsx file format is allowed", HttpStatus.BAD_REQUEST);
                }
        }



}
