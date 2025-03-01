package com.AuctionApp.Auction;

import com.AuctionApp.Auction.Component.Role;
import com.AuctionApp.Auction.Services.UserService;
import com.AuctionApp.Auction.entites.User;
import com.AuctionApp.Auction.repositories.UserRepository;
import com.AuctionApp.Auction.util.Generate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;


@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories
public class TrueAuctionApplication implements CommandLineRunner {



	public static void main(String[] args) throws IOException {
		SpringApplication.run(TrueAuctionApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;


	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);


	@Override
	public void run(String... args) throws Exception {
		User user = userRepository.findByRole(Role.ADMIN);
		if(user == null){
			User  adminUser = new User(Generate.generateId(),"kedar","kedarpande@gmail.com","$2y$04$NbRNQvA6e9cehSWCoy2F8uEwgZiI5e95TlFaUFdfKCG2L0EI.fD9e","sambhaji nagar","9272222200",Role.ADMIN);
			userRepository.save(adminUser);
		}
	}
}
