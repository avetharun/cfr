package com.studiopulsar.feintha.cfr;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.Text;

import java.util.Arrays;

public class CFR implements ModInitializer {

    @Override
    public void onInitialize() {}
    public static ItemGroup.Builder newItemGroup(String display_name, ItemConvertible... items) {
        return FabricItemGroup.builder().entries((displayContext, entries) -> Arrays.stream(items).forEach(itemConvertible -> entries.add(itemConvertible.asItem()))).displayName(Text.translatable(display_name));
    }
}
