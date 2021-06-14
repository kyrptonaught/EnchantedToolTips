package net.kyrptonaught.enchantedtooltips.mixin;

import net.kyrptonaught.enchantedtooltips.EnchantToolTipHelper;
import net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookMixin {
    @Redirect(method = "appendTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendEnchantments(Ljava/util/List;Lnet/minecraft/nbt/NbtList;)V"))
    private void ETTM$appendTooltip(List<Text> tooltip, NbtList enchantments) {
        if (MinecraftClient.getInstance().currentScreen == null)
            ItemStack.appendEnchantments(tooltip, enchantments);
        else if (EnchantedToolTipMod.getConfig().enableForBooks)
            EnchantToolTipHelper.appendToolTip(tooltip, enchantments, true,true);
    }
}