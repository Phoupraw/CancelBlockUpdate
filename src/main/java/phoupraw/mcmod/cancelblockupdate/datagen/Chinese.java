package phoupraw.mcmod.cancelblockupdate.datagen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import phoupraw.mcmod.cancelblockupdate.CancelBlockUpdate;
import phoupraw.mcmod.cancelblockupdate.registry.CBUGameRules;

@Environment(EnvType.CLIENT)
final class Chinese extends FabricLanguageProvider {

    Chinese(FabricDataGenerator dataGenerator) {
        super(dataGenerator, "zh_cn");
    }

    @Override
    public void generateTranslations(TranslationBuilder b) {
        String modName = "取消方块更新";
        b.add("modmenu.nameTranslation." + CancelBlockUpdate.MOD_ID, modName);
        b.add("modmenu.descriptionTranslation." + CancelBlockUpdate.MOD_ID, """
          §4§l【警告】§r在世界生成时启用本模组的效果可能会产生大量浮空方块，如果想生成正常的世界，请提前修改游戏规则。
          取消方块更新、方块计划刻、随机刻和流体计划刻，允许无条件放置方块。
          用于建造特殊建筑、制作和测试特殊地图。
          §l新增游戏规则：§r
          - §ocancelblockupdate:off§r：为§ofalse§r时，取消所有更新；为§otrue§r即原版；默认为§ofalse§r。
          - §ocancelblockupdate:replace§r：为§ofalse§r时，草、蕨等方块不能被直接替换；为§otrue§r即原版；默认为§ofalse§r。
          §l新增指令：§r
          - §o/cancelblockupdate random <pos>§r：触发§opos§r处方块的随机刻。
          - §o/cancelblockupdate schedule <pos>§r：触发§opos§r处方块的计划刻。
          建议搭配调整方块状态的调试棒使用。
          """);
        b.add(CBUGameRules.OFF.getTranslationKey(), modName + "：禁用模组全部效果");
        b.add(CBUGameRules.REPLACE.getTranslationKey(), modName + "：允许放置方块替换草、蕨");
    }

}
