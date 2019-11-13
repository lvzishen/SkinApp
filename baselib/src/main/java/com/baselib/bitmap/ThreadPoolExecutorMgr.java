package com.baselib.bitmap;

import android.util.Log;

import com.baselib.utils.ModuleConfig;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import bolts.Task;


public class ThreadPoolExecutorMgr {

	private static final boolean DEBUG = ModuleConfig.DEBUG;
	private static final String TAG = "ThreadPool";
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "Customize AsyncTask #" + mCount.getAndIncrement());
        }
    };
    
    
    private static final ThreadFactory sFbThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "Facebook AsyncTask #" + mCount.getAndIncrement());
        }
    };


    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(64);
    private static final BlockingQueue<Runnable> sFbPoolWorkQueue = new LinkedBlockingQueue<Runnable>(64);

    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    /*public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory,new ThreadPoolExecutor.DiscardOldestPolicy());*/
    public static final ExecutorService THREAD_POOL_EXECUTOR = Task.BACKGROUND_EXECUTOR;


    public static Executor FB_THREAD_POOL_EXECUTOR = null;

    public static Executor getFBExecutor(){
        if(FB_THREAD_POOL_EXECUTOR == null) {
            FB_THREAD_POOL_EXECUTOR =  new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                    KEEP_ALIVE, TimeUnit.SECONDS, sFbPoolWorkQueue, sFbThreadFactory) {

                @Override
                public void execute(Runnable command) {
                    try {
                        super.execute(command);
                    } catch (Exception e) {
                        if (DEBUG) {
                            Log.e(TAG, "", e);
                        }
                    }

                }

            };
        }
        return FB_THREAD_POOL_EXECUTOR;
    }

    public static void submit(final Runnable runnable){
        if(runnable == null)
            return;
        Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                if(runnable != null)
                    runnable.run();
                return null;
            }
        });
    }
}
