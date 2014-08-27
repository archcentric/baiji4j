package com.ctriposs.baiji.rpc.common;

import java.lang.annotation.*;

/**
 * Mark a Baiji Rpc supported service
 *
 * @author bulldog
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface BaijiContract {
    /**
     * A formally defined service name, extracted from Baiji IDL during code generation.
     *
     * @return service name
     */
    String serviceName();

    /**
     * A formally defined service namespace, extracted from Baiji IDL during code generation.
     *
     * @return service namespace
     */
    String serviceNamespace();

    /**
     * The version of Baiji Rpc CodeGenerator used to generated this service
     *
     * @return version
     */
    String codeGeneratorVersion() default "1.0.0";
}
