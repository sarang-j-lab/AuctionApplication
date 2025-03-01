package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.Component.CategoryRequirements;
import com.AuctionApp.Auction.Component.Status;
import com.AuctionApp.Auction.DTO.BidDTO;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.entites.*;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import com.AuctionApp.Auction.repositories.CategoryRepository;
import com.AuctionApp.Auction.repositories.PlayerRepository;
import com.AuctionApp.Auction.repositories.TeamRepository;
import com.AuctionApp.Auction.util.Generate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PanelService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BidService bidService;


    public String onNoneCategorySold(BidDTO bidDTO ,String auctionId){
        Optional<Player> playerOpt = playerRepository.findById(bidDTO.getPlayer());
        Optional<Team> teamOpt = teamRepository.findById(bidDTO.getTeam());
        Optional<Auction> auctionOpt = auctionRepository.findById(auctionId);

        if(bidDTO.getCategory() != null || teamOpt.isEmpty() || playerOpt.isEmpty() || auctionOpt.isEmpty()){
            throw new CustomException("Something went wrong please try again!", HttpStatus.BAD_REQUEST,"Error");
        }

        Player player = playerOpt.get();
        Team team = teamOpt.get();
        Auction auction = auctionOpt.get();

        team.setTotalPoints(team.getTotalPoints() - bidDTO.getAmount());
        team.setNoneCategoryPlayerBought(team.getNoneCategoryPlayerBought() + 1);

        if(team.getNoneCategoryPlayerBought() <= auction.getNoneCategoryPlayerRequired()){
            int baseBid = auction.getBaseBid();
            team.setReserve(team.getReserve() - baseBid);
            team.setNoneCategoryPlayerReserve(team.getNoneCategoryPlayerReserve() - baseBid);
        }
        team.setMaxBid(team.getTotalPoints() - team.getReserve());

        player.setStatus(Status.SOLD);
        Bid savedBid = bidService.saveBid(new Bid(Generate.generateId(),player.getPlayerId(),team.getTeamId(),bidDTO.getAmount(),null));
        player.setBid(savedBid);
        playerRepository.save(player);

        team.getTeamPlayers().add(player);
        teamRepository.save(team);

        return "Player sold successfully";
    }

    public String categorySold(BidDTO bidDTO,String auctionId){
        Optional<Category> categoryOpt = categoryRepository.findById(bidDTO.getCategory());
        Optional<Team> teamOpt = teamRepository.findById(bidDTO.getTeam());
        Optional<Player> playerOpt = playerRepository.findById(bidDTO.getPlayer());

        if (categoryOpt.isEmpty() || teamOpt.isEmpty() || playerOpt.isEmpty()) {
            throw new CustomException("Something went wrong please try again", HttpStatus.BAD_REQUEST, "Error");
        }

        Category category = categoryOpt.get();
        Team team = teamOpt.get();
        Player player = playerOpt.get();

        CategoryRequirements categoryRequirement = team.getPlayerRequirement().stream()
                .filter(req -> req.getCategory().equals(bidDTO.getCategory()))
                .findFirst()
                .orElseThrow(() -> new CustomException("Category requirement not found", HttpStatus.BAD_REQUEST, "Error"));

        categoryRequirement.setBought(categoryRequirement.getBought() + 1);
        team.setTotalPoints(team.getTotalPoints() - bidDTO.getAmount());

        if (categoryRequirement.getBought() <= categoryRequirement.getPlayerRequired()) {
            int baseBid = category.getBaseBid();
            categoryRequirement.setReserve(categoryRequirement.getReserve() - baseBid);
            team.setReserve(team.getReserve() - baseBid);
        }

        team.setMaxBid(team.getTotalPoints() - team.getReserve());

        Bid savedBid = bidService.saveBid(new Bid(Generate.generateId(), player.getPlayerId(), team.getTeamId(), bidDTO.getAmount(), bidDTO.getCategory()));
        player.setStatus(Status.SOLD);
        player.setBid(savedBid);
        playerRepository.save(player);

        team.getTeamPlayers().add(player);
        teamRepository.save(team);

        return "Player sold successfully";
    }


    public String reauctionNoneCategoryPlayer(Bid bid,String auctionId){
        Optional<Player> playerOpt = playerRepository.findById(bid.getPlayer());
        Optional<Team> teamOpt = teamRepository.findById(bid.getTeam());
        Optional<Auction> auctionOpt = auctionRepository.findById(auctionId);

        if (playerOpt.isEmpty() || teamOpt.isEmpty() || auctionOpt.isEmpty()) {
            throw new CustomException("Something went wrong please try again", HttpStatus.BAD_REQUEST, "Missing credentials");
        }

        Player player = playerOpt.get();
        Team team = teamOpt.get();
        Auction auction = auctionOpt.get();

        team.setNoneCategoryPlayerBought(team.getNoneCategoryPlayerBought() - 1);
        team.getTeamPlayers().remove(player);
        team.setTotalPoints(team.getTotalPoints() + bid.getAmount());

        if (team.getNoneCategoryPlayerBought() < auction.getNoneCategoryPlayerRequired()) {
            int baseBid = auction.getBaseBid();
            team.setReserve(team.getReserve() + baseBid);
            team.setNoneCategoryPlayerReserve(team.getNoneCategoryPlayerReserve() + baseBid);
        }

        team.setMaxBid(team.getTotalPoints() - team.getReserve());
        player.setStatus(Status.PENDING);
        player.setBid(null);

        bidService.deleteBid(bid.getId());
        teamRepository.save(team);
        playerRepository.save(player);
        return "Player can be re-auctioned now";
    }

    public String reauctionCategoryPlayer(Bid bid){
        Optional<Player> playerOpt = playerRepository.findById(bid.getPlayer());
        Optional<Team> teamOpt = teamRepository.findById(bid.getTeam());
        Optional<Category> categoryOpt = categoryRepository.findById(bid.getCategory());

        if (playerOpt.isEmpty() || teamOpt.isEmpty() || categoryOpt.isEmpty()) {
            throw new CustomException("Something went wrong please try again!!", HttpStatus.BAD_REQUEST, "error");
        }

        Player player = playerOpt.get();
        Team team = teamOpt.get();
        Category category = categoryOpt.get();

        CategoryRequirements categoryRequirement = team.getPlayerRequirement().stream()
                .filter(req -> req.getCategory().equals(bid.getCategory()))
                .findFirst()
                .orElseThrow(() -> new CustomException("Category not found!", HttpStatus.BAD_REQUEST, "error"));

        categoryRequirement.setBought(categoryRequirement.getBought() - 1);
        team.getTeamPlayers().remove(player);
        team.setTotalPoints(team.getTotalPoints() + bid.getAmount());

        if (categoryRequirement.getBought() < categoryRequirement.getPlayerRequired()) {
            int baseBid = category.getBaseBid();
            categoryRequirement.setReserve(categoryRequirement.getReserve() + baseBid);
            team.setReserve(team.getReserve() + baseBid);
        }

        team.setMaxBid(team.getTotalPoints() - team.getReserve());
        player.setStatus(Status.PENDING);
        player.setBid(null);

        bidService.deleteBid(bid.getId());
        teamRepository.save(team);
        playerRepository.save(player);
        return "Player can be re-auctioned now!";
    }


    public String addToUnsold(Player player,String auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isEmpty() && player == null){
            throw new CustomException("Something went wrong please try again",HttpStatus.BAD_REQUEST,"Try again");
        }
        player.setStatus(Status.UNSOLD);
        playerRepository.save(player);
        auction.get().getUnsoldPlayers().add(player);
        auctionRepository.save(auction.get());
        return "Player remain unsold successfully";
    }

    public String removeFromUnsold(String auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if (auction.isEmpty()) {
            throw new CustomException("Auction not found", HttpStatus.BAD_REQUEST, "Auction not found");
        }
        for (Player player : auction.get().getUnsoldPlayers()) {
            player.setStatus(Status.PENDING);
            playerRepository.save((player));
        }
        auction.get().getUnsoldPlayers().clear();
        auctionRepository.save(auction.get());
        return "Unsold player added to Reauction!";
    }

}
