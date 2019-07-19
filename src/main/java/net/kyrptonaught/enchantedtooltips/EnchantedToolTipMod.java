package net.kyrptonaught.enchantedtooltips;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class EnchantedToolTipMod implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "enchantedtooltips";

	@Override
	public void onInitialize() {
		TranslationCommand.registerCommand();
	}

	@Override
	public void onInitializeClient() {
		TranslationCommand.registerReceivePacket();
	}
}
