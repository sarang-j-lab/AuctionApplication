package com.AuctionApp.Auction.repositories;

import com.AuctionApp.Auction.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserId(long id);

    User findByMobileNo(String mobileNo);
}
