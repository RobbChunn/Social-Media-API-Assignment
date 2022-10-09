package com.cooksystems.team1.Controller;

import com.cooksystems.team1.dtos.UserRequestDto;
import com.cooksystems.team1.dtos.UserResponseDto;
import com.cooksystems.team1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;


    @GetMapping
    public List<UserResponseDto> getAllUsers() {

        return userService.getAllUsers();
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto){

        return userService.createUser(userRequestDto);
    }

    @DeleteMapping("/@{username}")
    public UserResponseDto deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }

    @GetMapping("/@{username}/following")
    public List<UserResponseDto> getUserFollowing(@PathVariable String username){
        return userService.getUserFollowing(username);
    }

    @GetMapping("/@{username}/followers")
    public List<UserResponseDto> getUserFollowers(@PathVariable String username){
        return userService.getUserFollowers(username);
    }
}
