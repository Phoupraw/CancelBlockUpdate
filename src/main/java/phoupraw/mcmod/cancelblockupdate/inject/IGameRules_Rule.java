package phoupraw.mcmod.cancelblockupdate.inject;

import net.minecraft.world.GameRules;

public interface IGameRules_Rule {

    default <T extends GameRules.Rule<T>> GameRules.Type<T> getType() {
        throw new IllegalStateException();
    }

}
