package com.AuctionApp.Auction.repositories;

import com.AuctionApp.Auction.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserId(long id);

    User findByMobileNo(String mobileNo);




}
