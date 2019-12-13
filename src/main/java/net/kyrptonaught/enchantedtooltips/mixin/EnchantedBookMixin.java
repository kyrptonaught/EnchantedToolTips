package net.kyrptonaught.enchantedtooltips.mixin;

import net.kyrptonaught.enchantedtooltips.EnchantToolTipHelper;
import net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.InfoEnchantment;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookMixin {

    @Inject(method = "addEnchantment(Lnet/minecraft/item/ItemStack;Lnet/minecraft/enchantment/InfoEnchantment;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getOrCreateTag()Lnet/minecraft/nbt/CompoundTag;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void ETTM$sortStoredEnchantmentsAfterCombination(ItemStack stack, InfoEnchantment enchantmentInfo, CallbackInfo ci, ListTag listTag) {
        // Sort the NBT tags based on their translated tooltip text
        Comparator<CompoundTag> enchantmentOrderComparator = Comparator
                .comparing(compoundTag -> {
                    Identifier identifier = Identifier.tryParse(compoundTag.getString("id"));
                    Enchantment enchantment = Registry.ENCHANTMENT.get(identifier);
                    return new TranslatableText(enchantment.getTranslationKey()).getString();
                }, String.CASE_INSENSITIVE_ORDER);

        List<CompoundTag> alphabeticallySortedEnchantments = listTag.stream()
                .filter(tag -> tag.getType() == 10)
                .map(CompoundTag.class::cast)
                .sorted(enchantmentOrderComparator)
                .collect(Collectors.toList());

        listTag.clear();
        listTag.addAll(alphabeticallySortedEnchantments);
    }

    @Redirect(method = "appendTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendEnchantments(Ljava/util/List;Lnet/minecraft/nbt/ListTag;)V"))
    private void ETTM$appendTooltip(List<Text> tooltip, ListTag enchantments) {
        if (MinecraftClient.getInstance().currentScreen == null)
            ItemStack.appendEnchantments(tooltip, enchantments);
        else if (EnchantedToolTipMod.getConfig().enableForBooks)
            EnchantToolTipHelper.appendToolTip(tooltip, enchantments, true);
    }
}