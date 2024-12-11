package com.yuki.Rest.Controller;

import com.yuki.dto.ChangePasswordDTO;
import com.yuki.dto.UserDto;
import com.yuki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yuki.entity.User;
import com.yuki.repositoty.UserDAO;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest")
public class UserRestController {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public List<User> user() {
        List<User> users = userDAO.findAll();
        users.forEach(user -> user.setUserSubscription(user.getuserSubscription()));
        return users;
    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    // API Create Users
    @PostMapping("/user")

    public User userCreate(@RequestBody User user) {
        return this.userDAO.save(user);
    }

    // API Update
    @PutMapping("/user/{userID}")
    public User updateUser(@PathVariable int userID, @RequestBody User user) {
        user.setUserID(userID);
        return userDAO.save(user);
    }

    // API get User by ID
    @PutMapping("/users" +
            "/updateProfile/{userID}")
    public ResponseEntity<User> updateProfile(@PathVariable int userID, @RequestBody UserDto userDto) {
        try {
            User updatedUser = userService.updateUserDetails(userID, userDto.isGender(), userDto.getFullName(), userDto.getUserImage(), userDto.getBirthday());
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userDAO.findByUsername(username);
        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setUserSubscription(foundUser.getuserSubscription());
            return ResponseEntity.ok(foundUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/users/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        boolean isPasswordChanged = userService.changePassword(changePasswordDTO);
        if (isPasswordChanged) {
            return ResponseEntity.ok("Password changed successfully");
        } else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    // API Delete
    @DeleteMapping("/user/{userID}")
    public void deleteUser(@PathVariable int userID) {
        userDAO.deleteById(userID);
    }
}
