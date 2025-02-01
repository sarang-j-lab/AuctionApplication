package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.DTO.UserDTO;
import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.entites.User;
import com.AuctionApp.Auction.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.AuctionApp.Auction.Config.AppUserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AppUserService userService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);

    private final Random random = new Random();
    private String generateId() {
        // Generate a random 7-digit number for the first part
        long firstPart = 1000000 + random.nextInt(9000000); // Ensures it's always 7 digits
        long secondPart = 10000 + random.nextInt(9000); // Ensures it's always 7 digits

        return firstPart+"-"+secondPart;
    }

    public Map<String,Object> create(UserDTO userRequest) {
        User userValidateWithMobileNo = userRepository.findByMobileNo(userRequest.getMobileNo());
        if(userValidateWithMobileNo != null){
            throw new CustomException("User with this mobile number is already exists",HttpStatus.BAD_REQUEST,"Please provide other phone number for signup");
        }
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User user = new User(generateId(),userRequest.getName(),userRequest.getEmail(),
                        userRequest.getPassword(), userRequest.getCity(),userRequest.getMobileNo());
         userRepository.save(user);

         Map<String,Object> response = new HashMap<>();

         response.put("token",jwtService.generateToken(user.getMobileNo()));
         response.put("user",user);


         return response;
    }

    public Map<String, Object> login(User user){
        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(user.getMobileNo(),user.getPassword()));

        Map<String,Object> response = new HashMap<>();
        User dbUser = userRepository.findByMobileNo(user.getMobileNo());

        response.put("token",jwtService.generateToken(user.getMobileNo()));
        response.put("user",dbUser);

        if(authentication.isAuthenticated()) return response;
        throw new CustomException("User not found with these credencials",HttpStatus.BAD_REQUEST,"Provide valid credencials");
    }


    public User getById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return user.get();
        }
        throw new CustomException("User not found",HttpStatus.BAD_REQUEST,"Usernot found");
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }



    public User update(String userId, UserDTO user) {
        User dbUser = getById(userId);
        if(dbUser != null){
            dbUser.setName(user.getName());
            dbUser.setEmail(user.getEmail());
            dbUser.setMobileNo(user.getMobileNo());
            dbUser.setCity(user.getCity());
            return userRepository.save(dbUser);
        }else{
            throw new CustomException("User not found with this id",HttpStatus.BAD_REQUEST,"User not found");
        }
    }

    public String delete(String userId){
        userRepository.deleteById(userId);
        return "User delete successfully";
    }

    public String changePassword(String userId,String oldPassword,String newPassword){
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()){
            if(user.get().getPassword().equals(oldPassword)){
                user.get().setPassword(newPassword);
                userRepository.save(user.get());
                return "Password changed successfully";
            }
            throw new CustomException("Password is incorrect",HttpStatus.BAD_REQUEST,"Wrong old password");
        }else{
            throw new CustomException("User not found",HttpStatus.BAD_REQUEST,"User not found");
        }
    }

    public User getUserByMobileNo(String mobileNO) {
        User user = userRepository.findByMobileNo(mobileNO);
        if(user != null){
            return  user;
        }
        throw new CustomException("User not found",HttpStatus.UNAUTHORIZED,"Please try again");
    }
}
