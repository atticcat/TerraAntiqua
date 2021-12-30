package com.mr_trousers.terraantiqua.common.registry;

import net.dries007.tfc.common.TFCItemGroup;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mr_trousers.terraantiqua.TerraAntiqua.MODID;
import static net.dries007.tfc.common.TFCItemGroup.MISC;

public class AntiquaItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> MUD = ITEMS.register("mud", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WET_MUDBRICK = ITEMS.register("ceramic/wet_mudbrick", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MUDBRICK = ITEMS.register("ceramic/mudbrick", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MUDBRICKS = ITEMS.register("ceramic/mudbricks", () -> new Item(new Item.Properties()));

    //ores
    public static final RegistryObject<Item> POOR_GALENA = ITEMS.register("ore/poor_galena", () -> new Item(new Item.Properties().tab(TFCItemGroup.ORES)));
    public static final RegistryObject<Item> NORMAL_GALENA = ITEMS.register("ore/normal_galena", () -> new Item(new Item.Properties().tab(TFCItemGroup.ORES)));
    public static final RegistryObject<Item> RICH_GALENA = ITEMS.register("ore/rich_galena", () -> new Item(new Item.Properties().tab(TFCItemGroup.ORES)));

    public static final RegistryObject<Item> POOR_ORPIMENT = ITEMS.register("ore/poor_orpiment", () -> new Item(new Item.Properties().tab(TFCItemGroup.ORES)));
    public static final RegistryObject<Item> NORMAL_ORPIMENT = ITEMS.register("ore/normal_orpiment", () -> new Item(new Item.Properties().tab(TFCItemGroup.ORES)));
    public static final RegistryObject<Item> RICH_ORPIMENT = ITEMS.register("ore/rich_orpiment", () -> new Item(new Item.Properties().tab(TFCItemGroup.ORES)));

    public static final RegistryObject<Item> POOR_REALGAR = ITEMS.register("ore/poor_realgar", () -> new Item(new Item.Properties().tab(TFCItemGroup.ORES)));
    public static final RegistryObject<Item> NORMAL_REALGAR = ITEMS.register("ore/normal_realgar", () -> new Item(new Item.Properties().tab(TFCItemGroup.ORES)));
    public static final RegistryObject<Item> RICH_REALGAR = ITEMS.register("ore/rich_realgar", () -> new Item(new Item.Properties().tab(TFCItemGroup.ORES)));

    //molten metal buckets
    public static final RegistryObject<BucketItem> MOLTEN_ARSENIC_BUCKET = ITEMS.register("bucket/metal/arsenic", () -> new BucketItem(AntiquaFluids.MOLTEN_ARSENIC.getSecond(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MISC)));
    public static final RegistryObject<BucketItem> MOLTEN_ARSENICAL_BRONZE_BUCKET = ITEMS.register("bucket/metal/arsenical_bronze", () -> new BucketItem(AntiquaFluids.MOLTEN_ARSENICAL_BRONZE.getSecond(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MISC)));
    public static final RegistryObject<BucketItem> MOLTEN_LEAD_BUCKET = ITEMS.register("bucket/metal/lead", () -> new BucketItem(AntiquaFluids.MOLTEN_LEAD.getSecond(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MISC)));
    public static final RegistryObject<BucketItem> MOLTEN_PEWTER_BUCKET = ITEMS.register("bucket/metal/pewter", () -> new BucketItem(AntiquaFluids.MOLTEN_PEWTER.getSecond(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(MISC)));
}
