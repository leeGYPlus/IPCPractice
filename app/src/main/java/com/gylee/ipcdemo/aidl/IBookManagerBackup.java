package com.gylee.ipcdemo.aidl;

/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
// Declare any non-default types here with import statements

public interface IBookManagerBackup extends android.os.IInterface
{
    /** Default implementation for IBookManager. */
    public static class Default implements IBookManagerBackup
    {
        /**
         * Demonstrates some basic types that you can use as parameters
         * and return values in AIDL.
         */
        @Override public java.util.List<Book> obtainBookList() throws android.os.RemoteException
        {
            return null;
        }
        @Override public void addBook(Book book) throws android.os.RemoteException
        {
        }
        @Override public void registerListener(com.gylee.ipcdemo.aidl.IOnNewBookArriveListener listener) throws android.os.RemoteException
        {
        }
        @Override public void unregisterListener(com.gylee.ipcdemo.aidl.IOnNewBookArriveListener listener) throws android.os.RemoteException
        {
        }
        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }
    /** Local-side IPC implementation stub class. */
    public static abstract class Stub extends android.os.Binder implements IBookManagerBackup
    {
        private static final String DESCRIPTOR = "com.gylee.ipcdemo.aidl.IBookManager";
        /** Construct the stub at attach it to the interface. */
        public Stub()
        {
            this.attachInterface(this, DESCRIPTOR);
        }
        /**
         * Cast an IBinder object into an com.gylee.ipcdemo.aidl.IBookManager interface,
         * generating a proxy if needed.
         */
        public static IBookManagerBackup asInterface(android.os.IBinder obj)
        {
            if ((obj==null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin!=null)&&(iin instanceof IBookManagerBackup))) {
                return ((IBookManagerBackup)iin);
            }
            return new Stub.Proxy(obj);
        }

        // 此时返回的 this 即为 BookMananger2Service 中的
        @Override public android.os.IBinder asBinder()
        {
            return this;
        }
        @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
        {
            String descriptor = DESCRIPTOR;
            switch (code)
            {
                case INTERFACE_TRANSACTION:
                {
                    reply.writeString(descriptor);
                    return true;
                }
                case TRANSACTION_obtainBookList:
                {
                    data.enforceInterface(descriptor);
                    java.util.List<Book> _result = this.obtainBookList();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case TRANSACTION_addBook:
                {
                    data.enforceInterface(descriptor);
                    Book _arg0;
                    if ((0!=data.readInt())) {
                        _arg0 = Book.CREATOR.createFromParcel(data);
                    }
                    else {
                        _arg0 = null;
                    }
                    this.addBook(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_registerListener:
                {
                    data.enforceInterface(descriptor);
                    com.gylee.ipcdemo.aidl.IOnNewBookArriveListener _arg0;
                    _arg0 = com.gylee.ipcdemo.aidl.IOnNewBookArriveListener.Stub.asInterface(data.readStrongBinder());
                    this.registerListener(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_unregisterListener:
                {
                    data.enforceInterface(descriptor);
                    com.gylee.ipcdemo.aidl.IOnNewBookArriveListener _arg0;
                    _arg0 = com.gylee.ipcdemo.aidl.IOnNewBookArriveListener.Stub.asInterface(data.readStrongBinder());
                    this.unregisterListener(_arg0);
                    reply.writeNoException();
                    return true;
                }
                default:
                {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }
        private static class Proxy implements IBookManagerBackup
        {
            private android.os.IBinder mRemote;
            Proxy(android.os.IBinder remote)
            {
                mRemote = remote;
            }
            @Override public android.os.IBinder asBinder()
            {
                return mRemote;
            }
            public String getInterfaceDescriptor()
            {
                return DESCRIPTOR;
            }
            /**
             * Demonstrates some basic types that you can use as parameters
             * and return values in AIDL.
             */
            @Override public java.util.List<Book> obtainBookList() throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.util.List<Book> _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_obtainBookList, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        return getDefaultImpl().obtainBookList();
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(Book.CREATOR);
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            @Override public void addBook(Book book) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((book!=null)) {
                        _data.writeInt(1);
                        book.writeToParcel(_data, 0);
                    }
                    else {
                        _data.writeInt(0);
                    }
                    boolean _status = mRemote.transact(Stub.TRANSACTION_addBook, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().addBook(book);
                        return;
                    }
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void registerListener(com.gylee.ipcdemo.aidl.IOnNewBookArriveListener listener) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
                    boolean _status = mRemote.transact(Stub.TRANSACTION_registerListener, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().registerListener(listener);
                        return;
                    }
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            @Override public void unregisterListener(com.gylee.ipcdemo.aidl.IOnNewBookArriveListener listener) throws android.os.RemoteException
            {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
                    boolean _status = mRemote.transact(Stub.TRANSACTION_unregisterListener, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().unregisterListener(listener);
                        return;
                    }
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            public static IBookManagerBackup sDefaultImpl;
        }
        static final int TRANSACTION_obtainBookList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_registerListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
        static final int TRANSACTION_unregisterListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
        public static boolean setDefaultImpl(IBookManagerBackup impl) {
            if (Stub.Proxy.sDefaultImpl == null && impl != null) {
                Stub.Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }
        public static IBookManagerBackup getDefaultImpl() {
            return Stub.Proxy.sDefaultImpl;
        }
    }
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    public java.util.List<Book> obtainBookList() throws android.os.RemoteException;
    public void addBook(Book book) throws android.os.RemoteException;
    public void registerListener(com.gylee.ipcdemo.aidl.IOnNewBookArriveListener listener) throws android.os.RemoteException;
    public void unregisterListener(com.gylee.ipcdemo.aidl.IOnNewBookArriveListener listener) throws android.os.RemoteException;
}
