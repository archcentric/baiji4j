package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.rpc.common.HasResponseStatus;
import com.ctriposs.baiji.rpc.server.filter.*;
import com.ctriposs.baiji.specific.SpecificRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class OperationHandler {

    private static final Logger _logger = LoggerFactory.getLogger(OperationHandler.class);

    private String _methodName;
    private Method _method;

    private Class<? extends SpecificRecord> _requestType;
    private Class<? extends SpecificRecord> _responseType;
    private Constructor<? extends SpecificRecord> _requestConstructor;
    private Constructor<? extends SpecificRecord> _responseConstructor;

    private Class<?> _serviceClass;
    private Constructor<?> _serviceConstructor;
    private final List<WithRequestFilter> _requestFilters = new ArrayList<>();
    private final List<WithResponseFilter> _responseFilters = new ArrayList<>();
    private final ConcurrentMap<Class<?>, Object> _filterInstances
            = new ConcurrentHashMap<Class<?>, Object>();

    private static final ConcurrentMap<Class<?>, Object> _singletonServiceInstances
            = new ConcurrentHashMap<Class<?>, Object>();

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

    public Class<?> getServiceClass() {
        return this._serviceClass;
    }

    @SuppressWarnings("unchecked")
    public OperationHandler(Class<?> serviceClass, Method method) {
        if (serviceClass == null) {
            throw new IllegalArgumentException("Missing required serviceClass");
        }
        if (method == null) {
            throw new IllegalArgumentException("Missing required method");
        }
        this._method = method;
        this._methodName = method.getName();

        // Service type
        this._serviceClass = serviceClass;
        try {
            this._serviceConstructor = serviceClass.getConstructor();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Missing public default constructor on service Type "
                    + this._serviceClass, ex);
        }

        initializeResponseType();
        initializeRequestType();
        initializeFilters();
    }

    private void initializeRequestType() {
        Class<?>[] parameterTypes = _method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length != 1) {
            throw new IllegalArgumentException("One and only one request type is required/allowed on method " + this._methodName);
        }
        this._requestType = (Class<? extends SpecificRecord>) _method.getParameterTypes()[0];
        try {
            this._requestConstructor = this._requestType.getConstructor();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Missing public default constructor on request type " + this._requestType, ex);
        }
    }

    private void initializeResponseType() {
        this._responseType = (Class<? extends SpecificRecord>) _method.getReturnType();
        if (this._responseType == null) {
            throw new IllegalArgumentException("missing required responseType(returnType) on method " + this._methodName);
        }
        if (!HasResponseStatus.class.isAssignableFrom(this._responseType)) {
            throw new IllegalArgumentException("Response type " + this._responseType + " does not implement required interface " + HasResponseStatus.class);
        }
        try {
            this._responseConstructor = this._responseType.getConstructor();
        } catch (Exception ex) {
            throw new IllegalArgumentException("missing public default constructor on response Type " + this._responseType, ex);
        }
    }

    private void initializeFilters() {
        Annotation[] annotations = _method.getAnnotations();

        for (Annotation annotation : annotations) {
            if (WithRequestFilter.class.equals(annotation.annotationType())) {
                _requestFilters.add((WithRequestFilter) annotation);
            } else if (WithRequestFilters.class.equals(annotation.annotationType())) {
                for (WithRequestFilter filter : ((WithRequestFilters) annotation).value()) {
                    _requestFilters.add(filter);
                }
            } else if (WithResponseFilter.class.equals(annotation.annotationType())) {
                _responseFilters.add((WithResponseFilter) annotation);
            } else if (WithResponseFilters.class.equals(annotation.annotationType())) {
                for (WithResponseFilter filter : ((WithResponseFilters) annotation).value()) {
                    _responseFilters.add(filter);
                }
            }
        }

        Collections.sort(_requestFilters, new Comparator<WithRequestFilter>() {
            @Override
            public int compare(WithRequestFilter o1, WithRequestFilter o2) {
                return o1.priority() - o2.priority();
            }
        });
        Collections.sort(_requestFilters, new Comparator<WithRequestFilter>() {
            @Override
            public int compare(WithRequestFilter o1, WithRequestFilter o2) {
                return o1.priority() - o2.priority();
            }
        });
    }

    public SpecificRecord getEmptyRequestInstance() throws Exception {
        return this._requestConstructor.newInstance();
    }

    public SpecificRecord getEmptyResponseInstance() throws Exception {
        return this._responseConstructor.newInstance();
    }


    /**
     * Gets all the request filters applied to the operation, sorted by priority ascending.
     *
     * @return A list of tilers.
     * The first part contains those which should be executed before the global filters.
     * The second part contains those which should be executed before the global filters.
     */
    public List<RequestFilter>[] getRequestFilters() {
        List<RequestFilter>[] filters = new List[2];
        filters[0] = new ArrayList<>();
        filters[1] = new ArrayList<>();

        for (WithRequestFilter annotation : _requestFilters) {
            try {
                RequestFilter filter = (RequestFilter) getFilterInstance(annotation.value(),
                        annotation.reusable());
                filters[annotation.priority() < 0 ? 0 : 1].add(filter);
            } catch (Exception e) {
                _logger.error(String.format("Error occurs when creating RequestFilter %s for operation %s.",
                        annotation.value().getName(), _methodName));
            }
        }

        return filters;
    }

    /**
     * Gets all the response filters applied to the operation, sorted by priority ascending.
     *
     * @return A list of tilers.
     * The first part contains those which should be executed before the global filters.
     * The second part contains those which should be executed before the global filters.
     */
    public List<ResponseFilter>[] getResponseFilters() {
        List<ResponseFilter>[] filters = new List[2];
        filters[0] = new ArrayList<>();
        filters[1] = new ArrayList<>();

        for (WithResponseFilter annotation : _responseFilters) {
            try {
                ResponseFilter filter = (ResponseFilter) getFilterInstance(annotation.value(),
                        annotation.reusable());
                filters[annotation.priority() < 0 ? 0 : 1].add(filter);
            } catch (Exception e) {
                _logger.error(String.format("Error occurs when creating ResponseFilter %s for operation %s.",
                        annotation.value().getName(), _methodName));
            }
        }

        return filters;
    }

    private Object getFilterInstance(Class<?> filterClass, boolean reusable) throws Exception {
        if (!reusable) {
            return filterClass.newInstance();
        }

        // Lazy initialization
        Object instance = _filterInstances.get(filterClass);
        if (instance == null) {
            instance = filterClass.newInstance();
            Object existedInstance = _filterInstances.putIfAbsent(filterClass, instance);
            if (existedInstance != null) {
                instance = existedInstance;
            }
        }
        return instance;
    }

    // Invoke target service
    public SpecificRecord invoke(OperationContext context, boolean newServiceInstance) throws Exception {
        Object serviceInstance = this.getServiceInstance(newServiceInstance);
        return (SpecificRecord) this._method.invoke(serviceInstance, context.getRequestObject());
    }

    private Object getServiceInstance(boolean newServiceInstance) throws Exception {
        if (newServiceInstance) {
            return _serviceConstructor.newInstance();
        } else {
            // Lazy initialization
            Object instance = _singletonServiceInstances.get(_serviceClass);
            if (instance == null) {
                instance = _serviceConstructor.newInstance();
                Object existedInstance = _singletonServiceInstances.putIfAbsent(_serviceClass, instance);
                if (existedInstance != null) {
                    instance = existedInstance;
                }
            }
            return instance;
        }
    }
}
