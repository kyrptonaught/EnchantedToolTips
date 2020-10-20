package net.kyrptonaught.enchantedtooltips;

import net.fabricmc.api.ClientModInitializer;
import net.kyrptonaught.enchantedtooltips.config.ConfigOptions;
import net.kyrptonaught.enchantedtooltips.config.CustomEnchantNames;
import net.kyrptonaught.kyrptconfig.config.ConfigManager;

import java.util.HashMap;

public class EnchantedToolTipMod implements ClientModInitializer {
    public static final String MOD_ID = "enchantedtooltips";
    public static ConfigManager config = new ConfigManager.MultiConfigManager(MOD_ID);

    @Override
    public void onInitializeClient() {
        config.registerFile("config.json5", new ConfigOptions());
        config.registerFile("enchants.json5", new CustomEnchantNames());
        config.load();
    }

    public static HashMap<String, String> getCustomEnchantsNames() {
        return ((CustomEnchantNames) config.getConfig("enchants.json5")).enchants;
    }

    public static ConfigOptions getConfig() {
        return (ConfigOptions) config.getConfig("config.json5");
    }
}
