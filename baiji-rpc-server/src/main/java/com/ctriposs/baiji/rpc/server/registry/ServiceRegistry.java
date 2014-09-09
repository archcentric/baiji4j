package com.ctriposs.baiji.rpc.server.registry;

/**
 * An interface used to access SOA service registry.
 * <p/>
 * When running, it will register all added services
 * and maintain the registration by sending heartbeats periodically.
 */
public interface ServiceRegistry {

    /**
     * Add a service into the registry.
     *
     * @param serviceInfo
     */
    void addService(ServiceInfo serviceInfo);

    /**
     * Gets the interval in second of heartbeats.
     *
     * @return
     */
    int getHeartbeatInterval();

    /**
     * Sets the interval in second of heartbeats.
     *
     * @param interval
     */
    void setHeartbeatInterval(int interval);

    void run();

    void stop();
}
