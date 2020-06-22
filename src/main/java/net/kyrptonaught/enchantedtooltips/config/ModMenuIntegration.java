package net.kyrptonaught.enchantedtooltips.config;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public String getModId() {
        return EnchantedToolTipMod.MOD_ID;
    }

    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return (screen) -> {
            ConfigOptions options = EnchantedToolTipMod.getConfig();
            ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle(new TranslatableText("Enchanted ToolTips Config"));
            builder.setSavingRunnable(() -> {
                EnchantedToolTipMod.config.saveAll();
            });
            ConfigCategory category = builder.getOrCreateCategory(new TranslatableText("key.enchantedtooltips.config.category.main"));
            ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();
            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.enchantedtooltips.config.booksenabled"), options.enableForBooks).setDefaultValue(true).setSaveConsumer(val -> options.enableForBooks = val).build());
            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.enchantedtooltips.config.itemsenabled"), options.enableForItems).setDefaultValue(true).setSaveConsumer(val -> options.enableForItems = val).build());
            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.enchantedtooltips.config.tableenabled"), options.enableForEnchantTable).setDefaultValue(true).setSaveConsumer(val -> options.enableForEnchantTable = val).build());

            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.enchantedtooltips.config.alwaysshow"), options.alwaysShowEnchantInfo).setDefaultValue(false).setSaveConsumer(val -> options.alwaysShowEnchantInfo = val).build());
            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.enchantedtooltips.config.displaypress"), options.displayPressForInfo).setDefaultValue(true).setSaveConsumer(val -> options.displayPressForInfo = val).build());
            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.enchantedtooltips.config.displaymissing"), options.displayMissingEnchant).setDefaultValue(true).setSaveConsumer(val -> options.displayMissingEnchant = val).build());
            category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.enchantedtooltips.config.sortenchants"), options.sortEnchantInfo).setDefaultValue(false).setSaveConsumer(val -> options.sortEnchantInfo = val).build());

            ConfigCategory display = builder.getOrCreateCategory(new TranslatableText("key.enchantedtooltips.config.category.display"));
            display.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.enchantedtooltips.config.displayapplies"), options.displayAppliesTo).setDefaultValue(true).setSaveConsumer(val -> options.displayAppliesTo = val).build());
            display.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.enchantedtooltips.config.displaydesc"), options.displayDescription).setDefaultValue(true).setSaveConsumer(val -> options.displayDescription = val).build());
            display.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.enchantedtooltips.config.displaymaxlvl"), options.displayMaxLvl).setDefaultValue(true).setSaveConsumer(val -> options.displayMaxLvl = val).build());
            display.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.enchantedtooltips.config.displaymodfrom"), options.displayModFrom).setDefaultValue(true).setSaveConsumer(val -> options.displayModFrom = val).build());
            ConfigCategory enchantCategory = builder.getOrCreateCategory(new TranslatableText("key.enchantedtooltips.config.category.enchants"));

            List<Identifier> enchants = new ArrayList<>(Registry.ENCHANTMENT.getIds());
            Collections.sort(enchants);
            enchants.forEach(identifier -> {
                String id = identifier.toString();
                String enchant = "enchantment." + id.replace(":", ".") + ".desc";
                enchantCategory.addEntry(entryBuilder.startTextField(new TranslatableText(enchant.substring(12, enchant.length() - 5)), EnchantedToolTipMod.getCustomEnchantsNames().getOrDefault(enchant, "")).setDefaultValue("").setSaveConsumer(val -> {
                    if (val.equals("")) EnchantedToolTipMod.getCustomEnchantsNames().remove(enchant);
                    else EnchantedToolTipMod.getCustomEnchantsNames().put(enchant, val);
                }).build());
            });
            return builder.build();
        };
    }
}
