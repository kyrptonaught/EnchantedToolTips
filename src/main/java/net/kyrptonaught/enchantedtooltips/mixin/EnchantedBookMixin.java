package net.kyrptonaught.enchantedtooltips.mixin;

import net.kyrptonaught.enchantedtooltips.EnchantToolTipHelper;
import net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookMixin extends Item {

    public EnchantedBookMixin(Settings item$Settings_1) {
        super(item$Settings_1);
    }

    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    public void ETTM$appendTooltip(ItemStack itemStack_1, World world_1, List<Text> list_1, TooltipContext tooltipContext_1, CallbackInfo cbi) {
        super.appendTooltip(itemStack_1, world_1, list_1, tooltipContext_1);
        if (MinecraftClient.getInstance().currentScreen == null) {
            ItemStack.appendEnchantments(list_1, EnchantedBookItem.getEnchantmentTag(itemStack_1));
            return;
        }
        if (EnchantedToolTipMod.getConfig().enableForBooks) {
            EnchantToolTipHelper.appendToolTip(list_1, EnchantedBookItem.getEnchantmentTag(itemStack_1),true);
            cbi.cancel();
            return;
        }

    }
}
