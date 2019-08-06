package net.kyrptonaught.enchantedtooltips;

import net.fabricmc.api.ClientModInitializer;
import net.kyrptonaught.enchantedtooltips.config.ConfigManager;

public class EnchantedToolTipMod implements ClientModInitializer {
    public static final String MOD_ID = "enchantedtooltips";
    public static ConfigManager config = new ConfigManager();

    @Override
    public void onInitializeClient() {
        config.loadConfig();
        config.loadEnchants();
    }
}
