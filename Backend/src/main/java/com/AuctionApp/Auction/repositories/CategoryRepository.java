package com.AuctionApp.Auction.repositories;

import com.AuctionApp.Auction.entites.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query(value = "select * from category_tbl where ac_fk = :auctionId", nativeQuery = true)
    public List<Category> findCategoryByAuctionId(@Param("auctionId") String auctionId);
}
