package com.irtimaled.bbor.client.gui;

import net.minecraft.client.util.math.MatrixStack;

interface IRenderableControl extends IControl {
    void render(MatrixStack matrixStack, int mouseX, int mouseY);
}
