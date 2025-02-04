package com.AuctionApp.Auction.controllers;

import com.AuctionApp.Auction.DTO.TeamDTO;
import com.AuctionApp.Auction.Services.TeamService;
import com.AuctionApp.Auction.entites.Team;
import com.mysql.cj.x.protobuf.Mysqlx;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping("/auction-teams/{auctionId}")
    public List<Team> showAuctionTeams(@PathVariable String auctionId){
        return teamService.auctionTeams(auctionId);
    }


    @PostMapping("/add-team/{auctionId}")
    public Team addNewTeam(@RequestBody @Valid TeamDTO teamRequest,@PathVariable String auctionId){
        return teamService.create(teamRequest,auctionId);
    }

    @PutMapping("/edit-team/{teamId}")
    public ResponseEntity<String> editTeam(@RequestBody @Valid TeamDTO team,@PathVariable String teamId){
        teamService.update(team,teamId);
        return new ResponseEntity<>("Team edited successfully!",HttpStatus.OK);
    }

    @DeleteMapping("/delete-team/{teamId}")
    public ResponseEntity<String> deleteTeam(@PathVariable String teamId){
        teamService.delete(teamId);
        return new ResponseEntity<>("Team delete successfully!", HttpStatus.OK);
    }

}
