package com.studiopulsar.feintha.cfr.dpu.mixin;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.studiopulsar.feintha.cfr.dpu.client.DatapackUtilsClient;
import com.studiopulsar.feintha.cfr.dpu.client.ModelOverrides.StringModelOverride;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.client.render.model.json.ModelOverride$Deserializer")
public class ModelOverrideDeserializerMixin {
    // Unfortunately, I need to do this to allow different types. Sorry!
    @Inject(method="deserializeMinPropertyValues", at=@At("HEAD"), cancellable = true)
    private void onDeserializeMinPropertyValues(JsonObject object, CallbackInfoReturnable<List<ModelOverride.Condition>> cir) {
        Map<Identifier, Float> map = Maps.newLinkedHashMap();
        List<ModelOverride.Condition> conds = new ArrayList<>();

        JsonObject jsonObject = JsonHelper.getObject(object, "predicate");
        int i = 7;
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet()) {
//            if (stringJsonElementEntry.getValue().isJsonObject()) {
//                // assume NBT
//                ModelOverride.Condition cond = new ModelOverride.Condition(new Identifier(stringJsonElementEntry.getKey()), 1);
//                NbtCompound cond_d = alib.json2NBT(stringJsonElementEntry.getValue().getAsJsonObject());
//                alib.setMixinField(cond, "data", cond_d);
//                alib.setMixinField(cond, "isNBTOverride", true);
//                this.hasNBTOverride = true;
//                conds.add(cond);
//                continue;
//            }
            if (JsonHelper.isBoolean(stringJsonElementEntry.getValue())) {
                boolean s = JsonHelper.asBoolean(stringJsonElementEntry.getValue(), stringJsonElementEntry.getKey());
                float v = s ? DatapackUtilsClient.BOOLEAN_TRUE_CONSTANT : 0;
                conds.add(new ModelOverride.Condition(new Identifier(stringJsonElementEntry.getKey()), v));
                continue;
            } else if (JsonHelper.isString(stringJsonElementEntry.getValue())) {
                conds.add(new StringModelOverride.StringModelOverrideCondition(new Identifier(stringJsonElementEntry.getKey()), JsonHelper.asString(stringJsonElementEntry.getValue(), stringJsonElementEntry.getKey())));
                continue;
            }
            conds.add(new ModelOverride.Condition(new Identifier(stringJsonElementEntry.getKey()), JsonHelper.asFloat(stringJsonElementEntry.getValue(), stringJsonElementEntry.getKey())));
            i++;
        }
        cir.setReturnValue (conds);
    }
    boolean hasNBTOverride = false;
//    @Redirect(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/render/model/json/ModelOverride;", at = @At(value = "NEW", target = "(Lnet/minecraft/util/Identifier;Ljava/util/List;)Lnet/minecraft/client/render/model/json/ModelOverride;"))
//    ModelOverride deserializeMixin(Identifier modelId, List<ModelOverride.Condition> conditions) {
//        var ret = new ModelOverride(modelId, conditions);
//        if (hasNBTOverride) {
//            alib.setMixinField(ret, "isNBTOverride", true);
//        }
//        return ret;
//    }
}
