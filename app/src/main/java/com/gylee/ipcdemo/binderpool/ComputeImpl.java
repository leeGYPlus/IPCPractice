package com.gylee.ipcdemo.binderpool;

import android.os.RemoteException;
import com.gylee.ipcdemo.binderpool.ICompute.Stub;

public class ComputeImpl extends Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return  a + b;
    }
}
