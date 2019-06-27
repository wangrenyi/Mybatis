package com.persistence.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.persistence.service.UserService;
import com.persistence.table.User;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private static Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public User saveUser(@RequestBody User user) {
        LOG.debug("save user:{}", user.getLoginId());
        return this.userService.saveUser(user);
    }

}
