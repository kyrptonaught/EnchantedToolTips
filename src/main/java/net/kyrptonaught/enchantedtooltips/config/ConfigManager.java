package net.kyrptonaught.enchantedtooltips.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().create();
    private static final Jankson JANKSON = Jankson.builder().build();
    private ConfigOptions config;
    private CustomEnchantNames enchantsLookup;

    private final File configFile, enchantsFile;

    public ConfigManager() {
        File dir = new File(FabricLoader.getInstance().getConfigDirectory() + "/enchantedtooltips");
        if (!Files.exists(dir.toPath())) {
            try {
                Files.createDirectories(dir.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.configFile = new File(dir, "config.json5");
        this.enchantsFile = new File(dir, "enchants.json5");
    }

    public ConfigOptions getConfig() {
        return config;
    }

    public CustomEnchantNames getEnchantsLookup() {
        return enchantsLookup;
    }

    public String getEnchantFileLocation() {
        return enchantsFile.getPath();
    }

    public void saveAll() {
        save(configFile, true);
        save(enchantsFile, false);
    }

    public void loadAll() {
        load(configFile, true);
        load(enchantsFile, false);
    }

    private void save(File saveFile, boolean isConfig) {
        try {
            if (!saveFile.exists() && !saveFile.createNewFile()) {
                System.out.println(EnchantedToolTipMod.MOD_ID + " Failed to save config! Overwriting with default config.");
                resetToDefault(isConfig);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileOutputStream out = new FileOutputStream(saveFile, false)) {
            String result = JANKSON.toJson(isConfig ? config : enchantsLookup).toJson(true, true, 0);
            if (!saveFile.exists())
                saveFile.createNewFile();
            out.write(result.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(EnchantedToolTipMod.MOD_ID + " Failed to save config! Overwriting with default config.");
            resetToDefault(isConfig);
        }
    }

    private void load(File saveFile, boolean isConfig) {
        if (!saveFile.exists() || !saveFile.canRead()) {
            System.out.println(EnchantedToolTipMod.MOD_ID + " Config not found! Creating one.");
            resetToDefault(isConfig);
            save(saveFile, isConfig);
            return;
        }
        boolean failed = false;
        try {
            JsonObject configJson = JANKSON.load(saveFile);
            String regularized = configJson.toJson(false, false, 0);
            if (isConfig) config = GSON.fromJson(regularized, ConfigOptions.class);
            else enchantsLookup = GSON.fromJson(regularized, CustomEnchantNames.class);

        } catch (Exception e) {
            e.printStackTrace();
            failed = true;
        }
        if (failed || (isConfig && config == null) || (!isConfig && enchantsLookup == null)) {
            System.out.println(EnchantedToolTipMod.MOD_ID + " Failed to load config! Overwriting with default config.");
            resetToDefault(isConfig);
        }
        save(saveFile, isConfig);
    }

    private void resetToDefault(boolean isConfig) {
        if (isConfig) config = new ConfigOptions();
        else enchantsLookup = new CustomEnchantNames();
    }
}
