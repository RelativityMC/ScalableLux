package ca.spottedleaf.starlight.common.world;

import ca.spottedleaf.starlight.common.light.SWMRNibbleArray;

public interface ExtendedSerializableChunkData {

    void scalablelux$setBlockLight(SWMRNibbleArray.SaveState[] light);

    void scalablelux$setSkyLight(SWMRNibbleArray.SaveState[] light);

    void scalableLux$setActuallyCorrect(boolean correct);

    SWMRNibbleArray.SaveState[] scalablelux$getBlockLight();

    SWMRNibbleArray.SaveState[] scalablelux$getSkyLight();

    boolean scalablelux$getActuallyCorrect();

    void scalablelux$setLightCorrect(boolean correct);

}
