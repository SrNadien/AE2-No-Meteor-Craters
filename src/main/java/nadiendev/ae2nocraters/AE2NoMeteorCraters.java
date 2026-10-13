package nadiendev.ae2nometeorcraters;

import nadiendev.ae2nometeorcraters.config.NoMeteorCratersConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(AE2NoMeteorCraters.MODID)
public class AE2NoMeteorCraters {

    public static final String MODID = "ae2nometeorcraters";

    public AE2NoMeteorCraters() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NoMeteorCratersConfig.SPEC);
    }
}
