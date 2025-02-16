package com.AuctionApp.Auction.controllers;

import com.AuctionApp.Auction.Services.AuctionService;
import com.AuctionApp.Auction.entites.Auction;
import com.AuctionApp.Auction.entites.User;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import com.AuctionApp.Auction.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        System.out.println(payload);
        return ResponseEntity.ok().body("Size updated successfully");
    }

}
