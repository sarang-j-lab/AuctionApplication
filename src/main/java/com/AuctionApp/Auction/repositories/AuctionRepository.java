package com.AuctionApp.Auction.repositories;

import com.AuctionApp.Auction.entites.Auction;
import com.AuctionApp.Auction.entites.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.stylesheets.LinkStyle;


import java.util.Date;
import java.util.List;


public interface AuctionRepository extends JpaRepository<Auction,Long> {

    List<Auction> findByAuctionIdIn(List<Long> auctionIds);

    @Modifying
    @Transactional
    @Query("UPDATE Auction a SET a.auctionName = :auctionName,  a.season = :season,a.auctionTime = :auctionTime,a.auctionDate = :auctionDate, a.pointsPerTeam = :pointsPerTeam,a.baseBid = :baseBid,a.bidIncreaseBy = :bidIncreaseBy,a.maxPlayerPerTeam = :maxPlayerPerTeam, a.minPlayerPerTeam = :minPlayerPerTeam WHERE a.auctionId = :auctionId")
    int updateAuction(@Param("auctionId") Long auctionId, @Param("auctionName") String auctionName, @Param("season") Integer season,
                      @Param("auctionTime") String auctionTime, @Param("auctionDate") Date auctionDate, @Param("pointsPerTeam") Long pointsPerTeam,
                      @Param("baseBid")Long baseBid, @Param("bidIncreaseBy")Long bidIncreaseBy, @Param("maxPlayerPerTeam")Integer maxPlayerPerTeam,
                      @Param("minPlayerPerTeam") Integer minPlayerPerTeam);





}
