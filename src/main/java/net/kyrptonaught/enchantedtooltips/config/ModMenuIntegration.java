package net.kyrptonaught.enchantedtooltips.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigScreen;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigSection;
import net.kyrptonaught.kyrptconfig.config.screen.items.BooleanItem;
import net.kyrptonaught.kyrptconfig.config.screen.items.TextItem;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            ConfigOptions options = EnchantedToolTipMod.getConfig();
            ConfigScreen configScreen = new ConfigScreen(screen, Text.translatable("key.enchantedtooltips.config.title"));
            configScreen.setSavingEvent(() -> EnchantedToolTipMod.config.save());

            ConfigSection mainSection = new ConfigSection(configScreen, Text.translatable("key.enchantedtooltips.config.category.main"));

            mainSection.addConfigItem(new BooleanItem(Text.translatable("key.enchantedtooltips.config.booksenabled"), options.enableForBooks, true).setSaveConsumer(val -> options.enableForBooks = val));
            mainSection.addConfigItem(new BooleanItem(Text.translatable("key.enchantedtooltips.config.itemsenabled"), options.enableForItems, true).setSaveConsumer(val -> options.enableForItems = val));
            mainSection.addConfigItem(new BooleanItem(Text.translatable("key.enchantedtooltips.config.tableenabled"), options.enableForEnchantTable, true).setSaveConsumer(val -> options.enableForEnchantTable = val));

            mainSection.addConfigItem(new BooleanItem(Text.translatable("key.enchantedtooltips.config.alwaysshow"), options.alwaysShowEnchantInfo, false).setSaveConsumer(val -> options.alwaysShowEnchantInfo = val));
            mainSection.addConfigItem(new BooleanItem(Text.translatable("key.enchantedtooltips.config.displaypress"), options.displayPressForInfo, true).setSaveConsumer(val -> options.displayPressForInfo = val));
            mainSection.addConfigItem(new BooleanItem(Text.translatable("key.enchantedtooltips.config.displaymissing"), options.displayMissingEnchant, true).setSaveConsumer(val -> options.displayMissingEnchant = val));
            mainSection.addConfigItem(new BooleanItem(Text.translatable("key.enchantedtooltips.config.sortenchants"), options.sortEnchantInfo, false).setSaveConsumer(val -> options.sortEnchantInfo = val));

            ConfigSection displaySection = new ConfigSection(configScreen, Text.translatable("key.enchantedtooltips.config.category.display"));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("key.enchantedtooltips.config.hidelvli"), options.hideLvlI, false).setSaveConsumer(val -> options.hideLvlI = val));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("key.enchantedtooltips.config.displayapplies"), options.displayAppliesTo, true).setSaveConsumer(val -> options.displayAppliesTo = val));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("key.enchantedtooltips.config.appliesbookonly"), options.appliesToBookOnly, false).setSaveConsumer(val -> options.appliesToBookOnly = val));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("key.enchantedtooltips.config.displaydesc"), options.displayDescription, true).setSaveConsumer(val -> options.displayDescription = val));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("key.enchantedtooltips.config.displaymaxlvl"), options.displayMaxLvl, true).setSaveConsumer(val -> options.displayMaxLvl = val));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("key.enchantedtooltips.config.comblvlmaxlvl"), options.combineLvlMaxLvl, false).setSaveConsumer(val -> options.combineLvlMaxLvl = val));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("key.enchantedtooltips.config.displaymodfrom"), options.displayModFrom, true).setSaveConsumer(val -> options.displayModFrom = val));

            ConfigSection enchantSection = new ConfigSection(configScreen, Text.translatable("key.enchantedtooltips.config.category.enchants"));

            List<Identifier> enchants = new ArrayList<>(Registry.ENCHANTMENT.getIds());
            Collections.sort(enchants);
            enchants.forEach(identifier -> {
                String id = identifier.toString();
                String enchant = "enchantment." + id.replace(":", ".") + ".desc";
                enchantSection.addConfigItem(new TextItem(Text.translatable(enchant.substring(12, enchant.length() - 5)), EnchantedToolTipMod.getCustomEnchantsNames().getOrDefault(enchant, ""), "").setSaveConsumer(val -> {
                    if (val.equals("")) EnchantedToolTipMod.getCustomEnchantsNames().remove(enchant);
                    else EnchantedToolTipMod.getCustomEnchantsNames().put(enchant, val);
                }));
            });
            return configScreen;
        };
    }
}
