package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.Component.CategoryAdditionalIncrements;
import com.AuctionApp.Auction.entites.CategoryRequirements;
import com.AuctionApp.Auction.DTO.CategoryDTO;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.entites.Auction;
import com.AuctionApp.Auction.entites.Category;
import com.AuctionApp.Auction.entites.Player;
import com.AuctionApp.Auction.entites.Team;
import com.AuctionApp.Auction.repositories.*;
import com.AuctionApp.Auction.util.Generate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private PlayerRepository playerRepository;



    @Autowired TeamRepository teamRepository;

    private final Random random = new Random();
    private String generateId() {
        // Generate a random 7-digit number for the first part
        long firstPart = 1000000 + random.nextInt(9000000); // Ensures it's always 7 digits

        return "" + firstPart;
    }

    public List<Category> showCategories(String auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            return auction.get().getCategories();
        }
        throw new CustomException("Auction not found", HttpStatus.BAD_REQUEST,"auction not found with this id");
    }





    public Category create(CategoryDTO newCategory ,String auctionId){
        if(newCategory.getMaxPlayerPerTeam() < newCategory.getMinPlayerPerTeam()){
            throw new CustomException("Max player should be greather than min player",HttpStatus.BAD_REQUEST,"Please provide valid max player");
        }
        Optional<Auction> auction = auctionRepository.findById(auctionId);

        if(auction.isPresent()){
        List<Team> teams = teamService.auctionTeams(auctionId);

        if(auction.get().getNoneCategoryPlayerRequired() < newCategory.getMinPlayerPerTeam()){
            throw new CustomException("This category's minimum player count exceeds the auction's minimum player limit per team!",HttpStatus.BAD_REQUEST,"Correct it");
        }

//        if(auction.get().getReserve() < newCategory.getBaseBid() * newCategory.getMaxPlayerPerTeam()){
//            throw new CustomException("The maximum number of players in this category is not desirable at this base bid!",HttpStatus.BAD_REQUEST,"not allowed");
//        }

        int reserve = auction.get().getReserve() - (auction.get().getBaseBid() * newCategory.getMinPlayerPerTeam()) ;
            reserve = reserve + (newCategory.getBaseBid() * newCategory.getMinPlayerPerTeam());

            if(reserve > auction.get().getPointsPerTeam()){
                throw new CustomException("The base bid or minimum player requirement for this category does not align with the team's points.",HttpStatus.BAD_REQUEST,"Please enter valid base bid or min player");
            }

            Category category = new Category(generateId(),
                    newCategory.getCategoryName(),
                    newCategory.getMaxPlayerPerTeam(),
                    newCategory.getMinPlayerPerTeam(),
                    newCategory.getBaseBid(),
                    newCategory.getIncrement());
            Category savedCategory =  categoryRepository.save(category);


            for(Team team: teams){
                team.setMaxBid(team.getTotalPoints() - reserve);
                team.setReserve(reserve);
                team.setNoneCategoryPlayerReserve((auction.get().getNoneCategoryPlayerRequired() - category.getMinPlayerPerTeam()) * auction.get().getBaseBid());
                CategoryRequirements categoryRequirement = new CategoryRequirements(savedCategory.getCategoryId(),0,category.getMinPlayerPerTeam(),category.getMaxPlayerPerTeam(),savedCategory.getBaseBid() * savedCategory.getMinPlayerPerTeam());
                team.getPlayerRequirement().add(categoryRequirement);
                teamRepository.save(team);
            }


            auction.get().setNoneCategoryPlayerRequired(auction.get().getNoneCategoryPlayerRequired() - category.getMinPlayerPerTeam());
            auction.get().getCategories().add(savedCategory);
            auction.get().setReserve(reserve);
            auctionRepository.save(auction.get());
            return category;
        }
        throw new CustomException("Auction not found", HttpStatus.BAD_REQUEST,"auction not found with this id");
    }


    public String update(CategoryDTO category, String cateogryId){
        Optional<Category> dbCategory = categoryRepository.findById(cateogryId);
        if(dbCategory.isPresent()){
            dbCategory.get().setCategoryName(category.getCategoryName());
            dbCategory.get().setIncrement(category.getIncrement());
            dbCategory.get().setMaxPlayerPerTeam(category.getMaxPlayerPerTeam());
            categoryRepository.save(dbCategory.get());
            return "Category edited successfully";
        }
        throw new CustomException("Category not found",HttpStatus.BAD_REQUEST,"Category not found with this ID");
    }


    public String delete(String categoryId,String auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(auction.isPresent() && category.isPresent()){
            List<Team> teams = teamService.auctionTeams(auctionId);

             List<Player> categoryPlayers = category.get().getPlayers();
             for (Player player: categoryPlayers){
                 player.setCategoryId(null);
                 playerRepository.save(player);
             }

            int reserve = auction.get().getReserve() - (category.get().getMinPlayerPerTeam() * category.get().getBaseBid());
            reserve = reserve + (auction.get().getBaseBid() * category.get().getMinPlayerPerTeam());
            for(Team team: teams){
                team.setMaxBid(team.getTotalPoints() - reserve);

                team.setReserve(reserve);
                team.setNoneCategoryPlayerReserve((auction.get().getNoneCategoryPlayerRequired()  + category.get().getMinPlayerPerTeam()) * auction.get().getBaseBid() );
                List<CategoryRequirements> cateReqArr = team.getPlayerRequirement();
                for(CategoryRequirements categoryRequirements : cateReqArr){
                    if(categoryRequirements.getCategory().equals(category.get().getCategoryId())){
                        team.getPlayerRequirement().remove(categoryRequirements);
                        break;
                    }
                }
                teamRepository.save(team);
            }

            auction.get().setNoneCategoryPlayerRequired(auction.get().getNoneCategoryPlayerRequired() + category.get().getMinPlayerPerTeam());
            auction.get().setReserve(reserve);
            auctionRepository.save(auction.get());
            categoryRepository.deleteById(categoryId);
            return "Category deleted successfully";
        }
        throw new CustomException("Invalid credentials",HttpStatus.BAD_REQUEST,"Invalid credentials");
    }


    public String addIncrements(CategoryAdditionalIncrements increments, String categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isPresent()){
            increments.setId(generateId());
            category.get().getCategoryAdditionalIncrements().add(increments);
            categoryRepository.save(category.get());
            return "Category added successfully";
        }else{
        return "Category not found";
        }
    }

    public String deleteIncrement(String categoryId, String incrementId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isPresent()){
            for (CategoryAdditionalIncrements increments :category.get().getCategoryAdditionalIncrements()){
                if(increments.getId().equals(incrementId)){
                    category.get().getCategoryAdditionalIncrements().remove(increments);
                    categoryRepository.save(category.get());
                    return "increment delete successfully";
                }
            }
            return "increment delete successfully";
        }else {
            throw new CustomException("category not found",HttpStatus.BAD_REQUEST,"category not found with this id");
        }
    }


    public List<Player> showPlayers(String categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isPresent()){
            return category.get().getPlayers();
        }
        throw new CustomException("Category not found",HttpStatus.BAD_REQUEST,"Category not found");
    }
}
