package net.kyrptonaught.enchantedtooltips.mixin;

import net.kyrptonaught.enchantedtooltips.EnchantToolTipHelper;
import net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantingScreenMixin extends HandledScreen<EnchantmentScreenHandler> {
    public EnchantingScreenMixin(EnchantmentScreenHandler container_1, PlayerInventory playerInventory_1, Text text_1) {
        super(container_1, playerInventory_1, text_1);
    }

    @Override
    public void renderTooltip(MatrixStack matrices, List<? extends StringRenderable> lines, int x, int y) {
        List<Text> betteLines = (List<Text>)lines;
        if (EnchantedToolTipMod.getConfig().enableForEnchantTable) {
            ListTag enchants = new ListTag();
            for (int i = 0; i < this.handler.enchantmentId.length; i++) {
                int power = this.handler.enchantmentPower[i];
                Enchantment enchant = Enchantment.byRawId(this.handler.enchantmentId[i]);
                int level = this.handler.enchantmentLevel[i];
                if (this.isPointWithinBounds(60, 14 + 19 * i, 108, 17, x, y) && power > 0 && level >= 0 && enchant != null) {
                    CompoundTag compoundTag = new CompoundTag();
                    compoundTag.putString("id", String.valueOf(Registry.ENCHANTMENT.getId(enchant)));
                    compoundTag.putShort("lvl", (short) level);
                    enchants.add(compoundTag);
                }
            }

            List<Text> list2 = new ArrayList<>();
            EnchantToolTipHelper.appendToolTip(list2, enchants, false);
            betteLines.addAll(list2);
        }
        super.renderTooltip(matrices,betteLines, x, y);
    }

    @Override
    public void renderTooltip(MatrixStack matrices, StringRenderable stringRenderable, int x, int y) {
        super.renderTooltip(matrices,Collections.singletonList(stringRenderable), x, y);
    }

    @Override
    protected void renderTooltip(MatrixStack matrices, ItemStack itemStack_1, int int_1, int int_2) {
        super.renderTooltip(matrices,this.getTooltipFromItem(itemStack_1), int_1, int_2);
    }
}
