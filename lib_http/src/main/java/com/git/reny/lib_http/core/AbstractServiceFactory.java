package com.git.reny.lib_http.core;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractServiceFactory<S> implements IServiceFactory<S> {

    private Map<Class<S>, S> serviceMap;

    @Override
    public S getService(Class<S> serviceClass) {
        if (null == serviceMap) {
            serviceMap = new HashMap<>();
        }
        if (null != serviceMap.get(serviceClass)) {
            return serviceMap.get(serviceClass);
        } else {
            S service = createService(serviceClass);
            serviceMap.put(serviceClass, service);
            return service;
        }
    }

    protected abstract S createService(Class<S> serviceClass);

}
