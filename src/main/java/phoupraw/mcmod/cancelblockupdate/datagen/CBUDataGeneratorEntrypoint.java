package phoupraw.mcmod.cancelblockupdate.datagen;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class CBUDataGeneratorEntrypoint implements DataGeneratorEntrypoint {

    public CBUDataGeneratorEntrypoint() {
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator g) {
        var pack = g.createPack();
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            pack.addProvider(Chinese::new);
            pack.addProvider(English::new);
        }
    }

}
