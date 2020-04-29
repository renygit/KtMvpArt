package com.git.reny.lib_base.base.taskdispatch.utils

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

object DispatcherExecutor {
    /**
     * 获取CPU线程池
     * @return
     */
    var cPUExecutor: ThreadPoolExecutor

    /**
     * 获取IO线程池
     * @return
     */
    var iOExecutor: ExecutorService
    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()

    // We want at least 2 threads and at most 4 threads in the core pool,
    // preferring to have 1 less than the CPU count to avoid saturating
    // the CPU with background work
    private val CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 5))
    private val MAXIMUM_POOL_SIZE = CORE_POOL_SIZE
    private const val KEEP_ALIVE_SECONDS = 5
    private val sPoolWorkQueue: BlockingQueue<Runnable> =
        LinkedBlockingQueue()
    private val sThreadFactory =
        DefaultThreadFactory()
    private val sHandler =
        RejectedExecutionHandler { r, _ ->
            // 一般不会到这里
            Executors.newCachedThreadPool().execute(r)
        }

    /**
     * The default thread factory.
     */
    private class DefaultThreadFactory internal constructor() : ThreadFactory {
        private val group: ThreadGroup
        private val threadNumber =
            AtomicInteger(1)
        private val namePrefix: String
        override fun newThread(r: Runnable): Thread {
            val t = Thread(
                group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0
            )
            if (t.isDaemon) t.isDaemon = false
            if (t.priority != Thread.NORM_PRIORITY) t.priority = Thread.NORM_PRIORITY
            return t
        }

        companion object {
            private val poolNumber =
                AtomicInteger(1)
        }

        init {
            val s = System.getSecurityManager()
            group = if (s != null) s.threadGroup else Thread.currentThread()
                .threadGroup
            namePrefix = "TaskDispatcherPool-" +
                    poolNumber.getAndIncrement() +
                    "-Thread-"
        }
    }

    init {
        cPUExecutor = ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE,
            KEEP_ALIVE_SECONDS.toLong(),
            TimeUnit.SECONDS,
            sPoolWorkQueue,
            sThreadFactory,
            sHandler
        )
        cPUExecutor.allowCoreThreadTimeOut(true)
        iOExecutor = Executors.newCachedThreadPool(sThreadFactory)
    }
}