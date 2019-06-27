package com.persistence.mybatis;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

/**
 * https://blog.csdn.net/xiaokang123456kao/article/details/76228684
 */
public class BaseDAO<E, X, M> extends SqlSessionDaoSupport {

    private Class<E> entityClass;
    private Class<X> entityExampleClass;
    private Class<M> entityMapperClass;

    public BaseDAO() {
        java.lang.reflect.Type[] parameterizedType = getParameterizedTypes();
        entityClass = (Class<E>)parameterizedType[0];
        entityExampleClass = (Class<X>)parameterizedType[1];
        entityMapperClass = (Class<M>)parameterizedType[2];
    }

    private java.lang.reflect.Type[] getParameterizedTypes() {
        java.lang.reflect.Type type = getClass().getGenericSuperclass();
        return ((ParameterizedType)type).getActualTypeArguments();
    }

    private M getEntityMapperProxy() {
        return this.getSqlSession().getMapper(entityMapperClass);
    }

    public int insertSelective(E record) {
        M entityMapper = getEntityMapperProxy();
        Method method = ReflectionUtils.findMethod(entityMapperClass, "insertSelective", entityClass);
        return (int)ReflectionUtils.invokeMethod(method, entityMapper, record);
    }

    @Override
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }
}
