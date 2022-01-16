package com.mr_trousers.terraantiqua.common.blocks.devices;

import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import com.mr_trousers.terraantiqua.common.blocks.AntiquaBlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class UnfiredSaggarBlock extends Block
{
    public static final IntegerProperty ROTATION = AntiquaBlockStateProperties.ROTATION_4;

    public UnfiredSaggarBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(ROTATION, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(ROTATION);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        int random = ThreadLocalRandom.current().nextInt(0, 4);
        return this.defaultBlockState().setValue(ROTATION, random);
    }
}
