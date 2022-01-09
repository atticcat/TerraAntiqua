package com.mr_trousers.terraantiqua.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.dries007.tfc.util.MultiBlock;

/**
 *not usable until Multiblock.conditions gets protected access
 *todo: add "matchAll" method, like matchOneOf (takes a sub multiblock) but requires all conditions (so it can be fed into matchOneOf)
 */
public class AntiquaMultiBlock extends MultiBlock
{
    private final List<BiPredicate<LevelAccessor, BlockPos>> conditions;

    public AntiquaMultiBlock() { this.conditions = new ArrayList<>(); }

    public AntiquaMultiBlock matchEachDirection(BlockPos posOffset, Predicate<BlockState> stateMatcher, Direction[] directions, int relativeAmount)
    {
        for (Direction d : directions)
        {
            conditions.add((world, pos) -> stateMatcher.test(world.getBlockState(pos.offset(posOffset).relative(d, relativeAmount))));
        }
        return this;
    }

    public AntiquaMultiBlock match(BlockPos posOffset, BiPredicate<LevelAccessor, BlockPos> condition)
    {
        super.match(posOffset, condition);
        return this;
    }

    public AntiquaMultiBlock match(BlockPos posOffset, Predicate<BlockState> stateMatcher)
    {
        super.match(posOffset, stateMatcher);
        return this;
    }

    public AntiquaMultiBlock matchEachDirection(BlockPos posOffset, BiPredicate<LevelAccessor, BlockPos> condition, Direction[] directions, int relativeAmount)
    {
        super.matchEachDirection(posOffset, condition, directions, relativeAmount);
        return this;
    }

    public AntiquaMultiBlock matchHorizontal(BlockPos posOffset, BiPredicate<LevelAccessor, BlockPos> condition, int relativeAmount)
    {
        super.matchHorizontal(posOffset, condition, relativeAmount);
        return this;
    }

    public <T extends BlockEntity> AntiquaMultiBlock match(BlockPos posOffset, Predicate<T> tileEntityPredicate, BlockEntityType<T> type)
    {
        super.match(posOffset, tileEntityPredicate, type);
        return this;
    }

    public AntiquaMultiBlock matchOneOf(BlockPos baseOffset, MultiBlock subMultiBlock)
    {
        super.matchOneOf(baseOffset, subMultiBlock);
        return this;
    }
}
