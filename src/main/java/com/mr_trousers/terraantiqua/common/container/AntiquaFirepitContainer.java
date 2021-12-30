package com.mr_trousers.terraantiqua.common.container;

import com.mr_trousers.terraantiqua.common.blockentities.AntiquaFirepitBlockEntity;
import com.mr_trousers.terraantiqua.common.registry.AntiquaContainerTypes;
import net.dries007.tfc.common.container.BlockEntityContainer;
import net.dries007.tfc.common.container.CallbackSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;

import static net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity.SLOT_FUEL_INPUT;

public class AntiquaFirepitContainer extends BlockEntityContainer<AntiquaFirepitBlockEntity>
{
    public static AntiquaFirepitContainer create(AntiquaFirepitBlockEntity tile, Inventory playerInv, int windowId)
    {
        return new AntiquaFirepitContainer(tile, windowId).init(playerInv, 20);
    }

    private AntiquaFirepitContainer(AntiquaFirepitBlockEntity tile, int windowId)
    {
        super(AntiquaContainerTypes.FIREPIT.get(), windowId, tile);

        addDataSlots(tile.getSyncableData());
    }

    @Override
    protected boolean moveStack(ItemStack stack, int slotIndex)
    {
        return switch (typeOf(slotIndex))
                {
                    case MAIN_INVENTORY, HOTBAR -> !moveItemStackTo(stack, SLOT_FUEL_INPUT, AntiquaFirepitBlockEntity.SLOT_ITEM_INPUT + 1, false);
                    case CONTAINER -> !moveItemStackTo(stack, containerSlots, slots.size(), false);
                };
    }

    @Override
    protected void addContainerSlots()
    {
        blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            // fuel slots
            for (int i = 0; i < 4; i++)
            {
                addSlot(new CallbackSlot(blockEntity, handler, i, 8, 70 - 18 * i));
            }
            addSlot(new CallbackSlot(blockEntity, handler, AntiquaFirepitBlockEntity.SLOT_ITEM_INPUT, 80, 29));
            addSlot(new CallbackSlot(blockEntity, handler, AntiquaFirepitBlockEntity.SLOT_OUTPUT_1, 71, 57));
            addSlot(new CallbackSlot(blockEntity, handler, AntiquaFirepitBlockEntity.SLOT_OUTPUT_2, 89, 57));
        });
    }

}
