package com.mr_trousers.terraantiqua.common.blockentities;

import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import com.mr_trousers.terraantiqua.common.blocks.devices.FiremouthBlock;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlockEntities;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlocks;
import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.util.Helpers;

import static com.mr_trousers.terraantiqua.TerraAntiqua.MODID;


public class FiremouthBlockEntity extends InventoryBlockEntity<ItemStackHandler>
{
    private BlockPos wellholePos;
    private static final Component NAME = new TranslatableComponent(MODID + ".tile_entity.firemouth");

    public FiremouthBlockEntity(BlockPos pos, BlockState state)
    {
        super(AntiquaBlockEntities.FIREMOUTH.get(), pos, state, defaultInventory(1), NAME);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt)
    {
        if (wellholePos != null) { nbt.put("wellhole", NbtUtils.writeBlockPos(wellholePos)); }
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt)
    {
        CompoundTag wellholeTag = nbt.getCompound("wellhole");
        if (!wellholeTag.isEmpty()) { wellholePos = NbtUtils.readBlockPos(wellholeTag); }
        else { wellholePos = null; }
        super.load(nbt);
    }

    public void searchWellholes(Level level, BlockPos center)
    {
        var mutable = new BlockPos.MutableBlockPos();
        for (var face : Direction.Plane.HORIZONTAL)
        {
            mutable.set(center).move(face, 2);
            triggerIfWellhole(mutable, level);
            triggerIfWellhole(mutable.move(face.getClockWise()), level);
            triggerIfWellhole(mutable.move(face.getCounterClockWise(), 2), level);
        }
    }

    private void triggerIfWellhole(BlockPos.MutableBlockPos pos, Level level)
    {
        BlockState state = level.getBlockState(pos);
        if (state.is(AntiquaBlocks.WELLHOLE.get()))
        {
            Objects.requireNonNull(Helpers.getBlockEntity(level, pos, WellholeBlockEntity.class)).linkFiremouths(level, pos);
        }
    }

    public void removeFromWellhole(BlockPos pos)
    {
        if (wellholePos != null)
        {
            assert level != null;
            BlockEntity entity = level.getBlockEntity(wellholePos);
            if (entity instanceof WellholeBlockEntity wellhole)
            {
                wellhole.removeFiremouth(pos);
            }
        }
    }

    public void setWellhole(BlockPos pos) {
        assert level != null;
        wellholePos = pos;
    }

    public void unsetWellhole() { wellholePos = null; }

    public BlockPos getWellhole() {
        assert level != null;
        return wellholePos;
    }

    //tell core block to try to start firing
    public boolean tryLight(Level level, BlockState state)
    {
        if (wellholePos != null && !level.isClientSide())
        {
            BlockEntity entity = level.getBlockEntity(wellholePos);
            if (entity instanceof WellholeBlockEntity wellhole && !state.getValue(FiremouthBlock.LIT))
            {
                return wellhole.startFiring();
            }
        }
        return false;
    }

    //light this firemouth
    public void light(BlockState state)
    {
        assert level != null;
        level.setBlockAndUpdate(worldPosition, state.setValue(FiremouthBlock.LIT, true));
    }

    public void extinguish(BlockState state)
    {
        assert level != null;
        level.setBlockAndUpdate(worldPosition, state.setValue(FiremouthBlock.LIT, false));
    }
}
