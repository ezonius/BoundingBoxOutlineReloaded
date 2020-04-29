package com.irtimaled.bbor.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

abstract class AbstractSlider extends AbstractButtonWidget implements IRenderableControl {
    double progress;

    AbstractSlider(int x, int y, int width) {
        super(x, y, width, 20, new LiteralText(""));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.render(matrixStack, mouseX, mouseY, 0f);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack ,MinecraftClient minecraft, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(WIDGETS_LOCATION);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexture(matrixStack,this.x + (int) (this.progress * (double) (this.width - 8)), this.y, 0, 66, 4, 20);
        this.drawTexture(matrixStack, this.x + (int) (this.progress * (double) (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
    }

    boolean setProgress(double progress) {
        progress = MathHelper.clamp(progress, 0d, 1d);
        if (this.progress == progress) return false;

        this.progress = progress;
        return true;
    }

    private void changeProgress(double mouseX) {
        double progress = (mouseX - (double) (this.x + 4)) / (double) (this.width - 8);
        if (setProgress(progress)) {
            onProgressChanged();
        }
        updateText();
    }

    @Override
    protected int getYImage(boolean hovered) {
        return 0;
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        changeProgress(mouseX);
        super.onDrag(mouseX, mouseY, deltaX, deltaY);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        changeProgress(mouseX);
        super.onClick(mouseX, mouseY);
    }

    protected abstract void updateText();

    protected abstract void onProgressChanged();
}
