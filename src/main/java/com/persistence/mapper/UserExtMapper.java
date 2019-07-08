package com.persistence.mapper;

import java.util.List;

import com.persistence.common.PagingQuery;
import com.persistence.table.User;

public interface UserExtMapper {

    List<User> pageUserList(PagingQuery pagingQuery);
}
