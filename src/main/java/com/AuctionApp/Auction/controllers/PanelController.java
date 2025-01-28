package com.AuctionApp.Auction.controllers;

import com.AuctionApp.Auction.entites.Bid;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.entites.Category;
import com.AuctionApp.Auction.entites.Player;
import com.AuctionApp.Auction.entites.Team;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import com.AuctionApp.Auction.repositories.CategoryRepository;
import com.AuctionApp.Auction.repositories.PlayerRepository;
import com.AuctionApp.Auction.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
public class PanelController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private TeamRepository teamRepository;

    private List<Player> cateoryWisePlayer = new ArrayList<>();

    @GetMapping("/teams/{auctionId}")
    public List<Team> showTeams(@PathVariable String auctionId){
        return teamRepository.findTeams(auctionId);
    }

    @GetMapping("/categories/{auctionId}")
    public List<Category> showCategories(@PathVariable String auctionId){
        return categoryRepository.findCategoryByAuctionId(auctionId);
    }

    @PostMapping("/select-category")
    public void selectCategory(@RequestBody Map<String ,String> payload){
        cateoryWisePlayer = playerRepository.getPLayerCategoryWise(payload.get("categoryId"));
    }

    @GetMapping("/new-player")
    public Player getNewPlayer(){
        if(cateoryWisePlayer.size() > 0){
            return cateoryWisePlayer.get(0);
        }else if(playerRepository.findAll().size() > 0){
                return playerRepository.findAll().get(0);
        }
        else {
        throw  new CustomException("No player exists",HttpStatus.BAD_REQUEST,"player list empty");
        }
    }

    @PostMapping("/new-bid")
    public  void addPlayerToTeam(@RequestBody Bid soldBid){
        if(soldBid.getStatus()){
            Optional<Player> player = playerRepository.findById(soldBid.getPlayerId());
            Optional<Team> team = teamRepository.findById(soldBid.getBidderId());
            if(player.isPresent() && team.isPresent()){
                team.get().getTeamPlayers().add(player.get());
                teamRepository.save(team.get());

            }else{
                throw new CustomException("Something missing", HttpStatus.BAD_REQUEST,"Team/Player missing");
            }
        }
    }
}
