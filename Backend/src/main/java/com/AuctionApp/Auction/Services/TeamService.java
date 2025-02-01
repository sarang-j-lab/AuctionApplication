package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.DTO.TeamDTO;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.entites.Auction;
import com.AuctionApp.Auction.entites.Team;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import com.AuctionApp.Auction.repositories.TeamRepository;
import com.AuctionApp.Auction.util.Generate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private AuctionRepository auctionRepository;



    public ArrayList<String> getShortcutKeys(String auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            return auction.get().getShortcutKeys();
        }
        throw new CustomException("Auction not found",HttpStatus.INTERNAL_SERVER_ERROR,"Please try again.");
    }

    public List<Team> auctionTeams(String auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            return auction.get().getTeams();
        }
        throw new CustomException("Auction not found",HttpStatus.BAD_REQUEST,"Auction not found with this ID");
    }
    @Transactional
    public Team create(TeamDTO newTeam,String auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            Team team = new Team(Generate.generateId(),newTeam.getTeamName(),newTeam.getShortcutKey(),newTeam.getShortName());
            Team savedTeam = teamRepository.save(team);
            auction.get().getShortcutKeys().remove(savedTeam.getShortcutKey());
            auction.get().getTeams().add(savedTeam);
            auctionRepository.save(auction.get());
            return savedTeam;
        }
        throw new CustomException("Provide valid auctionID", HttpStatus.BAD_REQUEST,"Invalid auctionID not found in database");
    }
    @Transactional
    public void update(TeamDTO team,String teamId,String auctionId){
        Optional<Team> dbTeam = teamRepository.findById(teamId);
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(dbTeam.isPresent() && auction.isPresent()){
            if(!dbTeam.get().getShortcutKey().equals(team.getShortcutKey())){
                auction.get().getShortcutKeys().remove(team.getShortcutKey());
                auction.get().getShortcutKeys().add(0,dbTeam.get().getShortcutKey());
            }
            dbTeam.get().setTeamName(team.getTeamName());
            dbTeam.get().setShortcutKey(team.getShortcutKey());
            dbTeam.get().setShortName(team.getShortName());
            auctionRepository.save(auction.get());
            teamRepository.save(dbTeam.get());
            return;
        }else{
        throw new CustomException("Team not found",HttpStatus.BAD_REQUEST,"Team not found with this team id");
        }
    }

    @Transactional
    public void delete(String teamId,String auctionId){
        Optional<Team> team = teamRepository.findById(teamId);
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(team.isPresent() && auction.isPresent()){
            auction.get().getShortcutKeys().add(0,team.get().getShortcutKey());
            auctionRepository.save(auction.get());
            teamRepository.deleteById(teamId);
        }
    }
}
