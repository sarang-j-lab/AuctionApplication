package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.DTO.TeamDTO;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.entites.Auction;
import com.AuctionApp.Auction.entites.Team;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import com.AuctionApp.Auction.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    public List<Team> auctionTeams(long auctionId){
        return auctionRepository.findById(auctionId).get().getTeams();
    }

    public Team create(TeamDTO newTeam,long auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            Team team = new Team(0,newTeam.getTeamName(),newTeam.getShortcutKey(),newTeam.getShortName());
            Team savedTeam = teamRepository.save(team);
            auction.get().getTeams().add(savedTeam);
            auctionRepository.save(auction.get());
            return savedTeam;
        }
        throw new CustomException("Provide valid auctionID", HttpStatus.BAD_REQUEST,"Invalid auctionID not found in database");
    }

    public void update(TeamDTO team,long teamId){
        Optional<Team> dbTeam = teamRepository.findById(teamId);
        if(dbTeam.isPresent()){
            dbTeam.get().setTeamName(team.getTeamName());
            dbTeam.get().setShortcutKey(team.getShortcutKey());
            dbTeam.get().setShortName(team.getShortName());
            teamRepository.save(dbTeam.get());
            return;
        }else{
        throw new CustomException("Team not found",HttpStatus.BAD_REQUEST,"Team not found with this team id");
        }
    }


    public void delete(long teamId){
        teamRepository.deleteById(teamId);
    }
}
