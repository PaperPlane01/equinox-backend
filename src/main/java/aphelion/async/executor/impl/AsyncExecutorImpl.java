package aphelion.async.executor.impl;

import aphelion.async.executor.AsyncExecutor;
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
