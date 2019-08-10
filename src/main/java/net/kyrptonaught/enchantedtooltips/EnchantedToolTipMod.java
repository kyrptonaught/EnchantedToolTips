package net.kyrptonaught.enchantedtooltips;

import net.fabricmc.api.ClientModInitializer;
import net.kyrptonaught.enchantedtooltips.config.ConfigManager;
import net.kyrptonaught.enchantedtooltips.config.ConfigOptions;

import java.util.HashMap;

public class EnchantedToolTipMod implements ClientModInitializer {
    public static final String MOD_ID = "enchantedtooltips";
    public static ConfigManager config = new ConfigManager();

    @Override
    public void onInitializeClient() {
        config.loadAll();
    }

    public static HashMap<String, String> getEnchantTranslations() {
        return config.getEnchantsLookup().enchants;
    }

    public static ConfigOptions getConfig() {
        return config.getConfig();
    }
}
