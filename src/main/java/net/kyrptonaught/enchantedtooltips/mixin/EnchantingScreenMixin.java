package net.kyrptonaught.enchantedtooltips.mixin;

import net.kyrptonaught.enchantedtooltips.EnchantToolTipHelper;
import net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.gui.screen.ingame.EnchantingScreen;
import net.minecraft.container.EnchantingTableContainer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(EnchantingScreen.class)
public abstract class EnchantingScreenMixin extends AbstractContainerScreen<EnchantingTableContainer> {
    public EnchantingScreenMixin(EnchantingTableContainer container_1, PlayerInventory playerInventory_1, Text text_1) {
        super(container_1, playerInventory_1, text_1);
    }

    @Override
    public void renderTooltip(List<String> list, int int_1, int int_2) {
        if (EnchantedToolTipMod.getConfig().enableForEnchantTable) {
            ListTag enchants = new ListTag();
            for (int i = 0; i < this.container.enchantmentId.length; i++) {
                int power = this.container.enchantmentPower[i];
                Enchantment enchant = Enchantment.byRawId(this.container.enchantmentId[i]);
                int level = this.container.enchantmentLevel[i];
                if (this.isPointWithinBounds(60, 14 + 19 * i, 108, 17, int_1, int_2) && power > 0 && level >= 0 && enchant != null) {
                    CompoundTag compoundTag = new CompoundTag();
                    compoundTag.putString("id", String.valueOf(Registry.ENCHANTMENT.getId(enchant)));
                    compoundTag.putShort("lvl", (short) level);
                    enchants.add(compoundTag);
                }
            }

            List<Text> list2 = new ArrayList<>();
            EnchantToolTipHelper.appendToolTip(list2, enchants, false);
            list.addAll(1, list2.stream().map(Text::asFormattedString).collect(Collectors.toList()));
        }
        super.renderTooltip(list, int_1, int_2);
    }

    @Override
    public void renderTooltip(String string_1, int int_1, int int_2) {
        super.renderTooltip(Collections.singletonList(string_1), int_1, int_2);
    }

    @Override
    protected void renderTooltip(ItemStack itemStack_1, int int_1, int int_2) {
        super.renderTooltip(this.getTooltipFromItem(itemStack_1), int_1, int_2);
    }
}
