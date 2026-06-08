package nadiendev.ae2nometeorcraters.mixin;

import appeng.worldgen.meteorite.CraterType;
import appeng.worldgen.meteorite.MeteoriteStructurePiece;
import appeng.worldgen.meteorite.fallout.FalloutMode;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = MeteoriteStructurePiece.class, remap = false)
public class MeteoriteStructurePieceMixin {

    @ModifyArg(
        method = "<init>(Lnet/minecraft/core/BlockPos;FLappeng/worldgen/meteorite/CraterType;Lappeng/worldgen/meteorite/fallout/FalloutMode;ZZ)V",
        at = @At(
            value = "INVOKE",
            target = "Lappeng/worldgen/meteorite/PlacedMeteoriteSettings;<init>(Lnet/minecraft/core/BlockPos;FLappeng/worldgen/meteorite/CraterType;Lappeng/worldgen/meteorite/fallout/FalloutMode;ZZ)V"
        ),
        index = 2
    )
    private CraterType ae2nometeorcrates$forceCraterTypeNone(CraterType original) {
        return CraterType.NONE;
    }
}
