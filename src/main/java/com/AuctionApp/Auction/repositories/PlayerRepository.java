package com.AuctionApp.Auction.repositories;

import com.AuctionApp.Auction.Component.Style;
import com.AuctionApp.Auction.entites.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player,Long> {

    @Query(value = "select * from player_tbl where auction_id = :auctionId",nativeQuery = true)
    List<Player> findByAuction(@Param("auctionId") long auctionid);

    @Modifying
    @Transactional
    @Query( "UPDATE  Player p SET p.playerAge = :playerAge , p.playerName = :playerName ,p.mobileNo = :mobileNo ," +
                "p.playerStyle = :playerStyle ,p.tShirtSize = :tShirtSize ,p.jersseyName = :jersseyName ,p.jersseyNumber = :jersseyNumber, p.trouserSize = :trouserSize, p.details = :details WHERE p.playerId = :playerId")
    int updatePlayerById(@Param("playerId") long playerId,
                          @Param("playerAge") int playerAge,
                          @Param("playerName") String playerName,
                          @Param("mobileNo") String mobileNo,
                          @Param("playerStyle") Style playerStyle,
                          @Param("tShirtSize") String tShirtSize,
                          @Param("jersseyName") String jersseyName,
                          @Param("jersseyNumber") int jersseyNumber,
                          @Param("trouserSize") String trouserSize,
                          @Param("details") String details);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM player_tbl WHERE player_id = :playerId",nativeQuery = true )
    void deletePlayerByPlayerId(@Param("playerId") long playerId);
}
