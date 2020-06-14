package net.kyrptonaught.enchantedtooltips.config;

import blue.endless.jankson.Comment;
import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;

import java.util.HashMap;

public class CustomEnchantNames implements AbstractConfigFile {
    @Comment("The descriptions displayed for enchants")
    public HashMap<String, String> enchants = new HashMap<>();
}
