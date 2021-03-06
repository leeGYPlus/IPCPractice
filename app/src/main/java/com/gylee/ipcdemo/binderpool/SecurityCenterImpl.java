package com.gylee.ipcdemo.binderpool;

import android.os.RemoteException;
import com.gylee.ipcdemo.binderpool.ISecurityCenter.Stub;

public class SecurityCenterImpl extends Stub {
    private static final char SECRET_CODE = '^';

    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^= SECRET_CODE;
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return encrypt(password);
    }
}
