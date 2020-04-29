package com.irtimaled.bbor.client.renderers;

import com.irtimaled.bbor.client.ClientRenderer;
import com.irtimaled.bbor.common.models.BoundingBoxSlimeChunk;
import com.irtimaled.bbor.config.ConfigManager;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class SlimeChunkRenderer extends AbstractRenderer<BoundingBoxSlimeChunk> {
    @Override
    public void render(MatrixStack matrixStack, BoundingBoxSlimeChunk boundingBox) {
        OffsetBox bb = new OffsetBox(boundingBox.getMinCoords(), boundingBox.getMaxCoords());
        Color color = boundingBox.getColor();
        renderCuboid(bb, color);

        double maxY = ClientRenderer.getMaxY(ConfigManager.slimeChunkMaxY.get());
        double dY = maxY - 39;
        if (dY > 0) {
            OffsetPoint min = bb.getMin().offset(0, 38, 0);
            OffsetPoint max = bb.getMax().offset(0, dY, 0);
            renderCuboid(new OffsetBox(min, max), color);
        }
    }
}
