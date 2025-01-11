package com.AuctionApp.Auction.repositories;

import com.AuctionApp.Auction.entites.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
