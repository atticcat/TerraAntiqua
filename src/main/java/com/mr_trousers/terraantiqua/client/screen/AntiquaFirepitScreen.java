package com.mr_trousers.terraantiqua.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mr_trousers.terraantiqua.common.blockentities.AntiquaFirepitBlockEntity;
import com.mr_trousers.terraantiqua.common.container.AntiquaFirepitContainer;
import net.dries007.tfc.client.screen.BlockEntityScreen;
import net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity;
import net.dries007.tfc.common.capabilities.heat.Heat;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;

public class AntiquaFirepitScreen extends BlockEntityScreen<AntiquaFirepitBlockEntity, AntiquaFirepitContainer>
{
    private static final ResourceLocation FIREPIT = new ResourceLocation(MOD_ID, "textures/gui/fire_pit.png");

    public AntiquaFirepitScreen(AntiquaFirepitContainer container, Inventory playerInventory, Component name)
    {
        super(container, playerInventory, name, FIREPIT);
        inventoryLabelY += 20;
        imageHeight += 20;
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        super.renderBg(matrixStack, partialTicks, mouseX, mouseY);
        int temp = (int) (51 * tile.getSyncableData().get(AbstractFirepitBlockEntity.DATA_SLOT_TEMPERATURE) / Heat.maxVisibleTemperature());
        if (temp > 0)
        {
            blit(matrixStack, leftPos + 30, topPos + 76 - Math.min(51, temp), 176, 0, 15, 5);
        }
    }
}
