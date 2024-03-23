package ca.spottedleaf.starlight.common.thread;

import ca.spottedleaf.starlight.common.config.Config;
import com.ishland.flowsched.executor.ExecutorManager;

import java.util.concurrent.atomic.AtomicInteger;

public class GlobalExecutors {

    private static final AtomicInteger prioritizedSchedulerCounter = new AtomicInteger(0);
    public static final ExecutorManager prioritizedScheduler = new ExecutorManager(Config.PARALLELISM, thread -> {
        thread.setDaemon(true);
        thread.setName("scalablelux-%d".formatted(prioritizedSchedulerCounter.getAndIncrement()));
    });
    private static final boolean FORCE_ENABLED = Boolean.getBoolean("scalablelux.force_enabled");
    public static final boolean ENABLED = SchedulingUtil.isExternallyManaged() || FORCE_ENABLED || Config.PARALLELISM > 1;

    static {
        if (SchedulingUtil.isExternallyManaged()) {
            System.out.println("[ScalableLux] Lighting scaling is enabled in externally managed mode");
        } else if (FORCE_ENABLED) {
            System.out.println("[ScalableLux] Lighting scaling is forced enabled, using %d threads".formatted(Config.PARALLELISM));
        } else if (ENABLED) {
            System.out.println("[ScalableLux] Lighting scaling is enabled, using %d threads".formatted(Config.PARALLELISM));
        } else {
            System.out.println("[ScalableLux] Lighting scaling is disabled (due to low parallelism in the settings)");
        }
    }

}
