package com.persistence.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.persistence.dao.UserDAO;
import com.persistence.table.User;

@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Transactional
    public User saveUser(User user) {
        user.setCreateTime(new Date());
        user.setCreateUser("test");
        user.setUpdateTime(new Date());
        user.setUpdateUser("test");
        user.setStatus(1);
        user.setVersion(1);
        userDAO.insertSelective(user);
        return user;
    }
}
