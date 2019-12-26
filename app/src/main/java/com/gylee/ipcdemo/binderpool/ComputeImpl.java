package com.gylee.ipcdemo.binderpool;

import android.os.RemoteException;

public class ComputeImpl extends com.gylee.ipcdemo.binderpool.ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return  a + b;
    }
}
