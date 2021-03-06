package com.persistence.mybatis;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import com.persistence.common.PagingResult;
import com.persistence.generator.PagingExamplePlugin;
import com.persistence.plugin.PagingQueryInterceptor;

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

    public long countByExample(X example) {
        M entityMapper = getEntityMapperProxy();
        Method method = ReflectionUtils.findMethod(entityMapperClass, "countByExample", entityExampleClass);
        return (long)ReflectionUtils.invokeMethod(method, entityMapper, example);
    }

    public int deleteByExample(X example) {
        M entityMapper = getEntityMapperProxy();
        Method method = ReflectionUtils.findMethod(entityMapperClass, "deleteByExample", entityExampleClass);
        return (int)ReflectionUtils.invokeMethod(method, entityMapper, example);
    }

    public int deleteByPrimaryKey(Integer id) {
        M entityMapper = getEntityMapperProxy();
        Method method = ReflectionUtils.findMethod(entityMapperClass, "deleteByPrimaryKey", Integer.class);
        return (int)ReflectionUtils.invokeMethod(method, entityMapper, id);
    }

    public int insert(E record) {
        M entityMapper = getEntityMapperProxy();
        Method method = ReflectionUtils.findMethod(entityMapperClass, "insert", entityClass);
        return (int)ReflectionUtils.invokeMethod(method, entityMapper, record);
    }

    public int insertSelective(E record) {
        M entityMapper = getEntityMapperProxy();
        Method method = ReflectionUtils.findMethod(entityMapperClass, "insertSelective", entityClass);
        return (int)ReflectionUtils.invokeMethod(method, entityMapper, record);
    }

    @SuppressWarnings("unchecked")
    public List<E> selectByExample(X example) {
        M entityMapper = getEntityMapperProxy();
        Method method = ReflectionUtils.findMethod(entityMapperClass, "selectByExample", entityExampleClass);
        return (List<E>)ReflectionUtils.invokeMethod(method, entityMapper, example);
    }

    @SuppressWarnings("unchecked")
    public E selectByPrimaryKey(Integer id) {
        M entityMapper = getEntityMapperProxy();
        Method method = ReflectionUtils.findMethod(entityMapperClass, "selectByPrimaryKey", Integer.class);
        return (E)ReflectionUtils.invokeMethod(method, entityMapper, id);
    }

    public int updateByExampleSelective(E record, X example) {
        M entityMapper = getEntityMapperProxy();
        Method method = ReflectionUtils.findMethod(entityMapperClass, "updateByExampleSelective", entityClass,
            entityExampleClass);
        return (int)ReflectionUtils.invokeMethod(method, entityMapper, record, example);
    }

    public int updateByExample(E record, X example) {
        M entityMapper = getEntityMapperProxy();
        Method method
            = ReflectionUtils.findMethod(entityMapperClass, "updateByExample", entityClass, entityExampleClass);
        return (int)ReflectionUtils.invokeMethod(method, entityMapper, record, example);
    }

    public int updateByPrimaryKeySelective(E record) {
        M entityMapper = getEntityMapperProxy();
        Method method = ReflectionUtils.findMethod(entityMapperClass, "updateByPrimaryKeySelective", entityClass);
        return (int)ReflectionUtils.invokeMethod(method, entityMapper, record);
    }

    public int updateByPrimaryKey(E record) {
        M entityMapper = getEntityMapperProxy();
        Method method = ReflectionUtils.findMethod(entityMapperClass, "updateByPrimaryKey", entityClass);
        return (int)ReflectionUtils.invokeMethod(method, entityMapper, record);
    }

    /**
     * @see PagingQueryInterceptor
     */
    public PagingResult pagingByExample2(X entityExample, Integer pageIndex, Integer pageSize) {
        MappedStatement mappedStatement = this.getSqlSession().getConfiguration()
            .getMappedStatement(entityMapperClass.getName() + ".selectByExample");

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("example", entityExample);
        paramMap.put("pageIndex", pageIndex);
        paramMap.put("pageSize", pageSize);

        Long count = this.countByExample(entityExample);
        List<?> details = this.getSqlSession().selectList(mappedStatement.getId(), paramMap);

        PagingResult pagingResult = new PagingResult();
        pagingResult.setCount(count);
        pagingResult.setDetails(details);

        return pagingResult;
    }

    /**
     * @describe user example limit,offset
     * @see PagingExamplePlugin
     */
    public PagingResult pagingByExample(X entityExample, Integer pageIndex, Integer pageSize) {
        PagingResult pagingResult = new PagingResult();

        Long count = this.countByExample(entityExample);
        pagingResult.setCount(count);

        Method setLimit = ReflectionUtils.findMethod(entityExample.getClass(), "setLimit", Integer.class);
        ReflectionUtils.invokeMethod(setLimit, entityExample, pageSize);

        Method setOffset = ReflectionUtils.findMethod(entityExample.getClass(), "setOffset", Integer.class);
        int offset = Optional.of(pageIndex).filter(p -> p > 0).map(mapper -> mapper * pageSize).orElse(0);
        ReflectionUtils.invokeMethod(setOffset, entityExample, offset);

        List<?> details = this.selectByExample(entityExample);
        pagingResult.setDetails(details);

        return pagingResult;
    }

    private M getEntityMapperProxy() {
        return this.getSqlSession().getMapper(entityMapperClass);
    }

    private java.lang.reflect.Type[] getParameterizedTypes() {
        java.lang.reflect.Type type = getClass().getGenericSuperclass();
        return ((ParameterizedType)type).getActualTypeArguments();
    }

    @Override
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

}
