package com.irtimaled.bbor.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

abstract class AbstractButton extends AbstractButtonWidget implements IRenderableControl {
    AbstractButton(int x, int y, int width, String name) {
        super(x, y, width, 20, new LiteralText(name));
    }

    AbstractButton(int x, int y, int width, String name, boolean enabled) {
        this(x, y, width, name);
        this.active = enabled;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.render(matrixStack, mouseX, mouseY, 0f);
    }
    
    
    
    @Override
    protected void renderBg(MatrixStack matrixStack, MinecraftClient minecraftClient, int i, int j) {
        renderBackground(matrixStack);
    }
    
    protected void renderBackground(MatrixStack matrixStack) {
    }

    @Override
    protected int getYImage(boolean hovered) {
        return getState();
    }

    protected int getState() {
        return this.active ? this.isHovered() ? 2 : 1 : 0;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        onPressed();
    }

    protected abstract void onPressed();
}
