package com.AuctionApp.Auction.Config;

import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.Services.UserPrinciples;
import com.AuctionApp.Auction.entites.User;
import com.AuctionApp.Auction.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mobileNo) throws UsernameNotFoundException {

        User user = userRepository.findByMobileNo(mobileNo);

        if(user == null){
            throw new CustomException("User not found with this mobile no.", HttpStatus.BAD_REQUEST,"Please provide valid details");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

        return new UserPrinciples(user,authority);
    }
}
