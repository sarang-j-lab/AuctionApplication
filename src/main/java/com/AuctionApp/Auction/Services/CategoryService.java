package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.Component.AdditinalIncrements;
import com.AuctionApp.Auction.Component.CategoryAdditionalIncrements;
import com.AuctionApp.Auction.DTO.CategoryDTO;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.entites.Auction;
import com.AuctionApp.Auction.entites.Category;
import com.AuctionApp.Auction.entites.Player;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import com.AuctionApp.Auction.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuctionRepository auctionRepository;

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
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){

        long reserve = auction.get().getReserve() - (auction.get().getBaseBid() * newCategory.getMinPlayerPerTeam()) ;
            reserve = reserve + (newCategory.getBaseBid() * newCategory.getMinPlayerPerTeam());


            Category category = new Category(generateId(),
                    newCategory.getCategoryName(),
                    newCategory.getMaxPlayerPerTeam(),
                    newCategory.getMinPlayerPerTeam(),
                    newCategory.getBaseBid(),
                    newCategory.getIncrement());
            Category savedCategory =  categoryRepository.save(category);
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
            dbCategory.get().setBaseBid(category.getBaseBid());
            dbCategory.get().setMaxPlayerPerTeam(category.getMaxPlayerPerTeam());
            dbCategory.get().setMinPlayerPerTeam(category.getMinPlayerPerTeam());
            categoryRepository.save(dbCategory.get());
            return "Category edited successfully";
        }
        throw new CustomException("Category not found",HttpStatus.BAD_REQUEST,"Category not found with this ID");
    }


    public String delete(String categoryId){
        categoryRepository.deleteById(categoryId);
        return "Category deleted successfully";
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
