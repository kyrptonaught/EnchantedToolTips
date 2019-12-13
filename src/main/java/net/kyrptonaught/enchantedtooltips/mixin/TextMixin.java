package net.kyrptonaught.enchantedtooltips.mixin;

import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Text.class)
public interface TextMixin extends Comparable<Text> {

    @Shadow String getString();

    @Override
    default int compareTo(Text other) {
        return this.getString().compareTo(other.getString());
    }
}
