package com.studiopulsar.feintha.cfr.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;

public class ItemStackHelper {
    public static ItemStack DeserializeFromNBT(NbtCompound compound) {
        return ItemStack.CODEC.parse(NbtOps.INSTANCE, compound).result().orElse(ItemStack.EMPTY);
    }
    public static NbtCompound SerializeToNBT(ItemStack stack) {
        return (NbtCompound) ItemStack.CODEC.encode(stack, NbtOps.INSTANCE, new NbtCompound()).result().get();
    }
}
