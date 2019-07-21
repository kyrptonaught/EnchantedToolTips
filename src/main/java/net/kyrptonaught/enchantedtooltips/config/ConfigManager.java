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
    public ConfigOptions config;
    public EnchantsLookup enchantsLookup;
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

    public void saveConfig() {
        try {
            if (!configFile.exists() && !configFile.createNewFile()) {
                System.out.println(EnchantedToolTipMod.MOD_ID + " Failed to save config! Overwriting with default config.");
                config = new ConfigOptions();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String result = JANKSON.toJson(config).toJson(true, true, 0);
            if (!configFile.exists())
                configFile.createNewFile();
            FileOutputStream out = new FileOutputStream(configFile, false);

            out.write(result.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(EnchantedToolTipMod.MOD_ID + " Failed to save config! Overwriting with default config.");
            config = new ConfigOptions();
            return;
        }
    }

    public void loadConfig() {
        if (!configFile.exists() || !configFile.canRead()) {
            System.out.println(EnchantedToolTipMod.MOD_ID + " Config not found! Creating one.");
            config = new ConfigOptions();
            saveConfig();
            return;
        }
        boolean failed = false;
        try {
            JsonObject configJson = JANKSON.load(configFile);
            String regularized = configJson.toJson(false, false, 0);
            config = GSON.fromJson(regularized, ConfigOptions.class);
        } catch (Exception e) {
            e.printStackTrace();
            failed = true;
        }
        if (failed || config == null) {
            System.out.println(EnchantedToolTipMod.MOD_ID + " Failed to load config! Overwriting with default config.");
            config = new ConfigOptions();
        }
        saveConfig();
    }

    public void saveEnchants() {
        try {
            if (!enchantsFile.exists() && !enchantsFile.createNewFile()) {
                System.out.println(EnchantedToolTipMod.MOD_ID + " Failed to save config! Overwriting with default config.");
                enchantsLookup = new EnchantsLookup();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String result = JANKSON.toJson(enchantsLookup).toJson(true, true, 0);
            if (!enchantsFile.exists())
                enchantsFile.createNewFile();
            FileOutputStream out = new FileOutputStream(enchantsFile, false);

            out.write(result.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(EnchantedToolTipMod.MOD_ID + " Failed to save config! Overwriting with default config.");
            enchantsLookup = new EnchantsLookup();
            return;
        }
    }

    public void loadEnchants() {
        if (!enchantsFile.exists() || !enchantsFile.canRead()) {
            System.out.println(EnchantedToolTipMod.MOD_ID + " Config not found! Creating one.");
            enchantsLookup = new EnchantsLookup();
            saveEnchants();
            return;
        }
        boolean failed = false;
        try {
            JsonObject configJson = JANKSON.load(enchantsFile);
            String regularized = configJson.toJson(false, false, 0);
            enchantsLookup = GSON.fromJson(regularized, EnchantsLookup.class);
        } catch (Exception e) {
            e.printStackTrace();
            failed = true;
        }
        if (failed || enchantsLookup == null) {
            System.out.println(EnchantedToolTipMod.MOD_ID + " Failed to load config! Overwriting with default config.");
            enchantsLookup = new EnchantsLookup();
        }
        saveEnchants();
    }
}
