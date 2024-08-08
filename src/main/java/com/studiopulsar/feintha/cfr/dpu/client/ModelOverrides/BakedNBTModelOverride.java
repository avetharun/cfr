package com.studiopulsar.feintha.cfr.dpu.client.ModelOverrides;

import com.mojang.datafixers.util.Function4;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

// This class should really be named something like "DynamicNBTModelOverride" as it always returns false, however the parser can handle nbt as strings
public class BakedNBTModelOverride extends BooleanModelOverride{
    public NbtCompound nbtRequest;
    public BakedNBTModelOverride(){
        // Handled via mixin!!!
        super((itemStack, clientWorld, livingEntity, integer) -> false );
    }
}
