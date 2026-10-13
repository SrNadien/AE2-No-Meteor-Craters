package nadiendev.ae2nometeorcraters.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class NoMeteorCratersConfig {

    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue DISABLE_CERTUS_QUARTZ;
    public static final ModConfigSpec.BooleanValue DISABLE_BUDDING_QUARTZ;
    public static final ModConfigSpec.BooleanValue DISABLE_MYSTERIOUS_CUBE;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("meteorites");
        DISABLE_CERTUS_QUARTZ = builder
                .comment("If true, the certus quartz blocks inside meteorites are replaced with sky stone.")
                .define("disableCertusQuartz", false);
        DISABLE_BUDDING_QUARTZ = builder
                .comment("If true, the budding certus quartz inside meteorites is replaced with sky stone and no quartz buds grow on it.")
                .define("disableBuddingQuartz", false);
        DISABLE_MYSTERIOUS_CUBE = builder
                .comment("If true, meteorites generate without the mysterious cube in their center.")
                .define("disableMysteriousCube", false);
        builder.pop();
        SPEC = builder.build();
    }

    private NoMeteorCratersConfig() {
    }

    public static boolean disableCertusQuartz() {
        return SPEC.isLoaded() && DISABLE_CERTUS_QUARTZ.get();
    }

    public static boolean disableBuddingQuartz() {
        return SPEC.isLoaded() && DISABLE_BUDDING_QUARTZ.get();
    }

    public static boolean disableMysteriousCube() {
        return SPEC.isLoaded() && DISABLE_MYSTERIOUS_CUBE.get();
    }
}
