package nadiendev.ae2nometeorcraters;

import nadiendev.ae2nometeorcraters.config.NoMeteorCratersConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(AE2NoMeteorCraters.MODID)
public class AE2NoMeteorCraters {

    public static final String MODID = "ae2nometeorcraters";

    public AE2NoMeteorCraters(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.LOCAL, NoMeteorCratersConfig.SPEC);
    }
}
