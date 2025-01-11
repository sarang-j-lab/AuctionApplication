package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.DTO.CategoryDTO;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.entites.Auction;
import com.AuctionApp.Auction.entites.Category;
import com.AuctionApp.Auction.repositories.AuctionRepository;
import com.AuctionApp.Auction.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    public List<Category> showCategories(long auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){
            return auction.get().getCategories();
        }
        throw new CustomException("Auction not found", HttpStatus.BAD_REQUEST,"auction not found with this id");
    }

    public Category create(CategoryDTO newCategory ,long auctionId){
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isPresent()){

        long reserve = auction.get().getReserve() - (auction.get().getBaseBid() * newCategory.getMinPlayerPerTeam()) ;
            reserve = reserve + (newCategory.getBaseBid() * newCategory.getMinPlayerPerTeam());


            Category category = new Category(UUID.randomUUID(),
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


    public String update(CategoryDTO category, UUID cateogryId){
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


    public String delete(UUID categoryId){
        categoryRepository.deleteById(categoryId);
        return "Category deleted successfully";
    }


}
