package phoupraw.mcmod.cancelblockupdate.inject;

import net.minecraft.world.GameRules;

public interface IGameRules_Type {

    default <T extends GameRules.Rule<T>> GameRules.Key<T> getKey() {
        throw new IllegalStateException();
    }
    default <T extends GameRules.Rule<T>> void setKey(GameRules.Key<T> key) {
        throw new IllegalStateException();
    }

}
