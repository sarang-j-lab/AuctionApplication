package com.AuctionApp.Auction.repositories;

import com.AuctionApp.Auction.Component.Style;
import com.AuctionApp.Auction.entites.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player,String> {

    @Query(value = "select * from player_tbl where auction_id = :auctionId",nativeQuery = true)
    List<Player> findByAuction(@Param("auctionId") String auctionid);

    @Modifying
    @Transactional
    @Query( "UPDATE  Player p SET p.playerAge = :playerAge , p.playerName = :playerName ,p.mobileNo = :mobileNo ," +
                "p.playerStyle = :playerStyle ,p.tShirtSize = :tShirtSize ,p.jersseyName = :jersseyName ,p.jersseyNumber = :jersseyNumber, p.trouserSize = :trouserSize WHERE p.playerId = :playerId")
    int updatePlayerById(@Param("playerId") String playerId,
                          @Param("playerAge") int playerAge,
                          @Param("playerName") String playerName,
                          @Param("mobileNo") String mobileNo,
                          @Param("playerStyle") Style playerStyle,
                          @Param("tShirtSize") String tShirtSize,
                          @Param("jersseyName") String jersseyName,
                          @Param("jersseyNumber") int jersseyNumber,
                          @Param("trouserSize") String trouserSize);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM auction_player_tbl WHERE player_id = :playerId",nativeQuery = true )
    void deletePlayerFromAuction(@Param("playerId") String playerId);

    @Query(value = "select * from player_tbl where cp_fk = :categoryId",nativeQuery = true)
    List<Player> getPLayerCategoryWise(@Param("categoryId") String categoryId);

    @Query(value = "select * from player_tbl where ap_fk = :auctionId order by form_no asc",nativeQuery = true)
    List<Player> getSortedPlayersByFormNo(@Param("auctionId") String auctionId);


}
