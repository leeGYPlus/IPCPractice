## 1. AIDL 生成类的描述

依据 AIDL 文件生成的 Java 文件的大致结构为以下：

```
public interface ISome extends android.os.IInterface{
    // 需要注意的是 Stub 为抽象类
    public static abstract class Stub extends android.os.Binder implements ISome{

        public static IBookManagerBackup asInterface(android.os.IBinder obj)
                {
                    if ((obj==null)) {
                        return null;
                    }
                    android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
                    if (((iin!=null)&&(iin instanceof ISome))) {
                        return ((IBookManagerBackup)iin);
                    }
                    return new Stub.Proxy(obj);
                }

        private static class Proxy implements ISome{
        }
    }
}
```

其中 asInterface 方法的主要作用是

    将服务端端 Binder 对象转换为客户端所需要的 AIDL 接口类型对象,如果客户端和服务端位于不同的进程，那么返回 相应的代理对象new Stub.Proxy(obj)。


我们可以大致的看一下其具体使用，主要包括提供功能的服务端：Service 和使用功能的 Activity


```
public class BookMananger2Service extends Service{
    // Stub 的匿名对象为服务端的对象
    private Binder mBinder = new IBookManager.Stub() {
        // AIDL 文件中定义的方法的具体实现
    }

    // 返回 IBinder 对象，此时的 mBinder 即为在 asInterface 功能所描述中的 服务端Binder 对象。
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
```
下面我们看一下客户端的实现：

客户端的究极目的是实现相应的功能，具体为调用 AIDL 文件中描述的相关方法，即调用 AIDL 对应的接口类中的方法。

```
public class BookManager2Activity extends AppCompatActivity {

    private IBookManager bookManager;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = IBookManager.Stub.asInterface(service);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            bookManager = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
        // 绑定服务
        Intent intent = new Intent(this, BookMananger2Service.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void addBook(View view) throws RemoteException {
        // 具体调用 bookManager 的方法，实现相应功能，从而完成跨进程调用
        Book book = new Book(bookId, "Android Book:" + bookId);
        bookManager.addBook(book);
    }
}
```

下面我们看一下在 Activity 中服务端处于相同进程和不同进程时获得的 IBookManager 对象：

* 相同进程

    在相同进程时，获得为在服务端实现的 Stub 对象，此处为 在 BookMananger2Service 定义的匿名对象 mBinder，此时客户端科室使用 Stub 对象引用进行方法调用。

* 不同进程

    此时获得的为 Stub 的实现类 Proxy 对象，此时k客户端可以通过该 Proxy 对象进行方法调用，不过同时该 Proxy 也持有 mBinder 的引用，并且通过 Stub#OnTranact、IBinder#tranact、mBinder#xxx 等方法，
    完成跨进程对某个方法的调用，从而完成相关功能的实现。


# 2


在上面的例子钟，服务端提供的 IBinder 对象是以匿名内部类的形式提供的：

 ```
 private Binder mBinder = new IBookManager.Stub() {
     // AIDL 文件中定义的方法的具体实现
 }
 ```
 以上为通过 AIDL 文件来生成相应的接口，不过在实际运用中，往往会自定义接口实现 AIDL 生成类的功能，从而完成自定义实现相应的 Stub 以及 代理类，如在 Naguat 中的 IApplicationThread 以及其实现类：


```
// 自定义接口实现 IInterface
public interface IApplicationThread extends IInterface{
    // 自定义的一些方法
}
public abstract class ApplicationThreadNative extends Binder
        implements IApplicationThread {
    static public IApplicationThread asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IApplicationThread in =
            (IApplicationThread)obj.queryLocalInterface(descriptor);
        if (in != null) {
            return in;
        }
        return new ApplicationThreadProxy(obj);
    }
    ....
}

class ApplicationThreadProxy implements IApplicationThread {
    private final IBinder mRemote;

    public ApplicationThreadProxy(IBinder remote) {
        mRemote = remote;
    }

    public final IBinder asBinder() {
        return mRemote;
    }
    ....
}

```
客户端调用：


```
class AMS extends ActivityManagerNative
                  implements Watchdog.Monitor, BatteryStatsImpl.BatteryCallback{
    public int startActivity(IBinder whoThread, String callingPackage,
                Intent intent, String resolvedType, Bundle bOptions) {
        ...
        appThread = ApplicationThreadNative.asInterface(whoThread);
        ...
    }
}
```

在 AOSP 源码中基本套路就是这些了，不过同时也存在继承 XXXXNative 实现子类，比如 AMS、ApplicationThread，不过我们需要知道的是类名为 XXXXNative 时，为相同进程的调用，
当类名为 XXXProxy 时，为不同进程的调用，当然这只是针对这种情况而说，并不是说以两者结尾的所有类都是如此的实现。

**针对 XXNative 和 XXPRoxy 对象引用，都是要在客户端进行调用，只是在 Proxy 对象时涉及跨进程调用，而此时 Proxy 对象为客户端对象，而 Stub 为服务端对象。**

不过在最下版本的源码炸，大部分自定义实现相应的接口基本上都被替换为通过 AIDL 的实现方式。

