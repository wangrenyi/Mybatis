package com.persistence.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.persistence.common.PagingQuery;
import com.persistence.mapper.UserExtMapper;
import com.persistence.mybatis.BaseDAO;
import com.persistence.table.User;
import com.persistence.table.UserExample;

@Repository
public class UserExtDAO extends BaseDAO<User, UserExample, UserExtMapper> {

    @Autowired
    private UserExtMapper userExtMapper;

    public List<User> pageUserList(PagingQuery pagingQuery) {
        return userExtMapper.pageUserList(pagingQuery);
    }
}
