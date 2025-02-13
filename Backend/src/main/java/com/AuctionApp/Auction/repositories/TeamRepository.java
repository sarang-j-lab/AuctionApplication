package com.AuctionApp.Auction.repositories;

import com.AuctionApp.Auction.entites.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team,String> {

    @Query(value = "SELECT * FROM team_tbl WHERE at_fk = :auctionId",nativeQuery = true)
    List<Team> findTeams(@Param("auctionId") String auctionId);

}
