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

    @Deprecated(forRemoval = true)
    default  SWMRNibbleArray[] getBlockNibbles() {
        return scalablelux$getBlockNibbles();
    }
    @Deprecated(forRemoval = true)
    default void setBlockNibbles(final SWMRNibbleArray[] nibbles) {
        scalablelux$setBlockNibbles(nibbles);
    }

    @Deprecated(forRemoval = true)
    default SWMRNibbleArray[] getSkyNibbles() {
        return scalablelux$getSkyNibbles();
    }
    @Deprecated(forRemoval = true)
    default void setSkyNibbles(final SWMRNibbleArray[] nibbles) {
        scalablelux$setSkyNibbles(nibbles);
    }

    @Deprecated(forRemoval = true)
    default boolean[] getSkyEmptinessMap() {
        return scalablelux$getSkyEmptinessMap();
    }
    @Deprecated(forRemoval = true)
    default void setSkyEmptinessMap(final boolean[] emptinessMap) {
        scalablelux$setSkyEmptinessMap(emptinessMap);
    }

    @Deprecated(forRemoval = true)
    default boolean[] getBlockEmptinessMap() {
        return scalablelux$getBlockEmptinessMap();
    }
    @Deprecated(forRemoval = true)
    default void setBlockEmptinessMap(final boolean[] emptinessMap) {
        scalablelux$setBlockEmptinessMap(emptinessMap);
    }
}
