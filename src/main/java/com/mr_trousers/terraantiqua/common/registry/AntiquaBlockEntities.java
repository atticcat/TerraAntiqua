package com.mr_trousers.terraantiqua.common.registry;

import com.mr_trousers.terraantiqua.common.blockentities.*;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mr_trousers.terraantiqua.TerraAntiqua.MODID;

public class AntiquaBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);

    public static final RegistryObject<BlockEntityType<FiremouthBlockEntity>> FIREMOUTH = TILE_ENTITIES.register("firemouth", () -> BlockEntityType.Builder.of(FiremouthBlockEntity::new, AntiquaBlocks.FIREMOUTH.get()).build(null));
    public static final RegistryObject<BlockEntityType<WellholeBlockEntity>> WELLHOLE = TILE_ENTITIES.register("wellhole", () -> BlockEntityType.Builder.of(WellholeBlockEntity::new, AntiquaBlocks.WELLHOLE.get()).build(null));
    public static final RegistryObject<BlockEntityType<SaggarBlockEntity>> SAGGAR = TILE_ENTITIES.register("saggar", () -> BlockEntityType.Builder.of(SaggarBlockEntity::new, AntiquaBlocks.SAGGAR.get()).build(null));
    //TFC replacements
    public static final RegistryObject<BlockEntityType<AntiquaFirepitBlockEntity>> FIREPIT = TILE_ENTITIES.register("firepit", () -> BlockEntityType.Builder.of(AntiquaFirepitBlockEntity::new, AntiquaBlocks.FIREPIT.get()).build(null));

}
