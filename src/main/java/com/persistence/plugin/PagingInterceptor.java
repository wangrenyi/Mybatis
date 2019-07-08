package com.persistence.plugin;

import java.sql.Connection;
import java.util.Optional;
import java.util.Properties;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.persistence.common.PagingQuery;
import com.persistence.util.ReflectionUtil;

/**
 * refer to: https://elim168.blog.csdn.net/article/details/40737009 
 */

@Intercepts(
    value = {@Signature(args = {Connection.class, Integer.class}, method = "prepare", type = StatementHandler.class)})
public class PagingInterceptor extends AbstractInterceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler handler = (RoutingStatementHandler)invocation.getTarget();
        StatementHandler delegate = (StatementHandler)ReflectionUtil.getValueByFieldName(handler, "delegate");

        BoundSql boundSql = delegate.getBoundSql();
        Object paramObject = boundSql.getParameterObject();

        if (paramObject == null || !(paramObject instanceof PagingQuery)) {
            return invocation.proceed();
        }

        MappedStatement mappedStatement
            = (MappedStatement)ReflectionUtil.getValueByFieldName(delegate, "mappedStatement");

        if (mappedStatement.getSqlCommandType() != SqlCommandType.SELECT) {
            return invocation.proceed();
        }

        PagingQuery PagingQuery = (PagingQuery)paramObject;
        Integer pageIndex = PagingQuery.getPageIndex();
        Integer pageSize = PagingQuery.getPageSize();

        if (pageIndex == null || pageIndex < 0 || pageSize == null || pageSize <= 0) {
            return invocation.proceed();
        }

        StringBuffer sb = new StringBuffer();
        int offset = Optional.of(pageIndex).filter(p -> p > 0).map(mapper -> mapper * pageSize).orElse(0);

        String originalSql = boundSql.getSql().trim();
        String newSql = sb.append(originalSql).append(" limit ").append(offset).append(",").append(pageSize).toString();
        ReflectionUtil.setValueByFieldName(boundSql, "sql", newSql);

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {}

}
