package com.irtimaled.bbor.client.gui;

import com.irtimaled.bbor.config.ConfigManager;
import com.irtimaled.bbor.config.Setting;

public class BoolSettingButton extends AbstractButton {
    private final Setting<Boolean> setting;

    BoolSettingButton(int id, int x, int y, int width, String label, Setting<Boolean> setting) {
        super(id, x, y, width, label);
        this.setting = setting;
    }

    @Override
    protected int getState() {
        return enabled ? setting.get() ? 2 : 1 : 0;
    }

    @Override
    public void onPressed() {
        ConfigManager.Toggle(setting);
    }
}
