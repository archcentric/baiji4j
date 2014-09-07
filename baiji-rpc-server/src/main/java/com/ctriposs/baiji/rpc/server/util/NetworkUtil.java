package com.ctriposs.baiji.rpc.server.util;

import java.net.*;
import java.util.Enumeration;
import java.util.List;

public final class NetworkUtil {

    private NetworkUtil() {
    }

    public static String getIpAddress() {
        String ip = null;
        try {
            Enumeration<NetworkInterface> er = NetworkInterface.getNetworkInterfaces();
            while (er.hasMoreElements()) {
                NetworkInterface ni = er.nextElement();
                if (ni.getName().startsWith("eth") || ni.getName().startsWith("bond")) {
                    List<InterfaceAddress> list = ni.getInterfaceAddresses();
                    for (InterfaceAddress interfaceAddress : list) {
                        InetAddress address = interfaceAddress.getAddress();
                        if (address instanceof Inet4Address) {
                            ip = address.getHostAddress();
                            break;
                        }
                    }
                }
                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {
        }
        if (ip == null) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
            }
        }
        return ip;
    }
}
