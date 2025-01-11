package com.AuctionApp.Auction.controllers;

import com.AuctionApp.Auction.DTO.TeamDTO;
import com.AuctionApp.Auction.Services.TeamService;
import com.AuctionApp.Auction.entites.Team;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping("/auction-teams/{auctionId}")
    public List<Team> showAuctionTeams(@PathVariable long auctionId){
        return teamService.auctionTeams(auctionId);
    }

    @PostMapping("/add-team/{auctionId}")
    public Team addNewTeam(@RequestBody @Valid TeamDTO teamRequest,@PathVariable long auctionId){
        return teamService.create(teamRequest,auctionId);
    }

    @PutMapping("/edit-team/{teamId}")
    public void editTeam(@RequestBody @Valid TeamDTO team,@PathVariable long teamId){
        teamService.update(team,teamId);
    }

    @DeleteMapping("/delete-team/{teamId}")
    public void deleteTeam(@PathVariable long teamId){
        teamService.delete(teamId);
    }

}
