package com.mr_trousers.terraantiqua.common.blocks.devices;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.mr_trousers.terraantiqua.TerraAntiqua;
import com.mr_trousers.terraantiqua.common.blockentities.FiremouthBlockEntity;
import com.mr_trousers.terraantiqua.common.blockentities.WellholeBlockEntity;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlocks;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.ExtendedBlock;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.MultiBlock;

public class WellholeBlock extends ExtendedBlock
{
    private static final MultiBlock KILN_MULTIBLOCK;

    static {
        BlockPos origin = BlockPos.ZERO;
        Tag.Named<Block> kilnBlock = TFCTags.Blocks.FORGE_INSULATION;
        BiPredicate<LevelAccessor, BlockPos> isWall = (world, pos) -> world.getBlockState(pos).is(kilnBlock);

        //todo: add requirement for air in front of firemouths, also... add the rest of the structure
        KILN_MULTIBLOCK = new MultiBlock()
            .matchHorizontal(origin, isWall, 1)
            .matchEachDirection(origin.north(), isWall, new Direction[]{Direction.EAST, Direction.WEST}, 1)
            .matchEachDirection(origin.south(), isWall, new Direction[]{Direction.EAST, Direction.WEST}, 1)
            .matchHorizontal(origin, isWall, 2)
            .match(origin.north(2).east(), state -> state.is(kilnBlock) ||
                (state.is(AntiquaBlocks.FIREMOUTH.get()) && state.getValue(FiremouthBlock.FACING) == Direction.NORTH))
            .match(origin.north(2).west(), state -> state.is(kilnBlock) ||
                (state.is(AntiquaBlocks.FIREMOUTH.get()) && state.getValue(FiremouthBlock.FACING) == Direction.NORTH))
            .match(origin.south(2).east(), state -> state.is(kilnBlock) ||
                (state.is(AntiquaBlocks.FIREMOUTH.get()) && state.getValue(FiremouthBlock.FACING) == Direction.SOUTH))
            .match(origin.south(2).west(), state -> state.is(kilnBlock) ||
                (state.is(AntiquaBlocks.FIREMOUTH.get()) && state.getValue(FiremouthBlock.FACING) == Direction.SOUTH))
            .match(origin.east(2).north(), state -> state.is(kilnBlock) ||
                (state.is(AntiquaBlocks.FIREMOUTH.get()) && state.getValue(FiremouthBlock.FACING) == Direction.EAST))
            .match(origin.east(2).south(), state -> state.is(kilnBlock) ||
                (state.is(AntiquaBlocks.FIREMOUTH.get()) && state.getValue(FiremouthBlock.FACING) == Direction.EAST))
            .match(origin.west(2).north(), state -> state.is(kilnBlock) ||
                (state.is(AntiquaBlocks.FIREMOUTH.get()) && state.getValue(FiremouthBlock.FACING) == Direction.WEST))
            .match(origin.west(2).south(), state -> state.is(kilnBlock) ||
                (state.is(AntiquaBlocks.FIREMOUTH.get()) && state.getValue(FiremouthBlock.FACING) == Direction.WEST));
    }

    public static boolean isValid(LevelAccessor level, BlockPos pos) { return KILN_MULTIBLOCK.test(level, pos); }

    public WellholeBlock(ExtendedProperties properties)
    {
        super(properties);
    }

    public HashSet<BlockPos> linkFiremouths(Level level, BlockPos center) {
        var firemouths = new HashSet<BlockPos>();
        var mutable = new BlockPos.MutableBlockPos();
        for (var face : Direction.Plane.HORIZONTAL) {
            mutable.set(center).move(face, 2);
            addIfFiremouth(center, mutable, firemouths, level);
            addIfFiremouth(center, mutable.move(face.getClockWise()), firemouths, level);
            // Move two because we moved it clockwise on the previous line
            addIfFiremouth(center, mutable.move(face.getCounterClockWise(), 2), firemouths, level);
        }
        return firemouths;
    }

    private void addIfFiremouth(BlockPos center, BlockPos.MutableBlockPos pos, HashSet<BlockPos> firemouths, Level level) {
        BlockState state = level.getBlockState(pos);
        if (state.is(AntiquaBlocks.FIREMOUTH.get()))
        {
            firemouths.add(pos.immutable());
            Objects.requireNonNull(Helpers.getBlockEntity(level, pos, FiremouthBlockEntity.class)).setWellhole(level, center);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        WellholeBlockEntity wellhole = Helpers.getBlockEntity(level, pos, WellholeBlockEntity.class);
        if (wellhole != null && isValid(level, pos))
        {
            LOGGER.info("wellhole used successfully");
            return InteractionResult.SUCCESS;
        }
        LOGGER.info("wellhole used unsuccessfully");
        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rand)
    {
        WellholeBlockEntity wellhole = Helpers.getBlockEntity(level, pos, WellholeBlockEntity.class);
        if (wellhole != null && wellhole.isLit() && !isValid(level, pos))
        {
            wellhole.abortFiring(state);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onPlace(BlockState p_60566_, Level p_60567_, BlockPos p_60568_, BlockState p_60569_, boolean p_60570_)
    {
        super.onPlace(p_60566_, p_60567_, p_60568_, p_60569_, p_60570_);
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_)
    {
        super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
    }
}
