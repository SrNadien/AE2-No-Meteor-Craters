package nadiendev.ae2nometeorcraters.mixin;

import appeng.worldgen.meteorite.CraterType;
import appeng.worldgen.meteorite.MeteoriteStructurePiece;
import appeng.worldgen.meteorite.fallout.FalloutMode;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MeteoriteStructurePiece.class)
public class MeteoriteStructurePieceMixin {

    private static final String NEW_PIECE = "<init>(Lnet/minecraft/core/BlockPos;FLappeng/worldgen/meteorite/CraterType;Lappeng/worldgen/meteorite/fallout/FalloutMode;ZZ)V";
    private static final String SETTINGS_INIT = "Lappeng/worldgen/meteorite/PlacedMeteoriteSettings;<init>(Lnet/minecraft/core/BlockPos;FLappeng/worldgen/meteorite/CraterType;Lappeng/worldgen/meteorite/fallout/FalloutMode;ZZ)V";

    @ModifyArg(method = NEW_PIECE, at = @At(value = "INVOKE", target = SETTINGS_INIT), index = 0)
    private BlockPos ae2nometeorcraters$raiseToSurface(BlockPos pos, float radius, CraterType craterType,
            FalloutMode fallout, boolean pureCrater, boolean craterLake) {
        int buried = (int) Math.ceil(radius) + 1;
        int below = Math.min(8, (int) Math.ceil(radius / Math.sqrt(0.8)) - 1);
        return pos.above(buried + below);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = SETTINGS_INIT), index = 2)
    private CraterType ae2nometeorcraters$forceCraterTypeNone(CraterType original) {
        return CraterType.NONE;
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = SETTINGS_INIT), index = 5)
    private boolean ae2nometeorcraters$disableCraterLake(boolean original) {
        return false;
    }
}
