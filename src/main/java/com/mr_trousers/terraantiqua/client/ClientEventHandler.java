package com.mr_trousers.terraantiqua.client;

import com.mr_trousers.terraantiqua.TerraAntiqua;
//import com.mr_trousers.terraantiqua.client.render.FirepitBlockEntityRenderer;
import com.mr_trousers.terraantiqua.client.screen.AntiquaFirepitScreen;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlockEntities;
import com.mr_trousers.terraantiqua.common.registry.AntiquaContainerTypes;
import net.dries007.tfc.client.screen.FirepitScreen;
import net.dries007.tfc.common.container.TFCContainerTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientEventHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    //public static final ResourceLocation FIREPIT_LOG_LIT = new ResourceLocation(TerraAntiqua.MODID, "block/firepit_log_lit");
    //public static final ResourceLocation FIREPIT_LOG_UNLIT = new ResourceLocation(TerraAntiqua.MODID, "block/firepit_log_unlit");

    public static void init() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEventHandler::clientSetup);
        //bus.addListener(ClientEventHandler::registerEntityRenderers);
        //bus.addListener(ClientEventHandler::registerItemModels);
    }

    public static void clientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            MenuScreens.register(AntiquaContainerTypes.FIREPIT.get(), AntiquaFirepitScreen::new);
        });
    }

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        //event.registerBlockEntityRenderer(AntiquaBlockEntities.FIREPIT.get(), ctx -> new FirepitBlockEntityRenderer());
    }

    public static void registerItemModels(ModelRegistryEvent event) {
        //ForgeModelBakery.addSpecialModel(FIREPIT_LOG_LIT);
        //ForgeModelBakery.addSpecialModel(FIREPIT_LOG_UNLIT);
    }
}
