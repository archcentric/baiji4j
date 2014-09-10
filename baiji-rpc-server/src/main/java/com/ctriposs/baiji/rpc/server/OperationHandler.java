package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.rpc.common.HasResponseStatus;
import com.ctriposs.baiji.specific.SpecificRecord;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OperationHandler {

    private static final Logger _logger = LoggerFactory.getLogger(OperationHandler.class);

    private String _methodName;
    private Method _method;

    private Class<? extends SpecificRecord> _requestType;
    private Class<? extends SpecificRecord> _responseType;
    private Constructor<? extends SpecificRecord> _requestConstructor;
    private Constructor<? extends SpecificRecord> _responseConstructor;

    private Class<?> _serviceType;
    private Constructor<?> _serviceConstructor;

    private static final ConcurrentMap<Class<?>, Object> _singletonServiceInstances
            = new ConcurrentHashMap<Class<?>, Object>();

    private ServiceConfig _config;

    public String getMethodName() {
        return this._methodName;
    }

    public Method getMethod() {
        return this._method;
    }

    public Class<? extends SpecificRecord> getResponseType() {
        return this._responseType;
    }

    public Class<? extends SpecificRecord> getRequestType() {
        return this._requestType;
    }

    public Class<?> getServiceType() {
        return this._serviceType;
    }

    @SuppressWarnings("unchecked")
    public OperationHandler(ServiceConfig config, Class<?> serviceType, Method method) {
        if (config == null) {
            throw new IllegalArgumentException("missing required serviceConfig");
        }
        if (serviceType == null) {
            throw new IllegalArgumentException("missing required serviceType");
        }
        if (method == null) {
            throw new IllegalArgumentException("missing required method");
        }
        this._config = config;
        this._method = method;
        this._methodName = method.getName();

        // service type
        this._serviceType = serviceType;
        try {
            this._serviceConstructor = serviceType.getConstructor();
        } catch (Exception ex) {
            throw new IllegalArgumentException("missing public parameterless constructor on service Type "
                    + this._serviceType, ex);
        }

        // response type
        this._responseType = (Class<? extends SpecificRecord>) method.getReturnType();
        if (this._responseType == null) {
            throw new IllegalArgumentException("missing required responseType(returnType) on method " + this._methodName);
        }
        if (!HasResponseStatus.class.isAssignableFrom(this._responseType)) {
            throw new IllegalArgumentException("Response type " + this._responseType + " does not implement required interface " + HasResponseStatus.class);
        }
        try {
            this._responseConstructor = this._responseType.getConstructor();
        } catch (Exception ex) {
            throw new IllegalArgumentException("missing public parameterless constructor on response Type " + this._responseType, ex);
        }

        // request type
        if (this._method.getParameterTypes() == null || this._method.getParameterTypes().length != 1) {
            throw new IllegalArgumentException("One and only one request type is required/allowed on method " + this._methodName);
        }
        this._requestType = (Class<? extends SpecificRecord>) method.getParameterTypes()[0];
        try {
            this._requestConstructor = this._requestType.getConstructor();
        } catch (Exception ex) {
            throw new IllegalArgumentException("missing public parameterless constructor on request Type " + this._requestType, ex);
        }
    }

    public SpecificRecord getEmptyRequestInstance() throws Exception {
        return this._requestConstructor.newInstance();
    }

    public SpecificRecord getEmptyResponseInstance() throws Exception {
        return this._responseConstructor.newInstance();
    }

    public Object getServiceInstance() throws Exception {
        if (this._config.isNewServiceInstancePerRequest()) {
            return _serviceConstructor.newInstance();
        } else {
            // Lazy initialization
            Object instance = _singletonServiceInstances.get(_serviceType);
            if (instance == null) {
                instance = _serviceConstructor.newInstance();
                Object existedInstance = _singletonServiceInstances.putIfAbsent(_serviceType, instance);
                if (existedInstance != null) {
                    instance = existedInstance;
                }
            }
            return instance;
        }
    }

    // invoke target service
    public SpecificRecord invoke(OperationContext context) throws Exception {
        Object serviceInstance = this.getServiceInstance();
        return (SpecificRecord) this._method.invoke(serviceInstance, context.getRequestObject());
    }
}
