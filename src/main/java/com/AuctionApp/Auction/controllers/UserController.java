package com.AuctionApp.Auction.controllers;

import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.entites.User;
import com.AuctionApp.Auction.DTO.UserDTO;
import com.AuctionApp.Auction.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/my-profile/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable long userId){
        return ResponseEntity.ok(userService.getById(userId));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User userCred){
        return new ResponseEntity<>(userService.login(userCred),HttpStatus.OK);
    }

    @PostMapping("/user-registration")
    public ResponseEntity<User> createUser(@RequestBody @Valid UserDTO userRequest){
        return new ResponseEntity<>(userService.create(userRequest), HttpStatus.CREATED);
    }

    @PutMapping("/edit-profile/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody @Valid UserDTO user, @PathVariable long userId){
        return new ResponseEntity<>(userService.update(userId,user),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable long userId){
        return new ResponseEntity<>(userService.delete(userId),HttpStatus.OK);
    }

    @PutMapping("/change-password/{userId}")
        public ResponseEntity<String> changeUserPassword(@PathVariable long userId,@RequestBody Map<String, String> payload){
            String oldPassword =  payload.get("oldPassword");
            String newPassword =   payload.get("newPassword");
            if(oldPassword == null || newPassword == null){
                throw new CustomException("provide valid passwords",HttpStatus.BAD_REQUEST,"Old and new Password is required");
            }
            if(oldPassword.equals(newPassword)){
                throw new CustomException("Same passwords",HttpStatus.BAD_REQUEST,"Both new and old password is same");
            }
          return new ResponseEntity<>(userService.changePassword(userId,oldPassword,newPassword),HttpStatus.OK);
    }
}
