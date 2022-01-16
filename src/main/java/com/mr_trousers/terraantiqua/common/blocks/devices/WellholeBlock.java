package com.mr_trousers.terraantiqua.common.blocks.devices;

import java.util.Random;
import java.util.function.BiPredicate;

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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.mr_trousers.terraantiqua.common.blockentities.WellholeBlockEntity;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlocks;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.EntityBlockExtension;
import net.dries007.tfc.common.blocks.ExtendedBlock;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.MultiBlock;

public class WellholeBlock extends ExtendedBlock implements EntityBlockExtension
{
    private static final MultiBlock KILN_MULTIBLOCK;

    static {
        final Direction[] WEAST = new Direction[]{Direction.EAST, Direction.WEST};
        final Direction[] SNOURTH = new Direction[]{Direction.NORTH, Direction.SOUTH};
        BlockPos origin = BlockPos.ZERO;
        Tag.Named<Block> kilnBlock = TFCTags.Blocks.FORGE_INSULATION;
        BiPredicate<LevelAccessor, BlockPos> isWall = (world, pos) -> world.getBlockState(pos).is(kilnBlock);
        BiPredicate<LevelAccessor, BlockPos> isSaggar = (world, pos) -> world.getBlockState(pos).is(AntiquaBlocks.SAGGAR.get()) ||
            world.getBlockState(pos).is(AntiquaBlocks.UNFIRED_SAGGAR.get()) || world.getBlockState(pos).isAir();

        //todo: add requirement for air in front of firemouths, also... add the rest of the structure
        KILN_MULTIBLOCK = new MultiBlock()
            .matchHorizontal(origin, isWall, 1)
            .matchEachDirection(origin.north(), isWall, WEAST, 1)
            .matchEachDirection(origin.south(), isWall, WEAST, 1)
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
                (state.is(AntiquaBlocks.FIREMOUTH.get()) && state.getValue(FiremouthBlock.FACING) == Direction.WEST))
            .matchHorizontal(origin.above(), isSaggar, 1)
            .matchEachDirection(origin.above().north(), isSaggar, WEAST, 1)
            .matchEachDirection(origin.above().south(), isSaggar, WEAST, 1)
            .matchHorizontal(origin.above(2), isSaggar, 1)
            .matchEachDirection(origin.above(2).north(), isSaggar, WEAST, 1)
            .matchEachDirection(origin.above(2).south(), isSaggar, WEAST, 1)
            .matchHorizontal(origin.above(), isWall, 2)
            .matchEachDirection(origin.above().north(2), isWall, WEAST, 1)
            .matchEachDirection(origin.above().south(2), isWall, WEAST, 1)
            .matchEachDirection(origin.above().east(2), isWall, SNOURTH, 1)
            .matchEachDirection(origin.above().west(2), isWall, SNOURTH, 1)
            .matchEachDirection(origin.above(2).north(2), isWall, WEAST, 1)
            .matchEachDirection(origin.above(2).south(2), isWall, WEAST, 1)
            .matchEachDirection(origin.above(2).east(2), isWall, SNOURTH, 1)
            .matchEachDirection(origin.above(2).west(2), isWall, SNOURTH, 1);
    }

    public static boolean isValid(LevelAccessor level, BlockPos pos) { return KILN_MULTIBLOCK.test(level, pos); }

    public WellholeBlock(ExtendedProperties properties)
    {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        WellholeBlockEntity wellhole = Helpers.getBlockEntity(level, pos, WellholeBlockEntity.class);
        if (wellhole != null && !level.isClientSide())
        {
            if (isValid(level, pos))
            {
                LOGGER.info("wellhole structure valid");
            }
            LOGGER.info(wellhole.stringFiremouths());
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
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        WellholeBlockEntity wellhole = Helpers.getBlockEntity(level, pos, WellholeBlockEntity.class);
        LOGGER.info("wellhole onPlace");
        if (wellhole != null && !level.isClientSide())
        {
            LOGGER.info("wellhole onPlace 2");
            wellhole.linkFiremouths(level, pos);
        }
    }

//    @Override
//    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
//    {
//        BlockEntity entity = level.getBlockEntity(pos);
//        if (entity != null)
//        {
//            LOGGER.info("wellhole onPlace");
//            if (entity instanceof WellholeBlockEntity wellhole)
//            {
//                LOGGER.info("wellhole onPlace 2");
//                wellhole.linkFiremouths(level, pos);
//            }
//        }
//    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state2, boolean bool)
    {
        LOGGER.info("wellhole onRemove");
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof WellholeBlockEntity wellhole && !level.isClientSide())
        {
            wellhole.unlinkFiremouths(level);
        }
    }
}
