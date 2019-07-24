package net.kyrptonaught.enchantedtooltips.config;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public String getModId() {
        return EnchantedToolTipMod.MOD_ID;
    }

    @Override
    public Optional<Supplier<Screen>> getConfigScreen(Screen screen) {
        ConfigOptions options = EnchantedToolTipMod.config.config;
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle("Enchanted ToolTips Config");
        builder.setSavingRunnable(() -> {
            EnchantedToolTipMod.config.saveConfig();
            EnchantedToolTipMod.config.saveEnchants();
        });
        ConfigCategory category = builder.getOrCreateCategory("key.enchantedtooltips.config.category.main");
        ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();
        category.addEntry(entryBuilder.startBooleanToggle("key.enchantedtooltips.config.booksenabled", options.enableForBooks).setSaveConsumer(val -> options.enableForBooks = val).build());
        category.addEntry(entryBuilder.startBooleanToggle("key.enchantedtooltips.config.itemsenabled", options.enableForItems).setSaveConsumer(val -> options.enableForItems = val).build());

        category.addEntry(entryBuilder.startBooleanToggle("key.enchantedtooltips.config.alwaysshow", options.alwaysShowEnchantInfo).setSaveConsumer(val -> options.alwaysShowEnchantInfo = val).build());
        category.addEntry(entryBuilder.startBooleanToggle("key.enchantedtooltips.config.displaypress", options.displayPressForInfo).setSaveConsumer(val -> options.displayPressForInfo = val).build());

        ConfigCategory display = builder.getOrCreateCategory("key.enchantedtooltips.config.category.display");
        display.addEntry(entryBuilder.startBooleanToggle("key.enchantedtooltips.config.displayapplies", options.displayAppliesTo).setSaveConsumer(val -> options.displayAppliesTo = val).build());
        display.addEntry(entryBuilder.startBooleanToggle("key.enchantedtooltips.config.displaydesc", options.displayDescription).setSaveConsumer(val -> options.displayDescription = val).build());
        display.addEntry(entryBuilder.startBooleanToggle("key.enchantedtooltips.config.displaymaxlvl", options.displayMaxLvl).setSaveConsumer(val -> options.displayMaxLvl = val).build());
        display.addEntry(entryBuilder.startBooleanToggle("key.enchantedtooltips.config.displaymodfrom", options.displayModFrom).setSaveConsumer(val -> options.displayModFrom = val).build());
        ConfigCategory enchantCategory = builder.getOrCreateCategory("key.enchantedtooltips.config.category.enchants");

        List<Identifier> enchants = new ArrayList<>(Registry.ENCHANTMENT.getIds());
        Collections.sort(enchants);
        enchants.forEach(identifier -> {
            String id = identifier.toString();
            String enchant = "enchantment." + id.replace(":", ".") + ".desc";
            enchantCategory.addEntry(entryBuilder.startTextField(enchant.substring(12, enchant.length() - 5), EnchantedToolTipMod.config.enchantsLookup.enchants.getOrDefault(enchant, "")).setSaveConsumer(val -> {
                if (!val.equals("")) EnchantedToolTipMod.config.enchantsLookup.enchants.put(enchant, val);
            }).build());
        });
        return Optional.of(builder::build);
    }
}
