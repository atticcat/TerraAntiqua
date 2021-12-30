package com.mr_trousers.terraantiqua.common.registry;

import com.mr_trousers.terraantiqua.common.container.AntiquaFirepitContainer;
import com.mr_trousers.terraantiqua.common.blockentities.AntiquaFirepitBlockEntity;
import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.dries007.tfc.common.container.BlockEntityContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.mr_trousers.terraantiqua.TerraAntiqua.MODID;

public class AntiquaContainerTypes
{
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);

    public static final RegistryObject<MenuType<AntiquaFirepitContainer>> FIREPIT = AntiquaContainerTypes.<AntiquaFirepitBlockEntity, AntiquaFirepitContainer>registerBlock("firepit", AntiquaBlockEntities.FIREPIT, AntiquaFirepitContainer::create);


    //from TFC
    private static <T extends InventoryBlockEntity<?>, C extends BlockEntityContainer<T>> RegistryObject<MenuType<C>> registerBlock(String name, Supplier<BlockEntityType<T>> type, BlockEntityContainer.Factory<T, C> factory)
    {
        return register(name, (windowId, playerInventory, buffer) -> {
            final Level world = playerInventory.player.level;
            final BlockPos pos = buffer.readBlockPos();
            final T entity = world.getBlockEntity(pos, type.get()).orElseThrow();

            return factory.create(entity, playerInventory, windowId);
        });
    }

    private static <C extends AbstractContainerMenu> RegistryObject<MenuType<C>> register(String name, IContainerFactory<C> factory)
    {
        return CONTAINERS.register(name, () -> IForgeMenuType.create(factory));
    }
}
