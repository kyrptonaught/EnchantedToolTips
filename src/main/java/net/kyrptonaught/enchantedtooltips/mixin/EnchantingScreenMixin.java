package net.kyrptonaught.enchantedtooltips.mixin;

import net.kyrptonaught.enchantedtooltips.EnchantToolTipHelper;
import net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantingScreenMixin extends HandledScreen<EnchantmentScreenHandler> {
    public EnchantingScreenMixin(EnchantmentScreenHandler container_1, PlayerInventory playerInventory_1, Text text_1) {
        super(container_1, playerInventory_1, text_1);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;II)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void addToolTipInfo(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci, boolean bl, int ii, int jj, int kk, Enchantment enchantment, int ll, int mm, List<Text> list) {
        if (EnchantedToolTipMod.getConfig().enableForEnchantTable) {
            NbtList enchants = new NbtList();
            for (int i = 0; i < this.handler.enchantmentId.length; i++) {
                int power = this.handler.enchantmentPower[i];
                Enchantment enchant = Enchantment.byRawId(this.handler.enchantmentId[i]);
                int level = this.handler.enchantmentLevel[i];
                if (this.isPointWithinBounds(60, 14 + 19 * i, 108, 17, mouseX, mouseY) && power > 0 && level >= 0 && enchant != null) {
                    NbtCompound compoundTag = new NbtCompound();
                    compoundTag.putString("id", String.valueOf(Registries.ENCHANTMENT.getId(enchant)));
                    compoundTag.putShort("lvl", (short) level);
                    enchants.add(compoundTag);
                }
            }

            //List<Text> list2 = new ArrayList<>();
            EnchantToolTipHelper.appendToolTip(list, enchants, false, true);
        }
    }
}
