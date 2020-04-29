package com.irtimaled.bbor.client.renderers;

import com.irtimaled.bbor.client.ClientRenderer;
import com.irtimaled.bbor.common.models.BoundingBoxWorldSpawn;
import com.irtimaled.bbor.common.models.Coords;
import com.irtimaled.bbor.config.ConfigManager;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class WorldSpawnRenderer extends AbstractRenderer<BoundingBoxWorldSpawn> {
    @Override
    public void render(MatrixStack matrixStack, BoundingBoxWorldSpawn boundingBox) {
        Color color = boundingBox.getColor();
        Coords minCoords = boundingBox.getMinCoords();
        Coords maxCoords = boundingBox.getMaxCoords();

        double y = ClientRenderer.getMaxY(ConfigManager.worldSpawnMaxY.get());

        OffsetBox offsetBox = new OffsetBox(minCoords.getX(), y, minCoords.getZ(), maxCoords.getX(), y, maxCoords.getZ());
        renderOutlinedCuboid(offsetBox.nudge(), color);
    }
}
