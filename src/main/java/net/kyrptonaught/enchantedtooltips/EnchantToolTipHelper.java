package net.kyrptonaught.enchantedtooltips;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.kyrptonaught.enchantedtooltips.config.ConfigOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class EnchantToolTipHelper {
    private static HashMap<String, String> modCache = new HashMap<>();

    static {
        modCache.put("biom4st3rmoenchantments", "Mo' Enchantments");
    }

    public static void appendToolTip(List<Text> list, NbtList enchants, boolean isItem, boolean isBook) {
        NbtList enchantsCopy = enchants.copy();
        if (EnchantedToolTipMod.getConfig().sortEnchantInfo)
            enchantsCopy.sort(Comparator.comparing(enchant -> ((NbtCompound) enchant).getString("id")));
        if (EnchantedToolTipMod.getConfig().alwaysShowEnchantInfo || GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) != 0) {
            appendEnchantInfo(list, enchantsCopy, isBook);
        } else {
            if (isItem)
                ItemStack.appendEnchantments(list, enchantsCopy);
            if (EnchantedToolTipMod.getConfig().displayPressForInfo)
                appendKeyHandler(list);
        }
    }

    private static void appendKeyHandler(List<Text> list) {
        String msg = I18n.translate("enchantedtooltip.presssneak").replaceAll("KEY", I18n.translate("enchantedtooltip.KEY"));
        list.add(new LiteralText(msg));
    }


    private static void appendEnchantInfo(List<Text> list, NbtList enchants, boolean isBook) {
        for (int i = 0; i < enchants.size(); i++) {
            ConfigOptions options = EnchantedToolTipMod.getConfig();
            NbtCompound enchantTag = enchants.getCompound(i);
            Identifier enchantID = Identifier.tryParse(enchantTag.getString("id"));
            if (enchantID == null) continue;
            Enchantment enchant = Registry.ENCHANTMENT.get(enchantID);
            if (enchant == null) {
                if (options.displayMissingEnchant) {
                    list.add(new LiteralText(enchantTag.getString("id")).formatted(Formatting.GOLD));
                    list.add(new TranslatableText("enchantedtooltip.enchant.removed").formatted(Formatting.WHITE));
                    list.add(new TranslatableText("enchantedtooltip.enchant.removed2").formatted(Formatting.WHITE));
                }
                continue;
            }
            //name
            MutableText lvl = new TranslatableText("enchantment.level." + enchantTag.getInt("lvl"));
            MutableText name = new TranslatableText(enchant.getTranslationKey()).append(" ");
            MutableText title = name.formatted(enchant.isCursed() ? Formatting.RED : Formatting.DARK_GREEN);

            if (options.combineLvlMaxLvl) {
                Text maxLvl = new TranslatableText("enchantment.level." + enchant.getMaxLevel());
                lvl.append(new LiteralText("/")).append(maxLvl);
            }
            if (enchantTag.getInt("lvl") > 1 || !options.hideLvlI)
                name.append(lvl);
            list.add(title);
            //desc
            if (options.displayDescription) {
                list.add(new LiteralText(" ").append(getEnchantDesc("enchantment." + enchantTag.getString("id").replace(":", ".") + ".desc")).formatted(Formatting.WHITE));
            }
            //Level
            if (options.displayMaxLvl && !options.combineLvlMaxLvl) {
                Text maxLvl = new TranslatableText("enchantment.level." + enchant.getMaxLevel());
                list.add(new TranslatableText("enchantedtooltip.enchant.maxLevel").append(maxLvl).formatted(Formatting.WHITE));
            }
            //applies to
            if (options.displayAppliesTo && (!options.appliesToBookOnly || isBook)) {
                list.add(new TranslatableText("enchantedtooltip.enchant.applicableTo").append(new TranslatableText("enchantedtooltip.enchant.type." + enchant.type.name())).formatted(Formatting.WHITE));
            }
            //from
            if (options.displayModFrom) {
                String mod = getFromMod(enchantID.getNamespace());
                list.add(new TranslatableText("enchantedtooltip.enchant.from").formatted(Formatting.WHITE).append(new LiteralText(mod).formatted(Formatting.BLUE)));
            }
        }
    }

    private static String getFromMod(String id) {
        if (!modCache.containsKey(id)) {
            modCache.put(id, StringUtils.capitalize(FabricLoader.getInstance().getModContainer(id).map(ModContainer::getMetadata).map(ModMetadata::getName).orElse(id)));
        }
        return modCache.get(id);
    }

    private static Text getEnchantDesc(String enchantId) {
        if (EnchantedToolTipMod.getCustomEnchantsNames().containsKey(enchantId) && !EnchantedToolTipMod.getCustomEnchantsNames().get(enchantId).equals(""))
            return new LiteralText(EnchantedToolTipMod.getCustomEnchantsNames().get(enchantId));
        return new TranslatableText(enchantId);
    }
}
