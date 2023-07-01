package phoupraw.mcmod.cancelblockupdate;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class CancelBlockUpdate {

    public static final String MOD_ID = "cancelblockupdate";
    public static final Logger LOGGER = LogManager.getLogger();

    /**
     stupid, fooly, shit, hell, damn mojang and log4j
     */
    public static void debug(Object msg) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            LOGGER.info(msg);
        }
    }

}
