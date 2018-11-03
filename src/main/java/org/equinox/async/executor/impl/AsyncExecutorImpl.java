package org.equinox.async.executor.impl;

import org.equinox.async.executor.AsyncExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncExecutorImpl implements AsyncExecutor {
    @Async
    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }
}
