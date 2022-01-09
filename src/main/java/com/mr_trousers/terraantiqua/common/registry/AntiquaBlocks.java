package com.mr_trousers.terraantiqua.common.registry;

import com.mr_trousers.terraantiqua.common.blockentities.AntiquaFirepitBlockEntity;
import com.mr_trousers.terraantiqua.common.blockentities.FiremouthBlockEntity;
import com.mr_trousers.terraantiqua.common.blockentities.WellholeBlockEntity;
import com.mr_trousers.terraantiqua.common.blocks.devices.AntiquaFirepitBlock;
import com.mr_trousers.terraantiqua.common.blocks.devices.FiremouthBlock;
import com.mr_trousers.terraantiqua.common.blocks.devices.WellholeBlock;
import net.dries007.tfc.common.TFCItemGroup;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.TFCMaterials;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mr_trousers.terraantiqua.TerraAntiqua.MODID;


public class AntiquaBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<LiquidBlock> MOLTEN_ARSENIC = BLOCKS.register("fluid/metal/arsenic", () -> new LiquidBlock(AntiquaFluids.MOLTEN_ARSENIC.getSecond(), BlockBehaviour.Properties.of(TFCMaterials.MOLTEN_METAL).noCollission().strength(100f).noDrops()));
    public static final RegistryObject<LiquidBlock> MOLTEN_ARSENICAL_BRONZE = BLOCKS.register("fluid/metal/arsenical_bronze", () -> new LiquidBlock(AntiquaFluids.MOLTEN_ARSENICAL_BRONZE.getSecond(), BlockBehaviour.Properties.of(TFCMaterials.MOLTEN_METAL).noCollission().strength(100f).noDrops()));
    public static final RegistryObject<LiquidBlock> MOLTEN_LEAD = BLOCKS.register("fluid/metal/lead", () -> new LiquidBlock(AntiquaFluids.MOLTEN_LEAD.getSecond(), BlockBehaviour.Properties.of(TFCMaterials.MOLTEN_METAL).noCollission().strength(100f).noDrops()));
    public static final RegistryObject<LiquidBlock> MOLTEN_PEWTER = BLOCKS.register("fluid/metal/pewter", () -> new LiquidBlock(AntiquaFluids.MOLTEN_PEWTER.getSecond(), BlockBehaviour.Properties.of(TFCMaterials.MOLTEN_METAL).noCollission().strength(100f).noDrops()));

    public static final RegistryObject<Block> FIREMOUTH = BLOCKS.register("firemouth", () -> new FiremouthBlock(ExtendedProperties.of(BlockBehaviour.Properties.of(Material.STONE).strength(0.5F, 2.0F).sound(SoundType.BASALT).noOcclusion().lightLevel((state) -> state.getValue(TFCBlockStateProperties.LIT) ? 15 : 0)).blockEntity(AntiquaBlockEntities.FIREMOUTH)));
    public static final RegistryObject<Block> WELLHOLE = BLOCKS.register("wellhole", () -> new WellholeBlock(ExtendedProperties.of(BlockBehaviour.Properties.of(Material.STONE).strength(0.5F, 2.0F).sound(SoundType.BASALT).noOcclusion().randomTicks()).blockEntity(AntiquaBlockEntities.WELLHOLE).<WellholeBlockEntity>serverTicks(WellholeBlockEntity::serverTick)));
    //TFC replacements
    public static final RegistryObject<Block> FIREPIT = BLOCKS.register("firepit", () -> new AntiquaFirepitBlock(ExtendedProperties.of(BlockBehaviour.Properties.of(Material.DIRT).strength(0.4F, 0.4F).sound(SoundType.NETHER_WART).noOcclusion().lightLevel((state) -> state.getValue(TFCBlockStateProperties.LIT) ? 15 : 0)).blockEntity(AntiquaBlockEntities.FIREPIT).<AntiquaFirepitBlockEntity>serverTicks(AntiquaFirepitBlockEntity::serverTick)));
}
