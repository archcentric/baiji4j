package com.ctriposs.baiji.rpc.client.registry;

/**
 * Created by yqdong on 2014/10/22.
 */
public class ServiceInfo {

    private String _serviceContact;

    private boolean _ready;

    public String getServiceContact() {
        return _serviceContact;
    }

    public void setServiceContact(String serviceContact) {
        this._serviceContact = serviceContact;
    }

    public boolean isReady() {
        return _ready;
    }

    public void setReady(boolean ready) {
        this._ready = ready;
    }
}
