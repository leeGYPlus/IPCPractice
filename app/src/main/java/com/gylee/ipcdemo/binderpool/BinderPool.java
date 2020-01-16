package com.gylee.ipcdemo.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CountDownLatch;
import com.gylee.ipcdemo.binderpool.IBinderPool.Stub;

public class BinderPool {
    private static final String TAG = "BinderPool";
    public static final int BINDER_NULL = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY = 1;

    private Context mContext;
    private com.gylee.ipcdemo.binderpool.IBinderPool mBinderPool;
    private static volatile BinderPool sInstance;
    private CountDownLatch countDownLatch;

    public BinderPool(Context mContext) {
        this.mContext = mContext;
        connectionBinderPoolService();
    }

    public static BinderPool getInstance(Context context){
        if (sInstance == null){
            synchronized (BinderPool.class){
                if(sInstance == null){
                    sInstance = new BinderPool(context);
                }
            }
        }
        return sInstance;
    }


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = com.gylee.ipcdemo.binderpool.IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(deathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.e(TAG, "binderDied: binder die" );
            mBinderPool.asBinder().unlinkToDeath(deathRecipient,0);
            mBinderPool = null;
            connectionBinderPoolService();
        }
    };


    private synchronized void connectionBinderPoolService() {
        countDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext,BinderPoolService.class);
        mContext.bindService(service,connection,Context.BIND_AUTO_CREATE);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder queryBinder(int binderCoder){
        IBinder binder = null;
        if (mBinderPool != null){
            try {
                binder = mBinderPool.queryBinder(binderCoder);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return binder;
    }

    public static class BinderPoolImpl extends Stub{

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode){
                case BINDER_COMPUTE:{
                    binder = new ComputeImpl();
                }
                case BINDER_SECURITY:{
                    binder = new SecurityCenterImpl();
                }
                default:
                    break;
            }
            return binder;
        }
    }
}
