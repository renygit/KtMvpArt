package com.git.reny.lib_http.core;

public interface IServiceFactory<S> {

    S getService(Class<S> serviceClass);

}
