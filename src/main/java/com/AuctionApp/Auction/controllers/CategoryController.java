package com.AuctionApp.Auction.controllers;

import com.AuctionApp.Auction.Component.AdditinalIncrements;
import com.AuctionApp.Auction.Component.CategoryAdditionalIncrements;
import com.AuctionApp.Auction.DTO.CategoryDTO;
import com.AuctionApp.Auction.Services.CategoryService;
import com.AuctionApp.Auction.entites.Category;
import com.AuctionApp.Auction.entites.Player;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/show-auction-category/{auctionId}")
    public List<Category> showAuctionCategory( @PathVariable String auctionId){
        return categoryService.showCategories(auctionId);
    }

    @GetMapping("/show-category-players/{categoryId}")
    public List<Player> showCategoryPlayer(@PathVariable String categoryId){
        return categoryService.showPlayers(categoryId);
    }

    @PostMapping("/create-auction-category/{auctionId}")
    public Category createCategory(@RequestBody @Valid CategoryDTO category, @PathVariable String auctionId){
        return categoryService.create(category, auctionId);
    }

    @PutMapping("/update-auction-category/{categoryId}")
    public String editCategory(@RequestBody @Valid CategoryDTO categoryDTO, @PathVariable String categoryId){
        return categoryService.update(categoryDTO,categoryId);
    }

    @DeleteMapping("/delete-auction-category/{categoryId}")
    public String deleteCategory(@PathVariable String categoryId){
        return categoryService.delete(categoryId);
    }



    @PostMapping("/add-category-increments/{categoryId}")
    public ResponseEntity<String> additionalIncrements(@RequestBody @Valid CategoryAdditionalIncrements increments , @PathVariable String categoryId){
        return new ResponseEntity<>(categoryService.addIncrements(increments,categoryId), HttpStatus.CREATED);
    }


    @DeleteMapping("/delete-category-increment/{categoryId}/{incrementId}")
    public ResponseEntity<String> deleteIncrement(@PathVariable String categoryId,@PathVariable String incrementId){
        return new ResponseEntity<>(categoryService.deleteIncrement(categoryId,incrementId),HttpStatus.OK);
    }
}
