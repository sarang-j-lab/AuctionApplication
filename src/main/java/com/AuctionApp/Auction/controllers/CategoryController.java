package com.AuctionApp.Auction.controllers;

import com.AuctionApp.Auction.DTO.CategoryDTO;
import com.AuctionApp.Auction.Services.CategoryService;
import com.AuctionApp.Auction.entites.Category;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/show-auction-category/{auctionId}")
    public List<Category> showAuctionCategory( @PathVariable long auctionId){
        return categoryService.showCategories(auctionId);
    }

    @PostMapping("/create-auction-category/{auctionId}")
    public Category createCategory(@RequestBody @Valid CategoryDTO category, @PathVariable long auctionId){
        return categoryService.create(category, auctionId);
    }

    @PutMapping("/update-auction-category/{categoryId}")
    public String editCategory(@RequestBody @Valid CategoryDTO categoryDTO, @PathVariable UUID categoryId){
        return categoryService.update(categoryDTO,categoryId);
    }

    @DeleteMapping("/delete-auction-category/{categoryId}")
    public String deleteCategory(@PathVariable UUID categoryId){
        return categoryService.delete(categoryId);
    }
}
