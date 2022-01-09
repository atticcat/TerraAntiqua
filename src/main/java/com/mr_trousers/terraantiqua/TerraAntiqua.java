package com.mr_trousers.terraantiqua;

import com.mr_trousers.terraantiqua.client.ClientEventHandler;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlockEntities;
import com.mr_trousers.terraantiqua.common.registry.AntiquaContainerTypes;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlocks;
import com.mr_trousers.terraantiqua.common.registry.AntiquaFluids;
import com.mr_trousers.terraantiqua.common.registry.AntiquaItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TerraAntiqua.MODID)
public class TerraAntiqua {
    public static final String MODID = "terraantiqua";
    private static final Logger LOGGER = LogManager.getLogger();

    public TerraAntiqua() {

        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);

        AntiquaItems.ITEMS.register(bus);
        AntiquaBlocks.BLOCKS.register(bus);
        AntiquaFluids.FLUIDS.register(bus);
        AntiquaBlockEntities.TILE_ENTITIES.register(bus);
        AntiquaContainerTypes.CONTAINERS.register(bus);

        ForgeEventHandler.init();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientEventHandler.init();
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
    }
}