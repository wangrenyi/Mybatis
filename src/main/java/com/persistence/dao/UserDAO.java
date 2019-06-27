package com.persistence.dao;

import org.springframework.stereotype.Repository;

import com.persistence.mapper.UserMapper;
import com.persistence.mybatis.BaseDAO;
import com.persistence.table.User;
import com.persistence.table.UserExample;

@Repository
public class UserDAO extends BaseDAO<User, UserExample, UserMapper> {

}
