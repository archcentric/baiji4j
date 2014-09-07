package com.ctriposs.baiji.rpc.server.registry;

public interface ServiceRegistry {

    void addService(ServiceInfo serviceInfo);

    int getHeartbeatInterval();

    void setHeartbeatInterval(int interval);

    void run();

    void stop();
}
