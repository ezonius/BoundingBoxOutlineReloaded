package com.irtimaled.bbor.client.renderers;

import com.irtimaled.bbor.common.models.AbstractBoundingBox;
import com.irtimaled.bbor.config.ConfigManager;
import com.mojang.blaze3d.platform.GLX;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractRenderer<T extends AbstractBoundingBox> {
    private static double TAU = 6.283185307179586D;
    private static double PI = TAU / 2D;

    public abstract void render(MatrixStack matrixStack, T boundingBox);

    void renderCuboid(OffsetBox bb, Color color) {
        OffsetBox nudge = bb.nudge();
        if (ConfigManager.fill.get()) {
            renderFilledFaces(nudge.getMin(), nudge.getMax(), color, 30);
        }
        renderOutlinedCuboid(nudge, color);
    }

    void renderOutlinedCuboid(OffsetBox bb, Color color) {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        renderFaces(bb.getMin(), bb.getMax(), color, 255);
    }

    private void renderFaces(OffsetPoint min, OffsetPoint max, Color color, int alpha) {
        double minX = min.getX();
        double minY = min.getY();
        double minZ = min.getZ();

        double maxX = max.getX();
        double maxY = max.getY();
        double maxZ = max.getZ();

        Renderer renderer = Renderer.startQuads()
                .setColor(color)
                .setAlpha(alpha);

        if(minX != maxX && minZ != maxZ) {
            renderer.addPoint(minX, minY, minZ)
                    .addPoint(maxX, minY, minZ)
                    .addPoint(maxX, minY, maxZ)
                    .addPoint(minX, minY, maxZ);

            if (minY != maxY) {
                renderer.addPoint(minX, maxY, minZ)
                        .addPoint(maxX, maxY, minZ)
                        .addPoint(maxX, maxY, maxZ)
                        .addPoint(minX, maxY, maxZ);
            }
        }

        if(minX != maxX && minY != maxY) {
            renderer.addPoint(minX, minY, maxZ)
                    .addPoint(minX, maxY, maxZ)
                    .addPoint(maxX, maxY, maxZ)
                    .addPoint(maxX, minY, maxZ);

            if(minZ != maxZ) {
                renderer.addPoint(minX, minY, minZ)
                        .addPoint(minX, maxY, minZ)
                        .addPoint(maxX, maxY, minZ)
                        .addPoint(maxX, minY, minZ);
            }
        }
        if(minY != maxY && minZ != maxZ) {
            renderer.addPoint(minX, minY, minZ)
                    .addPoint(minX, minY, maxZ)
                    .addPoint(minX, maxY, maxZ)
                    .addPoint(minX, maxY, minZ);

            if(minX != maxX) {
                renderer.addPoint(maxX, minY, minZ)
                        .addPoint(maxX, minY, maxZ)
                        .addPoint(maxX, maxY, maxZ)
                        .addPoint(maxX, maxY, minZ);
            }
        }
        renderer.render();
    }

    void renderLine(OffsetPoint startPoint, OffsetPoint endPoint, Color color) {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        Renderer.startLines()
                .setColor(color)
                .addPoint(startPoint)
                .addPoint(endPoint)
                .render();
    }

    void renderFilledFaces(OffsetPoint min, OffsetPoint max, Color color, int alpha) {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GL11.glEnable(GL11.GL_BLEND);
        renderFaces(min, max, color, alpha);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
        GL11.glPolygonOffset(-1.f, -1.f);
    }

    @Environment(EnvType.CLIENT)
    void renderText(MatrixStack matrixStack, OffsetPoint offsetPoint, String... texts) {
        TextRenderer fontRenderer = MinecraftClient.getInstance().textRenderer;

        GL11.glPushMatrix();
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GL11.glTranslated(offsetPoint.getX(), offsetPoint.getY() + 0.002D, offsetPoint.getZ());
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-0.0175F, -0.0175F, 0.0175F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glEnable(GL11.GL_BLEND);
        GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        float top = -(fontRenderer.fontHeight * texts.length) / 2f;
        for (String text : texts) {
            float left = fontRenderer.getWidth(text) / 2f;
            fontRenderer.draw(matrixStack, text, -left, top, -1);
            top += fontRenderer.fontHeight;
        }
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    void renderSphere(OffsetPoint center, double radius, Color color, int density, int dotSize) {
        GL11.glEnable(GL11.GL_POINT_SMOOTH);
        GL11.glPointSize(dotSize);
        Renderer renderer = Renderer.startPoints()
                .setColor(color);
        buildPoints(center, radius, density)
                .forEach(renderer::addPoint);
        renderer.render();
    }

    private Set<OffsetPoint> buildPoints(OffsetPoint center, double radius, int density) {
        int segments = 24 + (density * 8);

        Set<OffsetPoint> points = new HashSet<>(segments * segments);

        double thetaSegment = PI / (double) segments;
        double phiSegment = TAU / (double) segments;

        for (double phi = 0.0D; phi < TAU; phi += phiSegment) {
            for (double theta = 0.0D; theta < PI; theta += thetaSegment) {
                double dx = radius * Math.sin(phi) * Math.cos(theta);
                double dz = radius * Math.sin(phi) * Math.sin(theta);
                double dy = radius * Math.cos(phi);

                points.add(center.offset(dx, dy, dz));
            }
        }
        return points;
    }
}
