package com.persistence.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.persistence.common.PagingQuery;
import com.persistence.common.PagingResult;
import com.persistence.dao.UserDAO;
import com.persistence.dao.UserExtDAO;
import com.persistence.table.User;
import com.persistence.table.UserExample;

@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserExtDAO userExtDAO;

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
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        Optional.ofNullable(pagingQuery.getCriteria()).map(mapper -> {
            mapper.forEach((key, value) -> {
                mapper.put(key, "%" + value + "%");
            });
            return mapper;
        }).ifPresent(consumer -> criteria.andAnyLike(consumer));;

        // return this.userDAO.pagingByExample(example, pagingQuery.getPageIndex(), pagingQuery.getPageSize());
        List<User> users = this.userExtDAO.pageUserList(pagingQuery);
        PagingResult result = new PagingResult();
        result.setDetails(users);
        return result;
    }

    public int deleteUser(Integer id) {
        return this.userDAO.deleteByPrimaryKey(id);
    }
}
