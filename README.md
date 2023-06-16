[![Modrinth](https://img.shields.io/modrinth/dt/cancel-block-update?logo=modrinth&label=&suffix=%20&style=flat&color=242629&labelColor=5ca424&logoColor=1c1c1c)](https://modrinth.com/mod/cancel-block-update)
![Minecraft](https://img.shields.io/badge/Available%20for-MC%201.19~1.20.1-c70039)

[![Fabric](https://cdn.discordapp.com/attachments/705864145169416313/969720133998239794/fabric_supported.png)](https://fabricmc.net/)

# 取消方块更新

取消方块更新、方块计划刻、随机刻、流体计划刻，允许无条件放置方块。

添加了一条游戏规则`cancelblockupdate:off`：为false时，取消所有更新；为true时，允许所有更新。

添加了指令：

- `/cancelblockupdate random <pos>` 触发pos处方块的随机刻。
- `/cancelblockupdate schedule <pos>` 触发pos处方块的计划刻。

建议搭配调整方块状态的调试棒`/give @p debug_stick`使用。

用于建造特殊建筑、制作和测试特殊地图。