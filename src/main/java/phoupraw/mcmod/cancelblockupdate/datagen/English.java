package phoupraw.mcmod.cancelblockupdate.datagen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import phoupraw.mcmod.cancelblockupdate.CancelBlockUpdate;
import phoupraw.mcmod.cancelblockupdate.registry.CBUGameRules;

@Environment(EnvType.CLIENT)
final class English extends FabricLanguageProvider {

    English(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder b) {
        String modName = "Cancel Block Update";
        b.add("modmenu.nameTranslation." + CancelBlockUpdate.MOD_ID, modName);
        b.add("modmenu.descriptionTranslation." + CancelBlockUpdate.MOD_ID, "Cancel block updates, block schedule ticks, random ticks, place validation, fluid schedule ticks. Keep this mod on during world generation will generate lots of floating blocks. Switch this mod by game rule " + CBUGameRules.KEY_OFF.getName() + ".");
        b.add(CBUGameRules.KEY_OFF.getTranslationKey(), modName + ": disable all effects of the mod");
        b.add(CBUGameRules.KEY_REPL.getTranslationKey(), modName + ": allow replace grass");
    }

}
