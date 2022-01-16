package com.mr_trousers.terraantiqua.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.items.ItemStackHandler;

import com.mr_trousers.terraantiqua.common.registry.AntiquaBlockEntities;
import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.capabilities.InventoryItemHandler;

import static com.mr_trousers.terraantiqua.TerraAntiqua.MODID;

public class SaggarBlockEntity extends InventoryBlockEntity<ItemStackHandler>
{
    private static final Component NAME = new TranslatableComponent(MODID + ".tile_entity.saggar");

    public SaggarBlockEntity(BlockPos pos, BlockState state)
    {
        this(AntiquaBlockEntities.SAGGAR.get(), pos, state);
    }

    protected SaggarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state, self -> new InventoryItemHandler(self, 1), NAME);
    }
}
