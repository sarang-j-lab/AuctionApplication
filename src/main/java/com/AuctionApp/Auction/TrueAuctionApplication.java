package com.AuctionApp.Auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
@EnableTransactionManagement
public class TrueAuctionApplication {

	public static void main(String[] args) throws IOException {



		SpringApplication.run(TrueAuctionApplication.class, args);
	}

}
