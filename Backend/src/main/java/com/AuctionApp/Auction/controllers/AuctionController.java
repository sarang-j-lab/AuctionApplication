package com.AuctionApp.Auction.controllers;

import com.AuctionApp.Auction.Component.AdditinalIncrements;
import com.AuctionApp.Auction.DTO.AuctionDTO;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.Services.AuctionService;
import com.AuctionApp.Auction.entites.Auction;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auction")
@CrossOrigin
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private AuctionRepository auctionRepository;


    @GetMapping("/my-auction/{userId}")
    public ResponseEntity<List<Auction>> getAllAuctionOfUser(@PathVariable String userId){
        return new ResponseEntity<>(auctionService.getAll(userId),HttpStatus.OK);
    }

//    this route will give you auction without authentication
    @GetMapping("/get-auction/{auctionId}")
    public ResponseEntity<Auction> getAuctionWithoutAuth(@PathVariable String auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            return new ResponseEntity<>(auction.get(),HttpStatus.OK);
        }
        throw new CustomException("Auction not found",HttpStatus.BAD_REQUEST,"Auciton not found");
    }

    @GetMapping("/get-detailed-auction/{auctionId}")
    public ResponseEntity<Map<String,Object>> getDetailedAuction(@PathVariable String auctionId){
        return ResponseEntity.ok().body(auctionService.getDetailedAuction(auctionId));
    }

   @GetMapping("/auction-details/{auctionId}")
    public ResponseEntity<Auction> getAuction(@PathVariable String auctionId){
        return ResponseEntity.ok(auctionService.getAuction(auctionId));
   }

    @PostMapping("/new-auction/{userId}")
    public ResponseEntity<Auction> createNewAuction(@RequestBody @Valid AuctionDTO auction,@PathVariable String userId){
        return new ResponseEntity<>(auctionService.create(auction,userId), HttpStatus.CREATED);
    }


    @PutMapping("/edit-auction/{auctionId}")
    public ResponseEntity<Auction> updateAuction(@PathVariable String auctionId,@RequestBody @Valid AuctionDTO auctionRequest)  {
        System.out.println(auctionRequest);
        return new ResponseEntity<>(auctionService.updateAuction(auctionId,auctionRequest),HttpStatus.ACCEPTED);
   }

   @DeleteMapping("/delete-auction/{auctionId}")
    public ResponseEntity<String> deleteAuction(@PathVariable String auctionId){
       auctionService.deleteAuction(auctionId);
        return new ResponseEntity<>("Auction deleted successfully",HttpStatus.OK);
   }

   @GetMapping("/get-increments/{auctionId}")
   public ResponseEntity<List<AdditinalIncrements>> getAuctionIncrements(@PathVariable String auctionId){
        return new ResponseEntity<>(auctionService.getIncrements(auctionId),HttpStatus.OK);
   }

   @PostMapping("/add-increments/{auctionId}")
    public ResponseEntity<Auction> additionalIncrements(@RequestBody @Valid AdditinalIncrements increments , @PathVariable String auctionId){
       return new ResponseEntity<>(auctionService.addIncrements(increments,auctionId),HttpStatus.CREATED);
   }

   @DeleteMapping("/delete-increment/{auctionId}/{incrementId}")
    public ResponseEntity<Auction> deleteIncrement(@PathVariable String auctionId,@PathVariable String incrementId){
        return new ResponseEntity<>(auctionService.deleteIncrement(auctionId,incrementId),HttpStatus.OK);
   }
}
