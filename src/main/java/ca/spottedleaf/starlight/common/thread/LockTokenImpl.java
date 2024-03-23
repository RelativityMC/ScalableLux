package ca.spottedleaf.starlight.common.thread;

import com.ishland.flowsched.executor.LockToken;

public record LockTokenImpl(int ownerTag, long pos) implements LockToken {
}
