package com.AuctionApp.Auction.util;

import java.util.Random;


public class Generate {

    public  static  String generateId() {
        // Generate a random 7-digit number for the first part
        Random random = new Random();
        long firstPart = 1000000 + random.nextInt(9000000); // Ensures it's always 7 digits
        long secondPart = 1000 + random.nextInt(9000); // Ensures it's always 7 digits

        return firstPart+"-"+secondPart;
    }

}
