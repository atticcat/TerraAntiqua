package com.mr_trousers.terraantiqua.common.blockentities;

import java.util.HashSet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import com.mr_trousers.terraantiqua.common.blocks.devices.FiremouthBlock;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlockEntities;
import net.dries007.tfc.common.blockentities.TFCBlockEntity;

public class WellholeBlockEntity extends TFCBlockEntity
{
    public static void serverTick(Level level, BlockPos pos, BlockState state, WellholeBlockEntity wellhole)
    {
        if (wellhole.isLit)
        {
            if (wellhole.burnTicks > 0)
            {
                wellhole.burnTicks--;
            }
            else
            {
                wellhole.finishFiring(state);
            }
        }
    }

    private int burnTicks = 0;
    private boolean isLit = false;
    private HashSet<FiremouthBlockEntity> firemouthSet;

    public WellholeBlockEntity(BlockPos pos, BlockState state)
    {
        super(AntiquaBlockEntities.WELLHOLE.get(), pos, state);
    }

    public boolean isLit() { return isLit; }

    public void finishFiring(BlockState state)
    {
        assert level != null;
        level.setBlockAndUpdate(worldPosition, state.setValue(FiremouthBlock.LIT, false));
        isLit = false;
        burnTicks = 0;
    }

    public boolean startFiring(BlockState state)
    {
        assert level != null;

        isLit = true;
        burnTicks += 100;
        return true;
    }

    public void abortFiring(BlockState state)
    {
        isLit = false;
        burnTicks = 0;
    }
}
