package com.persistence.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.persistence.common.PagingQuery;
import com.persistence.common.PagingResult;
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

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public User updateUser(@RequestBody User user) {
        LOG.debug("save user:{}", user.getLoginId());
        return this.userService.saveUser(user);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<User> getUserList() {
        return this.userService.getUserList();
    }

    @RequestMapping(value = "/search/prepare", method = RequestMethod.GET)
    public PagingResult searchUsersPrepare(PagingQuery pagingQuery) {
        return this.userService.searchUsersPrepare(pagingQuery);
    }

    @RequestMapping(value = "/search/query", method = RequestMethod.GET)
    public PagingResult searchUsersQuery(PagingQuery pagingQuery) {
        return this.userService.searchUsersQuery(pagingQuery);
    }

    @RequestMapping(value = "/search/ex", method = RequestMethod.GET)
    public PagingResult searchUsersEx(PagingQuery pagingQuery) {
        return this.userService.searchUsersEx(pagingQuery);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public int deleteUser(@RequestParam("id") Integer id) {
        return this.userService.deleteUser(id);
    }

}
