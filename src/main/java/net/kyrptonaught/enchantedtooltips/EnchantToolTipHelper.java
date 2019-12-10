package net.kyrptonaught.enchantedtooltips;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.kyrptonaught.enchantedtooltips.config.ConfigOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.List;

public class EnchantToolTipHelper {
    private static HashMap<String, String> modCache = new HashMap<>();

    static {
        modCache.put("biom4st3rmoenchantments", "Mo' Enchantments");
    }

    public static void appendToolTip(List<Text> list, ListTag enchants, boolean isItem) {
        if (EnchantedToolTipMod.getConfig().alwaysShowEnchantInfo || GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) != 0)
            appendEnchantInfo(list, enchants);
        else {
            if (isItem)
                ItemStack.appendEnchantments(list, enchants);
            if (EnchantedToolTipMod.getConfig().displayPressForInfo)
                appendKeyHandler(list);
        }
    }

    private static void appendKeyHandler(List<Text> list) {
        String msg = I18n.translate("enchantedtooltip.presssneak").replaceAll("KEY", I18n.translate("enchantedtooltip.KEY"));
        list.add(new LiteralText(msg));
    }


    private static void appendEnchantInfo(List<Text> list, ListTag enchants) {
        for (int i = 0; i < enchants.size(); i++) {
            ConfigOptions options = EnchantedToolTipMod.getConfig();
            CompoundTag enchantTag = enchants.getCompound(i);
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
            Text lvl = new TranslatableText("enchantment.level." + enchantTag.getInt("lvl"));
            Text name = new TranslatableText(enchant.getTranslationKey()).append(" ").append(lvl);
            list.add(name.formatted(enchant.isCursed() ? Formatting.RED : Formatting.DARK_GREEN));
            //desc
            if (options.displayDescription) {
                list.add(new LiteralText(" ").append(getEnchantDesc("enchantment." + enchantTag.getString("id").replace(":", ".") + ".desc")).formatted(Formatting.WHITE));
            }
            //Level
            if (options.displayMaxLvl) {
                Text maxLvl = new TranslatableText("enchantment.level." + enchant.getMaximumLevel());
                list.add(new TranslatableText("enchantedtooltip.enchant.maxLevel").append(maxLvl).formatted(Formatting.WHITE));
            }
            //applies to
            if (options.displayAppliesTo) {
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
