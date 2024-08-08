package com.studiopulsar.feintha.cfr.dpu.mixin;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.studiopulsar.feintha.cfr.alib;
import com.studiopulsar.feintha.cfr.dpu.DatapackUtils;
import com.studiopulsar.feintha.cfr.dpu.client.DatapackUtilsClient;
import com.studiopulsar.feintha.cfr.dpu.client.ModelOverrides.BooleanModelOverride;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow @Final private ItemModels models;

    @Shadow public abstract ItemModels getModels();

    @Shadow @Final
    private TextureManager textureManager;
    @Mixin(HandledScreen.class)
    public static class InventoryRendererMixin {
        @Inject(method="drawSlot", at=@At("HEAD"))
        void drawSlotBeginMixin(DrawContext context, Slot slot, CallbackInfo ci){
            DatapackUtilsClient.currentInventorySlot = slot.id;
        }
        @Inject(method="drawSlot", at=@At("TAIL"))
        void drawSlotEndMixin(DrawContext context, Slot slot, CallbackInfo ci){
            DatapackUtilsClient.currentInventorySlot = -1;
        }
    }
    @ModifyArg(method="renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at=@At(value="INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V"))
    private BakedModel injectedModel(BakedModel model, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) MatrixStack matrixStack, @Local(argsOnly = true) ModelTransformationMode mode, @Local(argsOnly = true) VertexConsumerProvider consumerProvider) {
        BooleanModelOverride.currentModelTransform = mode;
        var m =  model.getOverrides().apply(model,stack, MinecraftClient.getInstance().world, null,42);
        if (stack.isOf(Items.DEBUG_STICK) && DatapackUtilsClient.isDebuggingFeaturesEnabled()) {
            DatapackUtils.debugRender(matrixStack, BooleanModelOverride.currentModelTransform, consumerProvider);
        }
        return m;
    }
    @Inject(at=@At("HEAD"), method= "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V")
    public void renderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci, @Share("model") LocalRef<BakedModel> modelRef) {
        BooleanModelOverride.currentModelTransform = renderMode;
    }
    @Inject(at=@At("RETURN"), method= "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V")
    public void renderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        BooleanModelOverride.currentModelTransform = ModelTransformationMode.NONE;
    }
    @Mixin(InGameHud.class)
    public static class InGameHudMixin{
        @Inject(method="renderHotbar", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/client/render/RenderTickCounter;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V", shift = At.Shift.BEFORE))
        void injectRenderHotbarItem(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci, @Local(name = "m") int index){
            DatapackUtilsClient.currentInventorySlot = index;
        }
        @Inject(method="renderHotbar", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/client/render/RenderTickCounter;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V", shift = At.Shift.AFTER))
        void injectRenderHotbarItemTail(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci, @Local(name = "m") int index){
            DatapackUtilsClient.currentInventorySlot = index;
        }

        @Inject(method="renderHotbarItem", at=@At("HEAD"))
        void injectRenderHotbarItem(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci){
            DatapackUtilsClient.isRenderingInHotbarCurrent = true;
            BooleanModelOverride.currentModelTransform = ModelTransformationMode.GUI;
        }
        @Inject(method="renderHotbarItem", at=@At("RETURN"))
        void injectRenderHotbarItemEnd(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci){
            DatapackUtilsClient.isRenderingInHotbarCurrent = false;
            BooleanModelOverride.currentModelTransform = ModelTransformationMode.GUI;
        }
    }

}
