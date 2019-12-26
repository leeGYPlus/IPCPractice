// IBinderPool.aidl
package com.gylee.ipcdemo.binderpool;

// Declare any non-default types here with import statements

interface IBinderPool {
   IBinder queryBinder(int binderCode);
}
