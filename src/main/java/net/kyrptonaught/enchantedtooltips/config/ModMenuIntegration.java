package net.kyrptonaught.enchantedtooltips.config;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod;
import net.minecraft.client.gui.screen.Screen;

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
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle("Macros");
        builder.setSavingRunnable(() -> {
            EnchantedToolTipMod.config.saveConfig();
        });
        ConfigCategory category = builder.getOrCreateCategory("key.cmdkeybind.config.category.main");
        ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();
        category.addEntry(entryBuilder.startBooleanToggle("key.cmdkeybind.config.enabled", true).build());
        //category.addEntry(entryBuilder.startIntSlider("key.cmdkeybind.config.numMacros", options.macros.size(), 1, 20).setSaveConsumer(newSize -> adjustSize(newSize, options)).build());

        return Optional.of(() -> builder.build());
    }
}
