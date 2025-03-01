package com.AuctionApp.Auction.repositories;

import com.AuctionApp.Auction.Component.Role;
import com.AuctionApp.Auction.entites.User;
import jakarta.validation.constraints.Past;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {



    @Transactional
    User findByMobileNo(String mobileNo);

    @Transactional
    @Query(value = "SELECT u FROM User u  WHERE u.role = :role")
    User findByRole(@Param("role") Role role);
}
