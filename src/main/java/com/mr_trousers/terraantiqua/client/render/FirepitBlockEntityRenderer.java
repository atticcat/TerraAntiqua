package com.mr_trousers.terraantiqua.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mr_trousers.terraantiqua.client.ClientEventHandler;
import com.mr_trousers.terraantiqua.common.blockentities.AntiquaFirepitBlockEntity;
import net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity;
import net.dries007.tfc.common.blockentities.FirepitBlockEntity;
import net.dries007.tfc.common.blocks.devices.FirepitBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.PistonHeadRenderer;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.items.CapabilityItemHandler;

import static net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity.SLOT_FUEL_CONSUME;
import static net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity.SLOT_FUEL_INPUT;

public class FirepitBlockEntityRenderer implements BlockEntityRenderer<AntiquaFirepitBlockEntity> {

    @Override
    public void render(AntiquaFirepitBlockEntity entity, float partialTicks, PoseStack stack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            int totalLogs = 0;
            for (int i = SLOT_FUEL_CONSUME; i <= SLOT_FUEL_INPUT; i++) {if (!cap.getStackInSlot(i).isEmpty()) {totalLogs++;}}
            if (totalLogs > 0) {
                for (int i = 0; i < totalLogs; i++) {
                    BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
                    ModelManager modelManager = blockRenderDispatcher.getBlockModelShaper().getModelManager();
                    stack.pushPose();
                    switch (i) {
                        case 0:
                            stack.translate(0.61F, 0.094F, 0.611F);
                            stack.mulPose(Vector3f.YP.rotationDegrees(45.0F));
                            break;
                        case 1:
                            stack.translate(0.333F, 0.094F, 0.495F);
                            stack.mulPose(Vector3f.YP.rotationDegrees(67.5F));
                            break;
                        case 2:
                            stack.translate(0.719F, 0.224F, 0.407F);
                            stack.mulPose(Vector3f.YN.rotationDegrees(90.0F));
                            stack.mulPose(Vector3f.ZP.rotationDegrees(22.5F));
                            break;
                        case 3:
                            stack.translate(0.345F, 0.281F, 0.566F);
                            stack.mulPose(Vector3f.YN.rotationDegrees(45.0F));
                            break;
                    }

                    ResourceLocation firepitLog = entity.getBlockState().getValue(FirepitBlock.LIT) ? ClientEventHandler.FIREPIT_LOG_LIT : ClientEventHandler.FIREPIT_LOG_UNLIT;
                    blockRenderDispatcher.getModelRenderer().renderModel(stack.last(), bufferSource.getBuffer(Sheets.cutoutBlockSheet()), (BlockState) null, modelManager.getModel(firepitLog), 1.0F, 1.0F, 1.0F, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
                    stack.popPose();
                }

            }
        });
    }
}
