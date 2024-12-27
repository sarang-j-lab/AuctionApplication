package com.AuctionApp.Auction.Services;

import com.AuctionApp.Auction.DTO.UserRequest;
import com.AuctionApp.Auction.advise.CustomException;
import com.AuctionApp.Auction.entites.User;
import com.AuctionApp.Auction.repositories.UserRepository;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(UserRequest userRequest) {
        User validateWithMobileNo = userRepository.findByMobileNo(userRequest.getMobileNo());
        if(validateWithMobileNo != null){
            throw new UnexpectedTypeException("User with this mobile number is already exists");
        }
        User user = User.
                build(0,userRequest.getName(),userRequest.getEmail(),
                        userRequest.getPassword(), userRequest.getCity(),userRequest.getMobileNo());
        return userRepository.save(user);
    }

    public User login(User user){
        User dbUser = userRepository.findByMobileNo(user.getMobileNo());
        if(dbUser != null){
            if(dbUser.getPassword().equals(user.getPassword())){
                return dbUser;
            }
            throw new CustomException("Invalid details", HttpStatus.BAD_REQUEST,"Submit valid details for login to you account");
        }
        throw new UsernameNotFoundException("User not found with these credencials");
    }


    public User getById(long userId) {
        User user =userRepository.findByUserId(userId);
        if(user != null){
            return user;
        }
        throw new UsernameNotFoundException("User not found");
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User update(long userId,UserRequest user) {
        User dbUser = getById(userId);
        if(dbUser != null){
            dbUser.setName(user.getName());
            dbUser.setEmail(user.getEmail());
            dbUser.setMobileNo(user.getMobileNo());
            return userRepository.save(dbUser);
        }else{
            throw new UsernameNotFoundException("User not found with this id");
        }
    }

    public String delete(long userId){
        userRepository.deleteById(userId);
        return "User delete successfully";
    }

    public String changePassword(long userId,String oldPassword,String newPassword){
        User user = userRepository.findByUserId(userId);
        if(user.equals(null)){
            throw new UsernameNotFoundException("User not found");
        }
        if(user.getPassword().equals(oldPassword)){
            user.setPassword(newPassword);
            userRepository.save(user);
            return "Password changed successfully";
        }
        throw new CustomException("Password is incorrect",HttpStatus.BAD_REQUEST,"Wrong old password");
    }

}
