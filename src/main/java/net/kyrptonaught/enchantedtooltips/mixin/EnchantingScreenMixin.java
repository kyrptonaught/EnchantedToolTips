package net.kyrptonaught.enchantedtooltips.mixin;

import net.kyrptonaught.enchantedtooltips.EnchantToolTipHelper;
import net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod;
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
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantingScreenMixin extends HandledScreen<EnchantmentScreenHandler> {
    public EnchantingScreenMixin(EnchantmentScreenHandler container_1, PlayerInventory playerInventory_1, Text text_1) {
        super(container_1, playerInventory_1, text_1);
    }

    @Override
    public void renderTooltip(MatrixStack matrices, List<Text> lines, int x, int y) {
        if (EnchantedToolTipMod.getConfig().enableForEnchantTable) {
            NbtList enchants = new NbtList();
            for (int i = 0; i < this.handler.enchantmentId.length; i++) {
                int power = this.handler.enchantmentPower[i];
                Enchantment enchant = Enchantment.byRawId(this.handler.enchantmentId[i]);
                int level = this.handler.enchantmentLevel[i];
                if (this.isPointWithinBounds(60, 14 + 19 * i, 108, 17, x, y) && power > 0 && level >= 0 && enchant != null) {
                    NbtCompound compoundTag = new NbtCompound();
                    compoundTag.putString("id", String.valueOf(Registries.ENCHANTMENT.getId(enchant)));
                    compoundTag.putShort("lvl", (short) level);
                    enchants.add(compoundTag);
                }
            }

            List<Text> list2 = new ArrayList<>();
            EnchantToolTipHelper.appendToolTip(list2, enchants, false, true);
            lines.addAll(list2);
        }
        super.renderTooltip(matrices, lines, x, y);
    }

    public void renderTooltip(MatrixStack matrixStack, Text text, int i, int j) {
        this.renderOrderedTooltip(matrixStack, Arrays.asList(text.asOrderedText()), i, j);
    }

    @Override
    protected void renderTooltip(MatrixStack matrices, ItemStack itemStack_1, int int_1, int int_2) {
        super.renderTooltip(matrices, this.getTooltipFromItem(itemStack_1), int_1, int_2);
    }
}
