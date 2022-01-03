package com.mr_trousers.terraantiqua.common.blocks;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class AntiquaBlockStateProperties
{
    //firepits
    public static final BooleanProperty HAS_WOOD_ASH = BooleanProperty.create("has_wood_ash");
    public static final IntegerProperty NUM_LOGS = IntegerProperty.create("num_logs", 0, 4);
}
