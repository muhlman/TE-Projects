package com.techelevator.tenmo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountsDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.User;

@RequestMapping("/user")
@RestController
public class UserController {

    private UserDAO userDao;

    public UserController(UserDAO dao) {
        this.userDao = dao;

    }

    
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<User> list() throws UserNotFoundException {
        return userDao.findAll();
    }

    @RequestMapping(path = "/{username}", method = RequestMethod.GET)
    public User findByUsername(@PathVariable String username) throws UserNotFoundException {
        return userDao.findByUsername(username);
    }

    // Em added PathVariable
    @RequestMapping(path = "/{username}/findid", method = RequestMethod.GET)
    public int findIdByUsername(@PathVariable String username) throws UserNotFoundException {
        return userDao.findIdByUsername(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void create(@RequestParam String username, @RequestParam String password) {

    }

}
