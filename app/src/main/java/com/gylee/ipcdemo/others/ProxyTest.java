package com.gylee.ipcdemo.others;

import java.lang.reflect.Proxy;

public class ProxyTest {
    public static void main(String[] args) {
        Class bookProxyClass = Proxy.getProxyClass(Book.class.getClassLoader(),Book.class);
    }
}
