package com.persistence.plugin;

import java.util.Map;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.plugin.Interceptor;

public abstract class AbstractInterceptor implements Interceptor {

    protected <T> T safeGet(Map<?, ?> map, String key) {
        try {
            return (T)map.get(key);
        } catch (BindingException ex) {
            return null;
        }
    }
}
