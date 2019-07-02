package com.persistence.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.persistence.common.PagingQuery;
import com.persistence.common.PagingResult;
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

    @Transactional
    public User updateUser(User user) {
        user.setUpdateTime(new Date());
        user.setUpdateUser("test");
        this.userDAO.updateByPrimaryKeySelective(user);
        return user;
    }

    public List<User> getUserList() {
        return this.userDAO.selectByExample(null);
    }

    public PagingResult searchUsers(PagingQuery pagingQuery) {
        // this.userDAO.selectByExample(example);
        return null;
    }

    public int deleteUser(Integer id) {
        return this.userDAO.deleteByPrimaryKey(id);
    }
}
