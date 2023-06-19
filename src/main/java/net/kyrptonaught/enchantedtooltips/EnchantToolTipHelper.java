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
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.config.Registry;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class EnchantToolTipHelper {
    private static final HashMap<String, String> modCache = new HashMap<>();

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
        list.add(Text.literal(msg));
    }


    private static void appendEnchantInfo(List<Text> list, NbtList enchants, boolean isBook) {
        for (int i = 0; i < enchants.size(); i++) {
            ConfigOptions options = EnchantedToolTipMod.getConfig();
            NbtCompound enchantTag = enchants.getCompound(i);
            Identifier enchantID = Identifier.tryParse(enchantTag.getString("id"));
            if (enchantID == null) continue;
            Enchantment enchant = Registries.ENCHANTMENT.get(enchantID);
            if (enchant == null) {
                if (options.displayMissingEnchant) {
                    list.add(Text.literal(enchantTag.getString("id")).formatted(Formatting.GOLD));
                    list.add(Text.translatable("enchantedtooltip.enchant.removed").formatted(Formatting.WHITE));
                    list.add(Text.translatable("enchantedtooltip.enchant.removed2").formatted(Formatting.WHITE));
                }
                continue;
            }
            //name
            MutableText lvl = Text.translatable("enchantment.level." + enchantTag.getInt("lvl"));
            MutableText name = Text.translatable(enchant.getTranslationKey()).append(" ");
            MutableText title = name.formatted(enchant.isCursed() ? Formatting.RED : Formatting.DARK_GREEN);

            if (options.combineLvlMaxLvl) {
                Text maxLvl = Text.translatable("enchantment.level." + enchant.getMaxLevel());
                lvl.append(Text.literal("/")).append(maxLvl);
            }
            if (enchantTag.getInt("lvl") > 1 || !options.hideLvlI)
                name.append(lvl);
            list.add(title);
            //desc
            if (options.displayDescription) {
                list.add(Text.literal(" ").append(getEnchantDesc("enchantment." + enchantTag.getString("id").replace(":", ".") + ".desc")).formatted(Formatting.WHITE));
            }
            //Level
            if (options.displayMaxLvl && !options.combineLvlMaxLvl) {
                Text maxLvl = Text.translatable("enchantment.level." + enchant.getMaxLevel());
                list.add(Text.translatable("enchantedtooltip.enchant.maxLevel").append(maxLvl).formatted(Formatting.WHITE));
            }
            //applies to
            if (options.displayAppliesTo && (!options.appliesToBookOnly || isBook)) {
                if (enchant.target != null)
                    list.add(Text.translatable("enchantedtooltip.enchant.applicableTo").append(Text.translatable("enchantedtooltip.enchant.type." + enchant.target.name())).formatted(Formatting.WHITE));
            }
            //from
            if (options.displayModFrom) {
                String mod = getFromMod(enchantID.getNamespace());
                list.add(Text.translatable("enchantedtooltip.enchant.from").formatted(Formatting.WHITE).append(Text.literal(mod).formatted(Formatting.BLUE)));
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
            return Text.literal(EnchantedToolTipMod.getCustomEnchantsNames().get(enchantId));
        return Text.translatable(enchantId);
    }
}
