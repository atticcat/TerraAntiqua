package com.mr_trousers.terraantiqua.common.blocks.devices;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

import com.mr_trousers.terraantiqua.common.blockentities.FiremouthBlockEntity;
import com.mr_trousers.terraantiqua.common.blockentities.WellholeBlockEntity;
import net.dries007.tfc.common.blocks.EntityBlockExtension;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.DeviceBlock;
import net.dries007.tfc.util.Helpers;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.core.Direction.NORTH;

public class FiremouthBlock extends DeviceBlock implements EntityBlockExtension
{

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public FiremouthBlock(ExtendedProperties properties)
    {
        super(properties, InventoryRemoveBehavior.DROP);
        registerDefaultState(getStateDefinition().any()
            .setValue(LIT, false)
            .setValue(FACING, NORTH)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(LIT);
        builder.add(FACING);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        FiremouthBlockEntity firemouth = Helpers.getBlockEntity(level, pos, FiremouthBlockEntity.class);
        if (firemouth != null)
        {
            BlockPos wellhole = firemouth.getWellhole();
            if (wellhole != null)
            {
                if (WellholeBlock.isValid(level, wellhole))
                {
                    LOGGER.info("firemouth structure valid");
                }
                BlockEntity entity = level.getBlockEntity(wellhole);
                if (entity instanceof WellholeBlockEntity)
                {
                    LOGGER.info("firemouth has a wellhole");
                }
                return InteractionResult.SUCCESS;
            }
        }
        LOGGER.info("firemouth used unsuccessfully)");
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        LOGGER.info("firemouth onPlace");
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof FiremouthBlockEntity firemouth)
        {
            firemouth.searchWellholes(level, pos);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        LOGGER.info("firemouth on Remove");
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof FiremouthBlockEntity firemouth)
        {
            firemouth.removeFromWellhole(pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
