package nadiendev.ae2nometeorcraters.mixin;

import appeng.worldgen.meteorite.MeteoritePlacer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MeteoritePlacer.class, remap = false)
public class MeteoritePlacerMixin {

    @Shadow @Final private LevelAccessor level;
    @Shadow @Final private BoundingBox boundingBox;
    @Shadow @Final private int x;
    @Shadow @Final private int y;
    @Shadow @Final private int z;
    @Shadow @Final private double meteoriteSize;
    @Shadow @Final private boolean placeCrater;

    @Inject(method = "place()V", at = @At("HEAD"))
    private void ae2nometeorcraters$clearTrees(CallbackInfo ci) {
        if (placeCrater) {
            return;
        }

        BoundingBox writable = new BoundingBox(boundingBox.minX() - 16, y - 64, boundingBox.minZ() - 16,
                boundingBox.maxX() + 16, y + 128, boundingBox.maxZ() + 16);
        double reach = meteoriteSize + 2;
        double reachSq = reach * reach;
        int horizontal = (int) Math.ceil(reach / Math.sqrt(0.7));

        List<BlockPos> seeds = new ArrayList<>();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int i = x - horizontal; i <= x + horizontal; i++) {
            for (int k = z - horizontal; k <= z + horizontal; k++) {
                double dx = i - x;
                double dz = k - z;
                if ((dx * dx + dz * dz) * 0.7 >= reachSq) {
                    continue;
                }
                for (int j = y - (int) Math.ceil(reach); j <= y + 48; j++) {
                    cursor.set(i, j, k);
                    if (!writable.isInside(cursor)) {
                        continue;
                    }
                    BlockState state = level.getBlockState(cursor);
                    if (ae2nometeorcraters$isTrunk(state)) {
                        seeds.add(cursor.immutable());
                    } else if (ae2nometeorcraters$isNaturalLeaves(state)) {
                        BlockPos trunk = ae2nometeorcraters$findTrunk(cursor.immutable(), writable);
                        seeds.add(trunk != null ? trunk : cursor.immutable());
                    }
                }
            }
        }

        if (!seeds.isEmpty()) {
            ae2nometeorcraters$fell(seeds, writable);
        }
    }

    private BlockPos ae2nometeorcraters$findTrunk(BlockPos leaf, BoundingBox writable) {
        BlockPos current = leaf;
        for (int step = 0; step < 7; step++) {
            int distance = level.getBlockState(current).getValue(LeavesBlock.DISTANCE);
            BlockPos next = null;
            for (Direction direction : Direction.values()) {
                BlockPos neighbor = current.relative(direction);
                if (!writable.isInside(neighbor)) {
                    continue;
                }
                BlockState state = level.getBlockState(neighbor);
                if (distance == 1 && ae2nometeorcraters$isTrunk(state)) {
                    return neighbor;
                }
                if (ae2nometeorcraters$isNaturalLeaves(state) && state.getValue(LeavesBlock.DISTANCE) == distance - 1) {
                    next = neighbor;
                    break;
                }
            }
            if (next == null) {
                return null;
            }
            current = next;
        }
        return null;
    }

    private void ae2nometeorcraters$fell(List<BlockPos> seeds, BoundingBox writable) {
        Set<BlockPos> visited = new HashSet<>(seeds);
        ArrayDeque<BlockPos> queue = new ArrayDeque<>(seeds);
        List<BlockPos> removed = new ArrayList<>();

        while (!queue.isEmpty() && removed.size() < 8192) {
            BlockPos pos = queue.poll();
            BlockState state = level.getBlockState(pos);
            boolean trunk = ae2nometeorcraters$isTrunk(state);
            if (!trunk && !ae2nometeorcraters$isNaturalLeaves(state)) {
                continue;
            }
            int distance = trunk ? 0 : state.getValue(LeavesBlock.DISTANCE);

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) {
                            continue;
                        }
                        boolean face = Math.abs(dx) + Math.abs(dy) + Math.abs(dz) == 1;
                        BlockPos neighbor = pos.offset(dx, dy, dz);
                        if (!writable.isInside(neighbor) || visited.contains(neighbor)) {
                            continue;
                        }
                        BlockState next = level.getBlockState(neighbor);
                        boolean follow = trunk
                                ? ae2nometeorcraters$isTrunk(next)
                                        || face && ae2nometeorcraters$isNaturalLeaves(next) && next.getValue(LeavesBlock.DISTANCE) == 1
                                : face && ae2nometeorcraters$isNaturalLeaves(next) && next.getValue(LeavesBlock.DISTANCE) == distance + 1;
                        if (follow) {
                            visited.add(neighbor);
                            queue.add(neighbor);
                        }
                    }
                }
            }

            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
            removed.add(pos);
        }

        ArrayDeque<BlockPos> loose = new ArrayDeque<>(removed);
        while (!loose.isEmpty()) {
            BlockPos pos = loose.poll();
            for (Direction direction : Direction.values()) {
                BlockPos neighbor = pos.relative(direction);
                if (!writable.isInside(neighbor)) {
                    continue;
                }
                BlockState state = level.getBlockState(neighbor);
                if (!state.isAir() && !state.canSurvive(level, neighbor)) {
                    level.setBlock(neighbor, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
                    loose.add(neighbor);
                }
            }
        }
    }

    private static boolean ae2nometeorcraters$isTrunk(BlockState state) {
        return state.is(BlockTags.LOGS) || state.is(Blocks.MUSHROOM_STEM) || state.is(Blocks.BROWN_MUSHROOM_BLOCK)
                || state.is(Blocks.RED_MUSHROOM_BLOCK);
    }

    private static boolean ae2nometeorcraters$isNaturalLeaves(BlockState state) {
        return state.is(BlockTags.LEAVES) && state.hasProperty(LeavesBlock.DISTANCE)
                && state.hasProperty(LeavesBlock.PERSISTENT) && !state.getValue(LeavesBlock.PERSISTENT);
    }
}
