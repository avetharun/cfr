package com.studiopulsar.feintha.cfr.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public class CodecHelpers {
    public static MapCodec<NbtCompound> nbtCompound(String fieldName) {return NbtCompound.CODEC.fieldOf(fieldName);}
    public static MapCodec<ItemStack> itemStack(String fieldName) {return ItemStack.CODEC.fieldOf(fieldName);}
    public static MapCodec<Integer> integer(String fieldName) {return Codec.INT.fieldOf(fieldName);}
    public static MapCodec<String> string(String fieldName) {return Codec.STRING.fieldOf(fieldName);}
    public static MapCodec<Boolean> bool(String fieldName) {return Codec.BOOL.fieldOf(fieldName);}
    public static MapCodec<Float> float_number(String fieldName) {return Codec.FLOAT.fieldOf(fieldName);}
    public static MapCodec<Double> double_number(String fieldName) {return Codec.DOUBLE.fieldOf(fieldName);}
    public static MapCodec<Byte> byte_number(String fieldName) {return Codec.BYTE.fieldOf(fieldName);}
    public static MapCodec<Long> long_number(String fieldName) {return Codec.LONG.fieldOf(fieldName);}
    public static class Optional {
        public static MapCodec<java.util.Optional<NbtCompound>> nbtCompound(String fieldName) { return NbtCompound.CODEC.optionalFieldOf(fieldName); }
        public static MapCodec<java.util.Optional<ItemStack>> itemStack(String fieldName) { return ItemStack.CODEC.optionalFieldOf(fieldName); }
        public static MapCodec<java.util.Optional<Integer>> integer(String fieldName) {return Codec.INT.optionalFieldOf(fieldName);}
        public static MapCodec<java.util.Optional<String>> string(String fieldName) {return Codec.STRING.optionalFieldOf(fieldName);}
        public static MapCodec<java.util.Optional<Float>> float_number(String fieldName) {return Codec.FLOAT.optionalFieldOf(fieldName);}
        public static MapCodec<java.util.Optional<Double>> double_number(String fieldName) {return Codec.DOUBLE.optionalFieldOf(fieldName);}
        public static MapCodec<java.util.Optional<Boolean>> bool(String fieldName) {return Codec.BOOL.optionalFieldOf(fieldName);}
        public static MapCodec<java.util.Optional<Byte>> byte_number(String fieldName) {return Codec.BYTE.optionalFieldOf(fieldName);}
        public static MapCodec<java.util.Optional<Long>> long_number(String fieldName) {return Codec.LONG.optionalFieldOf(fieldName);}
    }
}
