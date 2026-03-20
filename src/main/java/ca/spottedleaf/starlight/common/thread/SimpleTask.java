package ca.spottedleaf.starlight.common.thread;

import com.ishland.flowsched.executor.LockToken;
import com.ishland.flowsched.executor.Task;

import java.util.Objects;

public class SimpleTask extends Task {

    private final Runnable task;
    private final LockToken[] lockTokens;

    public SimpleTask(Runnable task, LockToken[] lockTokens) {
        this.task = Objects.requireNonNull(task, "task");
        this.lockTokens = Objects.requireNonNull(lockTokens, "lockTokens");
    }

    @Override
    public void run(Runnable releaseLocks) {
        try {
            this.task.run();
        } finally {
            releaseLocks.run();
        }
    }

    @Override
    public void propagateException(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public LockToken[] lockTokens() {
        return this.lockTokens;
    }

}
