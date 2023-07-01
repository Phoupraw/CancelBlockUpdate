package phoupraw.mcmod.cancelblockupdate.inject;

import net.minecraft.world.GameRules;

public interface IGameRules_Type {

    //@Contract(value = "_ -> param1", pure = true)
    //@SuppressWarnings("unchecked")
    //static <T extends GameRules.Rule<T>> GameRules$IType<T> cast(GameRules.Type<T> type) {
    //    return (GameRules$IType<T>) type;
    //}
    default <T extends GameRules.Rule<T>> GameRules.Key<T> getKey() {
        throw new IllegalStateException();
    }
    default <T extends GameRules.Rule<T>> void setKey(GameRules.Key<T> key) {
        throw new IllegalStateException();
    }

}
