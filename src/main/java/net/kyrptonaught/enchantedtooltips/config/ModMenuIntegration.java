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

        category.addEntry(entryBuilder.startBooleanToggle("key.enchantedtooltips.config.displayapplies", options.displayAppliesTo).setSaveConsumer(val -> options.displayAppliesTo = val).build());
        category.addEntry(entryBuilder.startBooleanToggle("key.enchantedtooltips.config.displaydesc", options.displayDescription).setSaveConsumer(val -> options.displayDescription = val).build());
        category.addEntry(entryBuilder.startBooleanToggle("key.enchantedtooltips.config.displaymaxlvl", options.displayMaxLvl).setSaveConsumer(val -> options.displayMaxLvl = val).build());
        category.addEntry(entryBuilder.startBooleanToggle("key.enchantedtooltips.config.displaymodfrom", options.displayModFrom).setSaveConsumer(val -> options.displayModFrom = val).build());
        ConfigCategory enchants = builder.getOrCreateCategory("key.enchantedtooltips.config.category.enchants");

        for (Identifier identifier : Registry.ENCHANTMENT.getIds()) {
            String id = identifier.toString();
            String enchant = "enchantment." + id.replace(":", ".") + ".desc";
            enchants.addEntry(entryBuilder.startTextField(enchant.substring(12, enchant.length() - 5), EnchantedToolTipMod.config.enchantsLookup.enchants.getOrDefault(enchant, "")).setSaveConsumer(val -> {
                if (!val.equals(""))
                    EnchantedToolTipMod.config.enchantsLookup.enchants.put(enchant, val);
            }).build());
        }
        return Optional.of(builder::build);
    }
}
