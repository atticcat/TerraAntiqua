package com.mr_trousers.terraantiqua.common.blocks.devices;

import com.mr_trousers.terraantiqua.common.AntiquaTags;
import com.mr_trousers.terraantiqua.common.blockentities.AntiquaFirepitBlockEntity;
import com.mr_trousers.terraantiqua.common.blocks.AntiquaBlockStateProperties;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.devices.FirepitBlock;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class AntiquaFirepitBlock extends FirepitBlock {

    public static final BooleanProperty HAS_WOOD_ASH = AntiquaBlockStateProperties.HAS_WOOD_ASH;
    public static final IntegerProperty NUM_LOGS = AntiquaBlockStateProperties.NUM_LOGS;

    public AntiquaFirepitBlock(ExtendedProperties properties) {
        super(properties);

        registerDefaultState(getStateDefinition().any()
            .setValue(HAS_WOOD_ASH, false)
            .setValue(NUM_LOGS, 0)
            .setValue(LIT, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(HAS_WOOD_ASH);
        builder.add(NUM_LOGS);
        super.createBlockStateDefinition(builder);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        final AntiquaFirepitBlockEntity firepit = Helpers.getBlockEntity(world, pos, AntiquaFirepitBlockEntity.class);
        if (firepit != null)
        {
            final ItemStack stack = player.getItemInHand(hand);
            //todo: change into antiqua versions of pot or grill firepit
            if (stack.getItem() == TFCItems.POT.get() || stack.getItem() == TFCItems.WROUGHT_IRON_GRILL.get())
            {
                if (!world.isClientSide)
                {
                    AbstractFirepitBlockEntity.convertTo(world, pos, state, firepit, stack.getItem() == TFCItems.POT.get() ? TFCBlocks.POT.get() : TFCBlocks.GRILL.get());
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            else if (TFCTags.Items.EXTINGUISHER.contains(stack.getItem()) && state.getValue(LIT))
            {
                firepit.extinguish(state);
                return InteractionResult.SUCCESS;
            }
            else if (TFCTags.Items.FIREPIT_LOGS.contains(stack.getItem()) && firepit.canAddFuel())
            {
                firepit.addFuel(state, stack);
                if (!world.isClientSide()) {firepit.markForBlockUpdate();}
                return InteractionResult.SUCCESS;
            }
            else if (AntiquaTags.Items.SHOVELS.contains(stack.getItem()) && state.getValue(HAS_WOOD_ASH))
            {
                firepit.ejectWoodAsh(state);
                return InteractionResult.SUCCESS;
            }
            else
            {
                if (player instanceof ServerPlayer serverPlayer)
                {
                    NetworkHooks.openGui(serverPlayer, firepit, pos);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
