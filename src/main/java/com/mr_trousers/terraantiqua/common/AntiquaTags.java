package com.mr_trousers.terraantiqua.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;

public class AntiquaTags
{
    public static class Items
    {
        public static final Tag.Named<Item> SHOVELS = ItemTags.createOptional(new ResourceLocation(MOD_ID, "shovels"));
    }
}
