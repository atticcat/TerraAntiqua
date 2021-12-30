package com.mr_trousers.terraantiqua.common.blockentities;

import com.mr_trousers.terraantiqua.common.container.AntiquaFirepitContainer;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlockEntities;
import net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity;
import net.dries007.tfc.common.blockentities.FirepitBlockEntity;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;

public class AntiquaFirepitBlockEntity extends AbstractFirepitBlockEntity<ItemStackHandler> {

    public static final int SLOT_ITEM_INPUT = 4; // item to be cooked
    public static final int SLOT_OUTPUT_1 = 5; // generic output slot
    public static final int SLOT_OUTPUT_2 = 6; // extra output slot
    public static final int SLOT_OUTPUT_ASH = 7;

    private static final Component NAME = new TranslatableComponent(MOD_ID + ".tile_entity.firepit");

    @Nullable
    protected HeatingRecipe cachedRecipe;

    public AntiquaFirepitBlockEntity(BlockPos pos, BlockState state)
    {
        super(AntiquaBlockEntities.FIREPIT.get(), pos, state, defaultInventory(7), NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowID, Inventory playerInv, Player player)
    {
        return AntiquaFirepitContainer.create(this, playerInv, windowID);
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
        cachedRecipe = HeatingRecipe.getRecipe(new ItemStackInventory(inventory.getStackInSlot(FirepitBlockEntity.SLOT_ITEM_INPUT)));
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



    public boolean canAddFuel()
    {
        return inventory.getStackInSlot(SLOT_FUEL_INPUT).isEmpty();
    }

    public void addFuel(Player player, InteractionHand hand, ItemStack stack)
    {
        stack.shrink(1);
        inventory.setStackInSlot(SLOT_FUEL_INPUT, new ItemStack(stack.getItem(), 1));
        needsSlotUpdate = true;
    }
}
