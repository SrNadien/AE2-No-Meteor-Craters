package nadiendev.ae2nometeorcraters.mixin;

import appeng.core.definitions.AEBlocks;
import appeng.decorative.solid.BuddingCertusQuartzBlock;
import appeng.decorative.solid.CertusQuartzClusterBlock;
import appeng.worldgen.meteorite.MeteoriteBlockPutter;
import appeng.worldgen.meteorite.MeteoritePlacer;
import nadiendev.ae2nometeorcraters.config.NoMeteorCratersConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MeteoritePlacer.class, remap = false)
public class MeteoriteCoreMixin {

    @Shadow @Final private BlockState skyStone;

    @Redirect(
        method = "placeMeteoriteSkyStone()V",
        at = @At(
            value = "INVOKE",
            target = "Lappeng/worldgen/meteorite/MeteoriteBlockPutter;put(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"
        )
    )
    private boolean ae2nometeorcraters$filterCore(MeteoriteBlockPutter putter, LevelAccessor level, BlockPos pos, BlockState state) {
        Block block = state.getBlock();
        boolean budding = NoMeteorCratersConfig.disableBuddingQuartz();
        if (budding && block instanceof CertusQuartzClusterBlock) {
            return false;
        }
        if (budding && block instanceof BuddingCertusQuartzBlock
                || NoMeteorCratersConfig.disableCertusQuartz() && state.is(AEBlocks.QUARTZ_BLOCK.block())) {
            return putter.put(level, pos, skyStone);
        }
        return putter.put(level, pos, state);
    }

    @Inject(method = "placeChest()V", at = @At("HEAD"), cancellable = true)
    private void ae2nometeorcraters$skipMysteriousCube(CallbackInfo ci) {
        if (NoMeteorCratersConfig.disableMysteriousCube()) {
            ci.cancel();
        }
    }
}
