package com.cooksystems.team1.services.impl;

import com.cooksystems.team1.dtos.UserRequestDto;
import com.cooksystems.team1.dtos.UserResponseDto;
import com.cooksystems.team1.entities.Credentials;
import com.cooksystems.team1.entities.User;
import com.cooksystems.team1.exceptions.BadRequestException;
import com.cooksystems.team1.exceptions.NotAuthorizedException;
import com.cooksystems.team1.exceptions.NotFoundException;
import com.cooksystems.team1.mappers.TweetMapper;
import com.cooksystems.team1.mappers.UserMapper;
import com.cooksystems.team1.repositories.HashtagRepository;
import com.cooksystems.team1.repositories.TweetRepository;
import com.cooksystems.team1.repositories.UserRepository;
import com.cooksystems.team1.services.UserService;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private TweetRepository tweetRepository;
    private TweetMapper tweetMapper;
    private HashtagRepository hashtagRepository;


    @Override
    public List<UserResponseDto> getAllUsers() {

        return userMapper.entitiesToDtos(userRepository.findByDeletedFalse());
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User newUser = userMapper.dtoToEntity(userRequestDto);
        userRepository.saveAndFlush(newUser);
        if(newUser == null){
            throw new NotAuthorizedException("You must use valid credentials");
        }else{
            return userMapper.entityToDto(newUser);
        }
    }

    @Override
    public UserResponseDto deleteUser(Long id) {
        Optional<User> toDelete = userRepository.findById(id);
        if(toDelete.isPresent()){
            User user = toDelete.get();
            userRepository.delete(user);
            return userMapper.entityToDto(user);
        }else{
            throw new NotFoundException("Username not found in database");
        }
    }

    @Override
    public List<UserResponseDto> getUserFollowing(String username) {
        List<User> incomingUser = userRepository.findByCredentialsUsername(username);
        if(incomingUser.isEmpty()){
            throw new BadRequestException("User not found with username: " + username );
        }
        User user = incomingUser.get(0);
        List<User> following = user.getFollowing();

        for(User u : following){
            if(u.isDeleted() || u == null){
                following.remove(u);
            }
            else{
                throw new BadRequestException("User does not exist");
            }
        }
        return userMapper.entitiesToDtos(following);
    }

    @Override
    public List<UserResponseDto> getUserFollowers(String username) {
        List<User> incomingUser = userRepository.findByCredentialsUsername(username);
        if(incomingUser.isEmpty()){
            throw new BadRequestException("User not found with username: " + username );
        }
        User user = incomingUser.get(0);
        List<User> followers = user.getFollowers();

        for(User u : followers){
            if(u.isDeleted() || u == null){
                followers.remove(u);
            }
            else{
                throw new BadRequestException("User does not exist");
            }
        }
        return userMapper.entitiesToDtos(followers);
    }


}
