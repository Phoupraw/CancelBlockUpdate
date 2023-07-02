package phoupraw.mcmod.cancelblockupdate.datagen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import phoupraw.mcmod.cancelblockupdate.CancelBlockUpdate;
import phoupraw.mcmod.cancelblockupdate.registry.CBUGameRules;

@Environment(EnvType.CLIENT)
final class English extends FabricLanguageProvider {

    English(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateTranslations(TranslationBuilder b) {
        String modName = "Cancel Block Update";
        b.add("modmenu.nameTranslation." + CancelBlockUpdate.MOD_ID, modName);
        b.add("modmenu.descriptionTranslation." + CancelBlockUpdate.MOD_ID, """
          §4§l[Warning]§r During world generation enable this mod might produce lots of floating blocks, if want to generate normal world, please change game rule in advance.
          Cancel block update, block schedule tick, random tick and fluid schedule tick, allow unlimited place block.
          For building special buildings, make and test special map.
          §lNew game rules:§r
          - §ocancelblockupdate:off§r: when §ofalse§r, cancel all block update; when §otrue§r it is vanilla; default §ofalse§r.
          - §ocancelblockupdate:replace§r: when §ofalse§r, grass, fern and so on block can't be replaced directly; when §otrue§r it is vanilla; default §ofalse§r.
          §lNew commands:§r
          - §o/cancelblockupdate random <pos>§r: trigger random tick of block at §opos§r.
          - §o/cancelblockupdate schedule <pos>§r：trigger schedule tick of block at §opos§r.
          Suggest with adapt block state debug stick to use.
          """);
        b.add(CBUGameRules.OFF.getTranslationKey(), modName + ": disable all effects of the mod");
        b.add(CBUGameRules.REPLACE.getTranslationKey(), modName + ": allow replace grass and fern");
    }

}
