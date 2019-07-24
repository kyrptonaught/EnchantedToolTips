package net.kyrptonaught.enchantedtooltips;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.kyrptonaught.enchantedtooltips.config.ConfigOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.enchantment.Enchantment;
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

import java.util.List;

public class EnchantToolTipHelper {


    public static void appendToolTip(List<Text> list, ListTag enchants) {
        long hndle = MinecraftClient.getInstance().window.getHandle();
        if (EnchantedToolTipMod.config.config.alwaysShowEnchantInfo || GLFW.glfwGetKey(hndle, GLFW.GLFW_KEY_LEFT_SHIFT) != 0)
            appendEnchantInfo(list, enchants);
        else if (EnchantedToolTipMod.config.config.displayPressForInfo) appendKeyHandler(list);
    }

    private static void appendKeyHandler(List<Text> list) {
        String[] msg = I18n.translate("enchantedtooltip.presssneak").split("KEY");
        Text pre = new LiteralText(msg[0]);
        Text mid = new TranslatableText("enchantedtooltip.KEY").formatted(Formatting.GREEN);
        Text post = new LiteralText(msg[1]);
        list.add(pre.append(mid).append(post));
    }


    private static void appendEnchantInfo(List<Text> list, ListTag enchants) {
        for (int i = 0; i < enchants.size(); i++) {
            ConfigOptions options = EnchantedToolTipMod.config.config;
            CompoundTag enchantTag = enchants.getCompoundTag(i);
            Identifier enchantID = Identifier.tryParse(enchantTag.getString("id"));
            Enchantment enchant = Registry.ENCHANTMENT.get(enchantID);
            //name
            Text lvl = new TranslatableText("enchantment.level." + enchantTag.getInt("lvl"));
            Text name = new TranslatableText(enchant.getTranslationKey()).append(" ").append(lvl);
            list.add(name.formatted(enchant.isCursed() ? Formatting.RED : Formatting.DARK_GREEN));
            //desc
            if (options.displayDescription) {
                list.add(new LiteralText(" ").append(ETTM$getEnchantDesc("enchantment." + enchantTag.getString("id").replace(":", ".") + ".desc")).formatted(Formatting.WHITE));
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
                String mod = enchantID.getNamespace().toLowerCase();
                mod = StringUtils.capitalize(FabricLoader.getInstance().getModContainer(mod).map(ModContainer::getMetadata).map(ModMetadata::getName).orElse(mod));
                list.add(new TranslatableText("enchantedtooltip.enchant.from").formatted(Formatting.WHITE).append(new LiteralText(mod).formatted(Formatting.BLUE)));
            }
        }
    }

    private static Text ETTM$getEnchantDesc(String text) {
        if (EnchantedToolTipMod.config.enchantsLookup.enchants.containsKey(text))
            return new LiteralText(EnchantedToolTipMod.config.enchantsLookup.enchants.get(text));
        return new TranslatableText(text);
    }
}
