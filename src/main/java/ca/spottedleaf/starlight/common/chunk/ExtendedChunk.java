package ca.spottedleaf.starlight.common.chunk;

import ca.spottedleaf.starlight.common.light.SWMRNibbleArray;

public interface ExtendedChunk {

    public SWMRNibbleArray[] scalablelux$getBlockNibbles();
    public void scalablelux$setBlockNibbles(final SWMRNibbleArray[] nibbles);

    public SWMRNibbleArray[] scalablelux$getSkyNibbles();
    public void scalablelux$setSkyNibbles(final SWMRNibbleArray[] nibbles);

    public boolean[] scalablelux$getSkyEmptinessMap();
    public void scalablelux$setSkyEmptinessMap(final boolean[] emptinessMap);

    public boolean[] scalablelux$getBlockEmptinessMap();
    public void scalablelux$setBlockEmptinessMap(final boolean[] emptinessMap);

    public boolean scalablelux$usingStarlight();

    @Deprecated
    default  SWMRNibbleArray[] getBlockNibbles() {
        return scalablelux$getBlockNibbles();
    }
    @Deprecated
    default void setBlockNibbles(final SWMRNibbleArray[] nibbles) {
        scalablelux$setBlockNibbles(nibbles);
    }

    @Deprecated
    default SWMRNibbleArray[] getSkyNibbles() {
        return scalablelux$getSkyNibbles();
    }
    @Deprecated
    default void setSkyNibbles(final SWMRNibbleArray[] nibbles) {
        scalablelux$setSkyNibbles(nibbles);
    }

    @Deprecated
    default boolean[] getSkyEmptinessMap() {
        return scalablelux$getSkyEmptinessMap();
    }
    @Deprecated
    default void setSkyEmptinessMap(final boolean[] emptinessMap) {
        scalablelux$setSkyEmptinessMap(emptinessMap);
    }

    @Deprecated
    default boolean[] getBlockEmptinessMap() {
        return scalablelux$getBlockEmptinessMap();
    }
    @Deprecated
    default void setBlockEmptinessMap(final boolean[] emptinessMap) {
        scalablelux$setBlockEmptinessMap(emptinessMap);
    }
}
