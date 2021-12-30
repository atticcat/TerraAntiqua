package com.mr_trousers.terraantiqua.common.registry;

import com.mr_trousers.terraantiqua.common.blockentities.AntiquaFirepitBlockEntity;
import com.mr_trousers.terraantiqua.common.registry.AntiquaBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mr_trousers.terraantiqua.TerraAntiqua.MODID;

public class AntiquaBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);

    public static final RegistryObject<BlockEntityType<AntiquaFirepitBlockEntity>> FIREPIT = TILE_ENTITIES.register("firepit", () -> BlockEntityType.Builder.of(AntiquaFirepitBlockEntity::new, AntiquaBlocks.FIREPIT.get()).build(null));

}
