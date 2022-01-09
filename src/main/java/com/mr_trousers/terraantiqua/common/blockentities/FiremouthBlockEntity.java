package com.mr_trousers.terraantiqua.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import com.mr_trousers.terraantiqua.common.blocks.devices.FiremouthBlock;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlockEntities;
import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.util.Helpers;

import static com.mr_trousers.terraantiqua.TerraAntiqua.MODID;


public class FiremouthBlockEntity extends InventoryBlockEntity<ItemStackHandler>
{
    private WellholeBlockEntity wellhole;
    private static final Component NAME = new TranslatableComponent(MODID + ".tile_entity.firemouth");

    public FiremouthBlockEntity(BlockPos pos, BlockState state)
    {
        super(AntiquaBlockEntities.FIREMOUTH.get(), pos, state, defaultInventory(1), NAME);
    }

    public void setWellhole(Level level, BlockPos pos) { this.wellhole = Helpers.getBlockEntity(level, pos, WellholeBlockEntity.class); }

    public WellholeBlockEntity getWellhole() { return wellhole; }

    //tell core block to try to start firing
    public boolean tryLight(BlockState state)
    {
        wellhole.startFiring(state);
        return true;
    }

    //light this firemouth
    protected void light(BlockState state)
    {
        assert level != null;
        level.setBlockAndUpdate(worldPosition, state.setValue(FiremouthBlock.LIT, true));
    }
}
