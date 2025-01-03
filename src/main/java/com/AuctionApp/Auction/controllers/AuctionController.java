package com.AuctionApp.Auction.controllers;

import com.AuctionApp.Auction.DTO.AuctionDTO;
import com.AuctionApp.Auction.Services.AuctionService;
import com.AuctionApp.Auction.entites.Auction;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/auction")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;


    @GetMapping("/my-auction/{userId}")
    public ResponseEntity<List<Auction>> getAllAuctionOfUser(@PathVariable long userId){
        return new ResponseEntity<>(auctionService.getAll(userId),HttpStatus.OK);
    }

   @GetMapping("/auction-details/{auctionId}")
    public ResponseEntity<Auction> getAuction(@PathVariable long auctionId){
        return ResponseEntity.ok(auctionService.getAuction(auctionId));
   }

    @PostMapping("/new-auction")
    public ResponseEntity<Auction> createNewAuction(@RequestBody @Valid AuctionDTO auction){
        return new ResponseEntity<>(auctionService.create(auction), HttpStatus.CREATED);
    }


    @PutMapping("/edit-auction/{auctionId}")
    public ResponseEntity<String> updateAuction(@PathVariable long auctionId,@RequestBody @Valid AuctionDTO auctionRequest)  {
        auctionService.updateAuction(auctionId,auctionRequest);
        return new ResponseEntity<>("Auction edited successfully",HttpStatus.ACCEPTED);
   }

   @DeleteMapping("/delete-auction/{auctionId}")
    public ResponseEntity<String> deleteAuction(@PathVariable long auctionId){
       auctionService.deleteAuction(auctionId);
        return new ResponseEntity<>("Auction deleted successfully",HttpStatus.OK);
   }
}
