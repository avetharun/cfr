package com.studiopulsar.feintha.cfr.dpu.client;

import com.studiopulsar.feintha.cfr.dpu.client.ModelOverrides.BooleanModelOverride;
import com.studiopulsar.feintha.cfr.dpu.client.ModelOverrides.IntModelOverride;
import com.studiopulsar.feintha.cfr.dpu.client.ModelOverrides.StringModelOverride;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.util.Identifier;

public class DatapackUtilsClient implements ClientModInitializer {
    public static int currentInventorySlot = -1;
    public static boolean isRenderingInHotbarCurrent = false;
    public static final float BOOLEAN_TRUE_CONSTANT = 907991;
    public static boolean enableDebuggingFeatures = false;
    public static boolean isDebuggingFeaturesEnabled(){return enableDebuggingFeatures;}
    public static void EnableDebuggingFeatures(){enableDebuggingFeatures = true;}
    public static void DisableDebuggingFeatures(){enableDebuggingFeatures = false;}
    @Override
    public void onInitializeClient() {
        ModelPredicateProviderRegistry.register(Identifier.of("slot"), new IntModelOverride((stack, clientWorld, livingEntity, integer) -> {
            return currentInventorySlot;
        }));
        ModelPredicateProviderRegistry.register(Identifier.of("count"), (stack, world, entity, seed) -> (float)stack.getCount() / (float)stack.getMaxCount());
        ModelPredicateProviderRegistry.register(Identifier.of("damage"), (stack, world, entity, seed) -> (float)stack.getDamage() / (float)stack.getMaxDamage());
        ModelPredicateProviderRegistry.register(Identifier.of("is_hand_first"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHandFirst(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("is_hand_third"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHandThird(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("is_hand_any"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHandAny(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("is_in_hand_first"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHandFirst(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("is_in_hand_third"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHandThird(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("is_in_hand_any"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInHandAny(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("is_gui"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInGUI(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("is_inventory"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInGUI(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("is_fixed"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInFixedPos(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("is_in_gui"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInGUI(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("is_in_inventory"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInGUI(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("is_in_fixed"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInFixedPos(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("is_in_item_frame"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingInFixedPos(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("is_dropped"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingAsDropped(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("is_ground"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingAsDropped(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("is_on_ground"), new BooleanModelOverride((itemStack, clientWorld, livingEntity, integer) -> BooleanModelOverride.isRenderingAsDropped(itemStack)));
        ModelPredicateProviderRegistry.register(Identifier.of("item_use_time"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItem() != stack ? 0.0F : (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft()) / 20.0F;
            }
        });

        ModelPredicateProviderRegistry.register(Identifier.of("item_swing_time"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return (float)(entity.getHandSwingProgress(MinecraftClient.getInstance().getRenderTime()));
            }
        });
        ModelPredicateProviderRegistry.register(Identifier.of("item_is_using"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? BOOLEAN_TRUE_CONSTANT : 0.0F;
        });
        ModelPredicateProviderRegistry.register(Identifier.of("item_is_swinging"), (stack, world, entity, seed) -> {
            return entity != null && entity.handSwinging && entity.getActiveItem() == stack ? BOOLEAN_TRUE_CONSTANT : 0.0F;
        });
        ModelPredicateProviderRegistry.register(Identifier.of("item_is_attacking"), (stack, world, entity, seed) -> {
            return entity != null && entity.handSwinging && entity.getActiveItem() == stack ? BOOLEAN_TRUE_CONSTANT : 0.0F;
        });
    }
    public static boolean isModelTransformationInHand(ModelTransformationMode mode) {
        return mode.isFirstPerson() || mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND || mode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
    }
    public static boolean isModelTransformationFixed(ModelTransformationMode mode) {
        return mode == ModelTransformationMode.FIXED;
    }
    public static boolean isModelTransformationGui(ModelTransformationMode mode) {
        return mode == ModelTransformationMode.GUI;
    }
    public static boolean isModelTransformationInHandFirst(ModelTransformationMode mode) {
        return mode.isFirstPerson();
    }
    public static boolean isModelTransformationInHandThird(ModelTransformationMode mode) {
        return mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND || mode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
    }

}
