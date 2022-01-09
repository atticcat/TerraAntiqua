package com.mr_trousers.terraantiqua.common.blockentities;

import com.mr_trousers.terraantiqua.common.blocks.devices.AntiquaFirepitBlock;
import com.mr_trousers.terraantiqua.common.container.AntiquaFirepitContainer;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlockEntities;
import net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;

public class AntiquaFirepitBlockEntity extends AbstractFirepitBlockEntity<ItemStackHandler> {

    public static final int SLOT_ITEM_INPUT = 4; // item to be cooked
    public static final int SLOT_OUTPUT_1 = 5; // generic output slot
    public static final int SLOT_OUTPUT_2 = 6; // extra output slot

    private static final Logger LOGGER = LogManager.getLogger();


    private static final Component NAME = new TranslatableComponent(MOD_ID + ".tile_entity.firepit");

    public static void serverTick(Level level, BlockPos pos, BlockState state, AntiquaFirepitBlockEntity firepit)
    {
        //todo: just have the property be changed every time a log is added or removed, probably cheaper
        BlockState finalState = state;
        firepit.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            int numLogs = 0;
            for (int i = SLOT_FUEL_CONSUME; i <= SLOT_FUEL_INPUT; i++) {if (!cap.getStackInSlot(i).isEmpty()) {numLogs++;}}
            if (finalState.getValue(AntiquaFirepitBlock.NUM_LOGS) != numLogs) {
                level.setBlockAndUpdate(firepit.worldPosition, finalState.setValue(AntiquaFirepitBlock.NUM_LOGS, numLogs));
            }
        });

        if (state.getValue(AntiquaFirepitBlock.LIT))
        {
            if (firepit.burnTicks > 0)
            {
                firepit.ashTicks++;
            }
            if (firepit.ashTicks >= 1000)
            {
                if (firepit.woodAshWaste < 16)
                {
                    firepit.woodAshWaste++;
                    if (!state.getValue(AntiquaFirepitBlock.HAS_WOOD_ASH))
                    {
                        state = state.setValue(AntiquaFirepitBlock.HAS_WOOD_ASH, true);
                        level.setBlockAndUpdate(firepit.worldPosition, state);
                    }
                }
                firepit.ashTicks -= 1000;
            }
        }
        AbstractFirepitBlockEntity.serverTick(level, pos, state, firepit);
    }

    @Nullable
    protected HeatingRecipe cachedRecipe;
    protected int woodAshWaste;
    protected int ashTicks;

    public AntiquaFirepitBlockEntity(BlockPos pos, BlockState state)
    {
        super(AntiquaBlockEntities.FIREPIT.get(), pos, state, defaultInventory(6), NAME);

        woodAshWaste = 0;
        ashTicks = 0;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        woodAshWaste = nbt.getInt("woodAshWaste");
        super.load(nbt);
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        nbt.putInt("woodAshWaste", woodAshWaste);
        return super.save(nbt);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowID, Inventory playerInv, Player player)
    {
        return AntiquaFirepitContainer.create(this, playerInv, windowID);
    }

    public boolean canAddFuel()
    {
        return inventory.getStackInSlot(SLOT_FUEL_INPUT).isEmpty();
    }

    public void addFuel(BlockState state, ItemStack stack)
    {
        stack.shrink(1);
        inventory.setStackInSlot(SLOT_FUEL_INPUT, new ItemStack(stack.getItem(), 1));
        needsSlotUpdate = true;
        //level.setBlockAndUpdate(worldPosition, state.setValue(AntiquaFirepitBlock.NUM_LOGS, state.getValue(AntiquaFirepitBlock.NUM_LOGS)+1));
    }

    public void ejectWoodAsh(BlockState state)
    {
        state = state.setValue(AntiquaFirepitBlock.HAS_WOOD_ASH, false);
        level.setBlockAndUpdate(worldPosition, state);
        Helpers.spawnItem(level, worldPosition, new ItemStack(TFCItems.POWDERS.get(Powder.WOOD_ASH).get(), woodAshWaste));
        woodAshWaste = 0;
    }

    @Override
    protected boolean consumeFuel()
    {
        if (super.consumeFuel())
        {
//            BlockState state = this.getBlockState();
//            level.setBlockAndUpdate(worldPosition, state.setValue(AntiquaFirepitBlock.NUM_LOGS, state.getValue(AntiquaFirepitBlock.NUM_LOGS) - 1));
            level.addParticle(ParticleTypes.FLAME, worldPosition.getX(), worldPosition.getY() + 0.5, worldPosition.getZ(), 0.0F, 0.1F, 0.0F);
            return true;
        } else
        {
            return false;
        }
    }

    @Override
    protected void handleCooking()
    {
        if (temperature > 0)
        {
            final ItemStack inputStack = inventory.getStackInSlot(SLOT_ITEM_INPUT);
            inputStack.getCapability(HeatCapability.CAPABILITY).ifPresent(cap -> {
                float itemTemp = cap.getTemperature();
                HeatCapability.addTemp(cap, temperature);

                if (cachedRecipe != null && cachedRecipe.isValidTemperature(itemTemp))
                {
                    HeatingRecipe recipe = cachedRecipe;
                    ItemStackInventory wrapper = new ItemStackInventory(inputStack);

                    // Clear input
                    inventory.setStackInSlot(SLOT_ITEM_INPUT, ItemStack.EMPTY);

                    // Handle outputs
                    mergeOutputStack(recipe.assemble(wrapper));
                    mergeOutputFluids(recipe.getOutputFluid(wrapper), cap.getTemperature());
                }
            });
        }
    }

    @Override
    protected void coolInstantly()
    {
        inventory.getStackInSlot(SLOT_ITEM_INPUT).getCapability(HeatCapability.CAPABILITY).ifPresent(cap -> cap.setTemperature(0f));
    }

    @Override
    protected void updateCachedRecipe()
    {
        assert level != null;
        cachedRecipe = HeatingRecipe.getRecipe(new ItemStackInventory(inventory.getStackInSlot(AntiquaFirepitBlockEntity.SLOT_ITEM_INPUT)));
    }

    /**
     * Merge an item stack into the two output slots
     */
    private void mergeOutputStack(ItemStack outputStack)
    {
        outputStack = inventory.insertItem(SLOT_OUTPUT_1, outputStack, false);
        if (outputStack.isEmpty())
        {
            return;
        }
        outputStack = inventory.insertItem(SLOT_OUTPUT_2, outputStack, false);
        if (outputStack.isEmpty())
        {
            return;
        }

        assert level != null;
        Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), outputStack);
    }

    /**
     * Merge a fluid stack into the two output slots, treating them as fluid containers, and optionally heat containers
     */
    private void mergeOutputFluids(FluidStack fluidStack, float temperature)
    {
        fluidStack = Helpers.mergeOutputFluidIntoSlot(inventory, fluidStack, temperature, SLOT_OUTPUT_1);
        Helpers.mergeOutputFluidIntoSlot(inventory, fluidStack, temperature, SLOT_OUTPUT_2);
        // Any remaining fluid is lost at this point
    }
}
