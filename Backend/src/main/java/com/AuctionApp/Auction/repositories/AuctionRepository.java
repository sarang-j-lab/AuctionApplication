package com.AuctionApp.Auction.repositories;

import com.AuctionApp.Auction.entites.Auction;
import com.AuctionApp.Auction.entites.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.stylesheets.LinkStyle;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public interface AuctionRepository extends JpaRepository<Auction,String> {

    List<Auction> findByAuctionIdIn(List<Long> auctionIds);

    @Modifying
    @Transactional
    @Query("UPDATE Auction a SET a.auctionName = :auctionName,  a.season = :season,a.auctionTime = :auctionTime,a.auctionDate = :auctionDate, a.bidIncreaseBy = :bidIncreaseBy, a.maxPlayerPerTeam = :maxPlayerPerTeam,a.playerRegistration = :playerRegistration WHERE a.auctionId = :auctionId")
    int updateAuction(@Param("auctionId") String auctionId, @Param("auctionName") String auctionName, @Param("season") int season,
                      @Param("auctionTime") String auctionTime, @Param("auctionDate") Date auctionDate,  @Param("bidIncreaseBy")int bidIncreaseBy,
                      @Param("maxPlayerPerTeam") int maxPlayerPerTeam ,@Param("playerRegistration") boolean playerRegitration );

    @Modifying
    @Transactional
    @Query(value = "delete from user_as_player_in_auction where auction_id = :auctionId", nativeQuery = true)
    void deleteUserAsAplayer(@Param("auctionId") String auctionId);
}
