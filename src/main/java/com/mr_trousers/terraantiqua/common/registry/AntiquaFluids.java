package com.mr_trousers.terraantiqua.common.registry;

import com.mojang.datafixers.util.Pair;
import net.dries007.tfc.common.fluids.MoltenFluid;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.mr_trousers.terraantiqua.TerraAntiqua.MODID;
import static net.dries007.tfc.common.fluids.TFCFluids.*;

public class AntiquaFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MODID);

    public static final FluidPair<ForgeFlowingFluid> MOLTEN_ARSENIC = register(
            "metal/arsenic",
            "metal/flowing_arsenic",
            properties -> properties.block(AntiquaBlocks.MOLTEN_ARSENIC).bucket(AntiquaItems.MOLTEN_ARSENIC_BUCKET).explosionResistance(100),
            FluidAttributes.builder(MOLTEN_STILL, MOLTEN_FLOW)
                    .translationKey("fluid.terraantiqua.metal.lead")
                    .color(ALPHA_MASK | 0xFF949495)
                    .rarity(Rarity.COMMON)
                    .luminosity(15)
                    .density(3000)
                    .viscosity(6000)
                    .temperature(1300)
                    .sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA),
            MoltenFluid.Source::new,
            MoltenFluid.Flowing::new
    );
    public static final FluidPair<ForgeFlowingFluid> MOLTEN_ARSENICAL_BRONZE = register(
            "metal/arsenical_bronze",
            "metal/flowing_arsenical_bronze",
            properties -> properties.block(AntiquaBlocks.MOLTEN_ARSENICAL_BRONZE).bucket(AntiquaItems.MOLTEN_ARSENICAL_BRONZE_BUCKET).explosionResistance(100),
            FluidAttributes.builder(MOLTEN_STILL, MOLTEN_FLOW)
                    .translationKey("fluid.terraantiqua.metal.lead")
                    .color(ALPHA_MASK | 0xFF949495)
                    .rarity(Rarity.COMMON)
                    .luminosity(15)
                    .density(3000)
                    .viscosity(6000)
                    .temperature(1300)
                    .sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA),
            MoltenFluid.Source::new,
            MoltenFluid.Flowing::new
    );
    public static final FluidPair<ForgeFlowingFluid> MOLTEN_LEAD = register(
            "metal/lead",
            "metal/flowing_lead",
            properties -> properties.block(AntiquaBlocks.MOLTEN_LEAD).bucket(AntiquaItems.MOLTEN_LEAD_BUCKET).explosionResistance(100),
            FluidAttributes.builder(MOLTEN_STILL, MOLTEN_FLOW)
                    .translationKey("fluid.terraantiqua.metal.lead")
                    .color(ALPHA_MASK | 0xFF949495)
                    .rarity(Rarity.COMMON)
                    .luminosity(15)
                    .density(3000)
                    .viscosity(6000)
                    .temperature(1300)
                    .sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA),
            MoltenFluid.Source::new,
            MoltenFluid.Flowing::new
    );
    public static final FluidPair<ForgeFlowingFluid> MOLTEN_PEWTER = register(
            "metal/pewter",
            "metal/flowing_pewter",
            properties -> properties.block(AntiquaBlocks.MOLTEN_PEWTER).bucket(AntiquaItems.MOLTEN_PEWTER_BUCKET).explosionResistance(100),
            FluidAttributes.builder(MOLTEN_STILL, MOLTEN_FLOW)
                    .translationKey("fluid.terraantiqua.metal.lead")
                    .color(ALPHA_MASK | 0xFF949495)
                    .rarity(Rarity.COMMON)
                    .luminosity(15)
                    .density(3000)
                    .viscosity(6000)
                    .temperature(1300)
                    .sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA),
            MoltenFluid.Source::new,
            MoltenFluid.Flowing::new
    );

    //the following method and class definition are borrowed wholesale from TFC, with permission
    private static <F extends FlowingFluid> FluidPair<F> register(String sourceName, String flowingName, Consumer<ForgeFlowingFluid.Properties> builder, FluidAttributes.Builder attributes, Function<ForgeFlowingFluid.Properties, F> sourceFactory, Function<ForgeFlowingFluid.Properties, F> flowingFactory)
    {
        final Mutable<Lazy<ForgeFlowingFluid.Properties>> propertiesBox = new MutableObject<>();
        final RegistryObject<F> source = FLUIDS.register(sourceName, () -> sourceFactory.apply(propertiesBox.getValue().get()));
        final RegistryObject<F> flowing = FLUIDS.register(flowingName, () -> flowingFactory.apply(propertiesBox.getValue().get()));

        propertiesBox.setValue(Lazy.of(() -> {
            ForgeFlowingFluid.Properties lazyProperties = new ForgeFlowingFluid.Properties(source, flowing, attributes);
            builder.accept(lazyProperties);
            return lazyProperties;
        }));

        return new FluidPair<>(flowing, source);
    }
    public static class FluidPair<F extends FlowingFluid> extends Pair<RegistryObject<F>, RegistryObject<F>>
    {
        private FluidPair(RegistryObject<F> first, RegistryObject<F> second)
        {
            super(first, second);
        }

        public F getFlowing()
        {
            return getFirst().get();
        }

        public F getSource()
        {
            return getSecond().get();
        }

        public BlockState getSourceBlock()
        {
            return getSource().defaultFluidState().createLegacyBlock();
        }
    }
}
