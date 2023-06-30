package phoupraw.mcmod.cancelblockupdate.datagen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import phoupraw.mcmod.cancelblockupdate.CancelBlockUpdate;
import phoupraw.mcmod.cancelblockupdate.registry.CBUGameRules;

@Environment(EnvType.CLIENT)
final class Chinese extends FabricLanguageProvider {

Chinese(FabricDataOutput dataOutput) {
    super(dataOutput, "zh_cn");
}

@Override
public void generateTranslations(TranslationBuilder b) {
    String modName = "取消方块更新";
    b.add("modmenu.nameTranslation." + CancelBlockUpdate.MOD_ID, modName);
    b.add("modmenu.descriptionTranslation." + CancelBlockUpdate.MOD_ID, "阻止方块更新、方块计划刻、随机刻、放置可行性检查、流体计划刻、铁轨自动转角。在世界生成时启用本模组的效果可能会产生大量浮空方块。可以用游戏规则" + CBUGameRules.OFF.getName() + "开关效果。");
    b.add(CBUGameRules.OFF.getTranslationKey(), modName + "：禁用模组全部效果");
    b.add(CBUGameRules.REPLACE.getTranslationKey(), modName + "：允许放置方块替换草、蕨");
}

}
