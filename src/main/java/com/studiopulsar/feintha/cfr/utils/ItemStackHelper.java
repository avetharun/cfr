package com.studiopulsar.feintha.cfr.utils;

import com.studiopulsar.feintha.cfr.alib;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;

public class ItemStackHelper {
    public static ItemStack DeserializeFromNBT(NbtCompound compound) {
        return ItemStack.CODEC.parse(NbtOps.INSTANCE, compound).result().orElse(ItemStack.EMPTY);
    }
    public static NbtCompound SerializeToNBT(ItemStack stack) {
        return (NbtCompound) stack.encodeAllowEmpty(alib.getWrapperLookupForRegistry(Registries.ITEM));
    }
}
