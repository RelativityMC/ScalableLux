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
}
