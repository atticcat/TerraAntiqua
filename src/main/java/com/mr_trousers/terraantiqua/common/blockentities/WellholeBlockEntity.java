package com.mr_trousers.terraantiqua.common.blockentities;

import java.util.HashSet;
import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import com.mr_trousers.terraantiqua.common.blocks.devices.FiremouthBlock;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlockEntities;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlocks;
import net.dries007.tfc.common.blockentities.TFCBlockEntity;
import net.dries007.tfc.util.Helpers;

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
    private HashSet<BlockPos> firemouths;

    public WellholeBlockEntity(BlockPos pos, BlockState state)
    {
        super(AntiquaBlockEntities.WELLHOLE.get(), pos, state);
        LOGGER.info("I'm constructing a wellhole entity");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt)
    {
        ListTag posList = new ListTag();
        for (BlockPos pos : firemouths)
        {
            CompoundTag posTag = new CompoundTag();
            posTag.putInt("x", pos.getX());
            posTag.putInt("y", pos.getY());
            posTag.putInt("z", pos.getZ());
            posList.add(posTag);
        }
        nbt.put("firemouths", posList);
        nbt.putInt("burnTicks", burnTicks);
        nbt.putBoolean("isLit", isLit);
        super.saveAdditional(nbt);
    }

    //todo: change to loadAdditional on next TFC release
    @Override
    public void load(CompoundTag nbt)
    {
        var firemouths = new HashSet<BlockPos>();
        ListTag posList = nbt.getList("firemouths", Tag.TAG_COMPOUND);
        for (Tag tag : posList)
        {
            if (tag instanceof CompoundTag posTag)
            {
                BlockPos pos = new BlockPos(posTag.getInt("x"), posTag.getInt("y"), posTag.getInt("z"));
                firemouths.add(pos);
            }
        }
        this.firemouths = firemouths;
        burnTicks = nbt.getInt("burnTicks");
        isLit = nbt.getBoolean("isLit");
        super.load(nbt);
    }


    public String stringFiremouths()
    {
        return firemouths.toString();
    }

    public void linkFiremouths(Level level, BlockPos center)
    {
        LOGGER.info("wellhole linking firemouths");
        var firemouths = new HashSet<BlockPos>();
        var mutable = new BlockPos.MutableBlockPos();
        for (var face : Direction.Plane.HORIZONTAL)
        {
            LOGGER.info("wellhole linking firemouths: checking "+face.toString());
            mutable.set(center).move(face, 2);
            addIfFiremouth(center, mutable, firemouths, level, face);
            addIfFiremouth(center, mutable.move(face.getClockWise()), firemouths, level, face);
            // Move two because we moved it clockwise on the previous line
            addIfFiremouth(center, mutable.move(face.getCounterClockWise(), 2), firemouths, level, face);
        }
        this.firemouths = firemouths;
    }

    private void addIfFiremouth(BlockPos center, BlockPos.MutableBlockPos pos, HashSet<BlockPos> firemouths, Level level, Direction face)
    {
        BlockState state = level.getBlockState(pos);
        if (state.is(AntiquaBlocks.FIREMOUTH.get()) && state.getValue(FiremouthBlock.FACING) == face)
        {
            LOGGER.info("wellhole found a firemouth to link");
            firemouths.add(pos.immutable());
            Objects.requireNonNull(Helpers.getBlockEntity(level, pos, FiremouthBlockEntity.class)).setWellhole(center);
        }
    }

    public void unlinkFiremouths(Level level)
    {
        if (firemouths != null)
        {
            for (BlockPos firemouthPos : firemouths)
            {
                if (firemouthPos != null)
                {
                    assert level != null;
                    BlockEntity entity = level.getBlockEntity(firemouthPos);
                    if (entity instanceof FiremouthBlockEntity firemouth)
                    {
                        firemouth.unsetWellhole();
                    }
                }
            }
        }
    }

    public void removeFiremouth(BlockPos pos)
    {
        firemouths.remove(pos);
    }

    public boolean isLit() { return isLit; }

    //todo: convert contents
    public void finishFiring(BlockState state)
    {
        for (BlockPos firemouthPos : firemouths)
        {
            if (firemouthPos != null)
            {
                assert level != null;
                BlockEntity entity = level.getBlockEntity(firemouthPos);
                if (entity instanceof FiremouthBlockEntity firemouth)
                {
                    firemouth.extinguish(firemouth.getBlockState());
                }
            }
        }

        isLit = false;
        burnTicks = 0;
    }

    //todo: check if there are enough firemouths for amount of saggars
    public boolean startFiring(BlockState state)
    {
        for (BlockPos firemouthPos : firemouths)
        {
            if (firemouthPos != null)
            {
                assert level != null;
                BlockEntity entity = level.getBlockEntity(firemouthPos);
                if (entity instanceof FiremouthBlockEntity firemouth)
                {
                    //todo: check if firemouth is fully fueled
                    firemouth.light(firemouth.getBlockState());
                }
            }
        }

        isLit = true;
        burnTicks += 100;
        return true;
    }

    public void abortFiring(BlockState state)
    {
        for (BlockPos firemouthPos : firemouths)
        {
            if (firemouthPos != null)
            {
                assert level != null;
                BlockEntity entity = level.getBlockEntity(firemouthPos);
                if (entity instanceof FiremouthBlockEntity firemouth)
                {
                    firemouth.extinguish(firemouth.getBlockState());
                }
            }
        }

        isLit = false;
        burnTicks = 0;
    }
}
