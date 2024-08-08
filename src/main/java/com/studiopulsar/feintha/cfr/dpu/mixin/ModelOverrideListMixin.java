package com.studiopulsar.feintha.cfr.dpu.mixin;

import com.google.gson.JsonParser;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.studiopulsar.feintha.cfr.alib;
import com.studiopulsar.feintha.cfr.dpu.client.ModelOverrides.StringModelOverride;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ModelOverrideList.class)
public abstract class ModelOverrideListMixin {
    @Redirect(method = "<init>(Lnet/minecraft/client/render/model/Baker;Lnet/minecraft/client/render/model/json/JsonUnbakedModel;Ljava/util/List;)V", at=@At(value = "NEW", target = "([Lnet/minecraft/client/render/model/json/ModelOverrideList$InlinedCondition;Lnet/minecraft/client/render/model/BakedModel;)Lnet/minecraft/client/render/model/json/ModelOverrideList$BakedOverride;"))
    ModelOverrideList.BakedOverride createCustomInlinedCondition(ModelOverrideList.InlinedCondition[] inlinedConditions, BakedModel model, @Local ModelOverride modelOverride, @Local BakedModel bakedModel, @Local Object2IntMap<Identifier> object2IntMap) {
        inlinedConditions = (ModelOverrideList.InlinedCondition[]) modelOverride.streamConditions().<ModelOverrideList.InlinedCondition>map((condition) -> {
            int i = object2IntMap.getInt(condition.getType());
            if (condition instanceof StringModelOverride.StringModelOverrideCondition sMOC) {
                return (ModelOverrideList.InlinedCondition) new StringModelOverride.InlinedStringModelOverrideCondition(i, sMOC.value);
            } else {
                return new ModelOverrideList.InlinedCondition(i, condition.getThreshold());
            }
        }).toArray(ModelOverrideList.InlinedCondition[]::new);
        return new ModelOverrideList.BakedOverride(inlinedConditions, bakedModel);
    }
    @ModifyExpressionValue(
            method="apply", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/json/ModelOverrideList$BakedOverride;test([F)Z"))
    private boolean applyMixin(boolean original, @Local ModelOverrideList.BakedOverride override, @Local(argsOnly = true) ItemStack stack){
        if (!original) {
            for (ModelOverrideList.InlinedCondition condition : override.conditions) {
                if (condition instanceof StringModelOverride.InlinedStringModelOverrideCondition stringCondition) {
                    if (stringCondition.value.startsWith("name_contains:")) {
                        var val = stringCondition.value.replaceFirst("name_contains:", "");
                        original |= stack.getName().toString().contains(val);
                    }
                    if (stringCondition.value.startsWith("{") && stringCondition.value.endsWith("}")) {
                        NbtCompound c1 = alib.json2NBT(JsonParser.parseString(stringCondition.value).getAsJsonObject());
                        original |= alib.checkNBTEquals(c1, (NbtCompound) stack.encode(alib.getWrapperLookupForRegistry(Registries.ITEM)));
                    }
                }
            }
        }
        return original;
    }
}
