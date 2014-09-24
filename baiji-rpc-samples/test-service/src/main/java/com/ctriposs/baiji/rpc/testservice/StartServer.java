package com.ctriposs.baiji.rpc.testservice;

import com.ctriposs.baiji.rpc.common.BaijiContract;
import com.ctriposs.baiji.rpc.server.BaijiServiceHost;
import com.ctriposs.baiji.rpc.server.HostConfig;
import com.ctriposs.baiji.rpc.server.ServiceHost;
import com.ctriposs.baiji.rpc.server.netty.BlockingHttpServerBuilder;
import com.ctriposs.baiji.rpc.server.registry.EtcdServiceRegistry;
import com.ctriposs.baiji.rpc.server.registry.ServiceInfo;
import com.ctriposs.baiji.rpc.server.registry.ServiceRegistry;
import io.netty.channel.ChannelOption;

public final class StartServer {

    public static void main(String[] args) throws Exception {
        HostConfig config = new HostConfig();
        config.debugMode = true;
        ServiceHost router = new BaijiServiceHost(config, TestServiceImpl.class);

        BlockingHttpServerBuilder builder = new BlockingHttpServerBuilder(8114);

        builder.serviceHost(router)
                .withWorkerCount(10)
                .serverSocketOption(ChannelOption.SO_BACKLOG, 100)
                .clientSocketOption(ChannelOption.TCP_NODELAY, true)
                .build().startWithoutWaitingForShutdown();

        ServiceRegistry registry = new EtcdServiceRegistry("http://localhost:4001/");
        BaijiContract contract = TestService.class.getAnnotation(BaijiContract.class);
        ServiceInfo service = new ServiceInfo.Builder().serviceName(contract.serviceName())
                .serviceNamespace(contract.serviceNamespace())
                .subEnv("dev")
                .port(8114).build();
        registry.addService(service);
        registry.run();
    }
}
