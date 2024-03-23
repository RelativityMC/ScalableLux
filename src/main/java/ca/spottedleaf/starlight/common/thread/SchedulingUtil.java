package ca.spottedleaf.starlight.common.thread;

import ca.spottedleaf.starlight.common.util.CoordinateUtils;
import com.ishland.flowsched.executor.LockToken;

import java.util.ArrayList;

public class SchedulingUtil {

    public static void scheduleTask(int ownerTag, Runnable task, int x, int z, int radius) {
        final ArrayList<LockToken> lockTokens = new ArrayList<>((radius * 2 + 1) * (radius * 2 + 1));
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                lockTokens.add(new LockTokenImpl(ownerTag, CoordinateUtils.getChunkKey(x + i, z + j)));
            }
        }
        final SimpleTask simpleTask = new SimpleTask(task, lockTokens.toArray(LockToken[]::new), 240);
        GlobalExecutors.prioritizedScheduler.schedule(simpleTask);
    }

    public static boolean isExternallyManaged() {
        return false;
    }

}
