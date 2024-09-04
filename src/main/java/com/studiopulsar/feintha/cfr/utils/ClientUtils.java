package com.studiopulsar.feintha.cfr.utils;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class ClientUtils {
    public static BlockHitResult raycastFromCrosshair(boolean fluids) {
        assert MinecraftClient.getInstance().player != null;
//        var hit = MinecraftClient.getInstance().player.raycast(3, MinecraftClient.getInstance().getRenderTickCounter().getLastFrameDuration(), fluids);
        var hit = MinecraftClient.getInstance().player.raycast(3, MinecraftClient.getInstance().getTickDelta(), fluids);
        return (BlockHitResult) hit;
    }
    public static BlockState blockAtCursor(boolean fluids) {
        assert MinecraftClient.getInstance().world != null;
        return MinecraftClient.getInstance().world.getBlockState(raycastFromCrosshair(fluids).getBlockPos());
    }
    public static BlockPos cursorBlockPos(boolean fluids) {
        assert MinecraftClient.getInstance().world != null;
        return raycastFromCrosshair(fluids).getBlockPos();
    }
}
