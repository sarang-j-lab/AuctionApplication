package com.AuctionApp.Auction.controllers;

import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.Services.AuctionService;
import com.AuctionApp.Auction.Services.UserService;
import com.AuctionApp.Auction.entites.Auction;
import com.AuctionApp.Auction.entites.User;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import com.AuctionApp.Auction.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private AuctionRepository auctionRepository;


    @Autowired
    private UserService userService;

    @GetMapping("/get-all-user")
    public ResponseEntity<List<User>> getAllUser(){
        return ResponseEntity.ok(userRepository.findAll());
    }
    @GetMapping("/get-user-auction/{userId}")
    public ResponseEntity<List<Auction>> getAllUser(@PathVariable String userId){
        return ResponseEntity.ok(auctionService.getAll(userId));
    }

    @PutMapping("/update-team-size/{auctionId}")
    public ResponseEntity<String> updateTeamSize(@PathVariable String auctionId,@RequestBody Map<String,Integer> payload){
        try{
            auctionRepository.updateTeamSize(payload.get("teamSize"),auctionId);
            return ResponseEntity.ok().body("Team Size updated successfully");
        }catch(Exception e){
            throw new CustomException("Failed to update team size", HttpStatus.INTERNAL_SERVER_ERROR,"error");
        }
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.delete(userId));
    }
    @DeleteMapping("/delete-auction/{auctionId}")
    public ResponseEntity<String> deleteAuction(@PathVariable String auctionId){
        auctionService.deleteAuction(auctionId);
        return ResponseEntity.ok("Auction deleted successfully!");
    }

}
