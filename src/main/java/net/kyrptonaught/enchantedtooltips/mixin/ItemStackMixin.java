package net.kyrptonaught.enchantedtooltips.mixin;


import net.kyrptonaught.enchantedtooltips.EnchantToolTipHelper;
import net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Redirect(method = "getTooltip", at = @At(
            value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendEnchantments(Ljava/util/List;Lnet/minecraft/nbt/ListTag;)V"))
    private void ETTM$addEnchantInfo(List<Text> list, ListTag enchants) {
        if (MinecraftClient.getInstance().currentScreen == null || enchants.size() == 0)
            ItemStack.appendEnchantments(list, enchants);
        else if (EnchantedToolTipMod.getConfig().enableForItems)
            EnchantToolTipHelper.appendToolTip(list, enchants, true);
    }
}