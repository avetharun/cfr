package com.studiopulsar.feintha.cfr.dpu;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DatapackUtils implements ModInitializer {
    public static void debugRender(MatrixStack matrixStack, ModelTransformationMode mode, VertexConsumerProvider consumerProvider) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
        matrixStack.translate(0,0,-.55);
        matrixStack.scale(0.05f,0.05f,0.05f);
//        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180 * MathHelper.RADIANS_PER_DEGREE));
        String modeS = mode.asString();
        if (mode.isFirstPerson()) {
            modeS = mode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND ? "fpl_hand" : "fpr_hand";
        }
        MinecraftClient.getInstance().textRenderer.draw(modeS, 0,-20,0xffffffff,false, matrixStack.peek().getPositionMatrix(), consumerProvider, TextRenderer.TextLayerType.POLYGON_OFFSET,  0x000000000, 255);
        matrixStack.pop();
    }
    @Override
    public void onInitialize() {
    }
}
