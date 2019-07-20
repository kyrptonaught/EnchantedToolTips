package net.kyrptonaught.enchantedtooltips.mixin;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod;
import net.kyrptonaught.enchantedtooltips.config.EnchantsLookup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.player.PlayerEntity;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.util.List;

@Mixin(ItemStack.class)
public class ItemMixin {

	@Inject(at = @At("TAIL"), method = "appendEnchantments")
	@Environment(EnvType.CLIENT)
	private static void ETTM2$appendEnchantments(List<Text> list, ListTag enchants, CallbackInfo cli) {
		if (MinecraftClient.getInstance().currentScreen == null || enchants.size() == 0) return;
		if (GLFW.glfwGetKey(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) == 0) {
			String[] msg = I18n.translate("enchantedtooltip.presssneak").split("KEY");
			Text pre = new LiteralText(msg[0]);
			Text mid = new TranslatableText("enchantedtooltip.KEY").formatted(Formatting.GREEN);
			Text post = new LiteralText(msg[1]);
			list.add(pre.append(mid).append(post));
		}
	}

	@Inject(at = @At("HEAD"), method = "appendEnchantments", cancellable = true)
	@Environment(EnvType.CLIENT)
	private static void ETTM$appendEnchantments(List<Text> list, ListTag enchants, CallbackInfo cli) {
		if (GLFW.glfwGetKey(MinecraftClient.getInstance().window.getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) != 0) {
			for (int i = 0; i < enchants.size(); i++) {
				CompoundTag enchantTag = enchants.getCompoundTag(i);
				Identifier enchantID = Identifier.tryParse(enchantTag.getString("id"));
				Enchantment enchant = Registry.ENCHANTMENT.get(enchantID);
				//name
				list.add(enchant.getName(enchantTag.getInt("lvl")).formatted(Formatting.DARK_GREEN));
				//Level
				Text maxLvl = new TranslatableText("enchantment.level." + enchant.getMaximumLevel());
				list.add(new TranslatableText("enchantedtooltip.enchant.maxLevel").append(maxLvl).formatted(Formatting.GRAY));
				//applies to
				list.add(new TranslatableText("enchantedtooltip.enchant.applicableTo").append(new TranslatableText("enchantedtooltip.enchant.type." + enchant.type.name())).formatted(Formatting.GRAY));
				//desc
				list.add(ETTM$getEnchantDesc("enchantment." + enchantTag.getString("id").replace(":", ".") + ".desc").formatted(Formatting.GRAY));
				//from
				String mod = enchantID.getNamespace().toLowerCase();
				mod = StringUtils.capitalize(FabricLoader.getInstance().getModContainer(mod).map(ModContainer::getMetadata).map(ModMetadata::getName).orElse(mod));
				list.add(new TranslatableText("enchantedtooltip.enchant.from").formatted(Formatting.GRAY).append(new LiteralText(mod).formatted(Formatting.BLUE)));
			}
			cli.cancel();
			return;
		}
	}

	private static Text ETTM$getEnchantDesc(String text) {
		if (EnchantedToolTipMod.config.enchantsLookup.enchants.containsKey(text))
			return new LiteralText(EnchantedToolTipMod.config.enchantsLookup.enchants.get(text));
		return new TranslatableText(text);
	}
}
