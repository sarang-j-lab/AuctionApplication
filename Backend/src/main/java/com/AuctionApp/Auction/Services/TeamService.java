package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.Component.CategoryRequirements;
import com.AuctionApp.Auction.DTO.TeamDTO;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.entites.Auction;
import com.AuctionApp.Auction.entites.Category;
import com.AuctionApp.Auction.entites.Player;
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
            Auction getAuction = auction.get();
            Team team = new Team(Generate.generateId(),newTeam.getTeamName(),newTeam.getShortName(),getAuction.getPointsPerTeam(),getAuction.getReserve(),getAuction.getPointsPerTeam() - getAuction.getReserve(),0,auction.get().getNoneCategoryPlayerRequired() * auction.get().getBaseBid());

            List<Category> auctionCategories = getAuction.getCategories();

            Team savedTeam = teamRepository.save(team);
            for(Category category: auctionCategories){
                CategoryRequirements categoryRequirements = new CategoryRequirements(category.getCategoryId(),0, category.getMinPlayerPerTeam(), category.getMaxPlayerPerTeam(),category.getBaseBid() * category.getMinPlayerPerTeam());
                team.getPlayerRequirement().add(categoryRequirements);
                teamRepository.save(team);
            }

            auction.get().getTeams().add(savedTeam);
            auctionRepository.save(getAuction);
            return savedTeam;
        }
        throw new CustomException("Provide valid auctionID", HttpStatus.BAD_REQUEST,"Invalid auctionID not found in database");
    }


    @Transactional
    public void update(TeamDTO team,String teamId){
        Optional<Team> dbTeam = teamRepository.findById(teamId);
        if(dbTeam.isPresent() ){
            dbTeam.get().setTeamName(team.getTeamName());
            dbTeam.get().setShortName(team.getShortName());
            teamRepository.save(dbTeam.get());
            return;
        }else{
        throw new CustomException("Team not found",HttpStatus.BAD_REQUEST,"Team not found with this team id");
        }
    }

    @Transactional
    public void delete(String teamId){
            Optional<Team> team = teamRepository.findById(teamId);
            if(team.isPresent()){
                if(!team.get().getTeamPlayers().isEmpty()){
                    throw new CustomException("This team contains auctioned players. First, you have to place those players for re-auction.",HttpStatus.BAD_REQUEST,"error");
                }
                teamRepository.deleteById(teamId);
            }
    }

    public List<Player> showTeamPlayers(String teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()){
            return team.get().getTeamPlayers();
        }
        throw new CustomException("Team not found",HttpStatus.BAD_REQUEST,"Team not found");
    }
}
