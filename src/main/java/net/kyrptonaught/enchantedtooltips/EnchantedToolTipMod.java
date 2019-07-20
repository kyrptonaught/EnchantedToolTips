package net.kyrptonaught.enchantedtooltips;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.kyrptonaught.enchantedtooltips.config.ConfigManager;
import net.kyrptonaught.enchantedtooltips.config.ConfigOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;

public class EnchantedToolTipMod implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "enchantedtooltips";
	public static ConfigManager config = new ConfigManager();

	@Override
	public void onInitialize() {
		GenEnchantsCommand.registerCommand();
	}

	@Override
	public void onInitializeClient() {
		config.loadConfig();
		config.loadEnchants();
		GenEnchantsCommand.registerReceivePacket();
	}
}
