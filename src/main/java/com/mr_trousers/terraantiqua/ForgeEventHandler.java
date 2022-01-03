package com.mr_trousers.terraantiqua;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

import com.mr_trousers.terraantiqua.common.registry.AntiquaBlocks;
import net.dries007.tfc.common.blockentities.AbstractFirepitBlockEntity;
import net.dries007.tfc.util.events.StartFireEvent;

public class ForgeEventHandler
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(ForgeEventHandler::onFireStart);
    }

    public static void onFireStart(StartFireEvent event)
    {
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        Block block = state.getBlock();

        if (block == AntiquaBlocks.FIREPIT.get())
        {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof AbstractFirepitBlockEntity<?> firepit && firepit.light(state))
            {
                event.setCanceled(true);
            }
        }
    }
}
