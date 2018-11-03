package org.equinox.async.executor;

public interface AsyncExecutor {
    void execute(Runnable runnable);
}
