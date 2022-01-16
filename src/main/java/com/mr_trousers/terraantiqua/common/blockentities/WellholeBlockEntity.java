package com.mr_trousers.terraantiqua.common.blockentities;

import java.util.HashSet;
import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import com.mr_trousers.terraantiqua.common.blocks.devices.FiremouthBlock;
import com.mr_trousers.terraantiqua.common.blocks.devices.SaggarBlock;
import com.mr_trousers.terraantiqua.common.blocks.devices.UnfiredSaggarBlock;
import com.mr_trousers.terraantiqua.common.blocks.devices.WellholeBlock;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlockEntities;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlocks;
import net.dries007.tfc.common.blockentities.TFCBlockEntity;
import net.dries007.tfc.util.Helpers;

public class WellholeBlockEntity extends TFCBlockEntity
{
    /*
     * earthenware 800C
     * stoneware 1200-1300C
     * porcelain 1300-1350C
     */
    private static final int logFiringTemperature = 1000;
    private static final int coalFiringTemperature = 1300;

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
    }

    @Override
    protected void saveAdditional(CompoundTag nbt)
    {
        ListTag posList = new ListTag();
        for (BlockPos pos : firemouths) { posList.add(NbtUtils.writeBlockPos(pos)); }
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
            if (tag instanceof CompoundTag posTag) { firemouths.add(NbtUtils.readBlockPos(posTag)); }
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
        var firemouths = new HashSet<BlockPos>();
        var mutable = new BlockPos.MutableBlockPos();
        for (var face : Direction.Plane.HORIZONTAL)
        {
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
            firemouths.add(pos.immutable());
            Objects.requireNonNull(Helpers.getBlockEntity(level, pos, FiremouthBlockEntity.class)).setWellhole(center.immutable());
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

    //todo: check if there is enough firemouth fuel for number of saggars
    public boolean startFiring()
    {
        if (WellholeBlock.isValid(level, worldPosition))
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
            burnTicks += 500;
            return true;
        }
        else
        {
            return false;
        }
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
        BlockPos.MutableBlockPos mutable = worldPosition.mutable();
        // todo: this while loop should eventually check that mutable is not a chimney block
        int i = 0;
        while (i < 4)
        {
            mutable.move(Direction.UP);
            for (Direction d : Direction.Plane.HORIZONTAL)
            {
                assert level != null;
                BlockPos pos = mutable.relative(d, 1);
                fireBlockContent(level, pos);
                fireBlockContent(level, pos.relative(d.getClockWise(), 1));
            }
            i++;
        }

        isLit = false;
        burnTicks = 0;
    }

    private void fireBlockContent(Level level, BlockPos pos)
    {
        LOGGER.info("firing a block at "+pos.toShortString()+" of "+level.getBlockState(pos));
        BlockState state = level.getBlockState(pos);
        if (state.is(AntiquaBlocks.UNFIRED_SAGGAR.get()))
        {
            LOGGER.info("firing saggar at "+pos.toShortString());
            level.setBlock(pos, AntiquaBlocks.SAGGAR.get().defaultBlockState().setValue(SaggarBlock.ROTATION, state.getValue(UnfiredSaggarBlock.ROTATION)), 3);
        }
        else if (state.is(AntiquaBlocks.SAGGAR.get()))
        {
            //todo: convert saggar contents. what the hell is a heat capability?
        }
    }
}
