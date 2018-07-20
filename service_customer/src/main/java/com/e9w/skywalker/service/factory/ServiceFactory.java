package com.e9w.skywalker.service.factory;

import com.e9w.skywalker.service.interceptor.ServiceProviderInterceptor;
import org.springframework.aop.framework.ProxyFactory;

/**
 * Created by fc on 2016-12-09.
 */
public class ServiceFactory {

    public static <T> T getService(Class<T> serviceInterface) {
        ProxyFactory proxyFactory=new ProxyFactory(serviceInterface, new ServiceProviderInterceptor());
        return (T) proxyFactory.getProxy();
    }
}
