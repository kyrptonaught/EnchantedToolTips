package net.kyrptonaught.enchantedtooltips;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GenEnchantsCommand implements ClientCommandPlugin {

    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register((ArgumentBuilders.literal("genEnchantList").
                requires((serverCommandSource_1) -> serverCommandSource_1.hasPermissionLevel(0)).
                executes((commandContext_1) -> execute(commandContext_1.getSource(), false)).
                then((ArgumentBuilders.literal("overwrite")).
                        then(ArgumentBuilders.argument("shouldOverwrite", BoolArgumentType.bool()).
                                executes((commandContext_1) -> execute(commandContext_1.getSource(), BoolArgumentType.getBool(commandContext_1, "shouldOverwrite")))))));
    }

    private static int execute(CottonClientCommandSource serverCommandSource, boolean shouldOverwrite) {
        for (Identifier identifier : Registry.ENCHANTMENT.getIds()) {
            String id = identifier.toString();
            String enchant = "enchantment." + id.replace(":", ".") + ".desc";
            if (shouldOverwrite || !EnchantedToolTipMod.getEnchantTranslations().containsKey(enchant))
                EnchantedToolTipMod.getEnchantTranslations().put(enchant, I18n.translate(enchant));
        }
        EnchantedToolTipMod.config.saveAll();
        serverCommandSource.sendFeedback(new TranslatableText("enchantedtooltip.command.output"));
        serverCommandSource.sendFeedback(new TranslatableText("enchantedtooltip.command.outputLocation").append(EnchantedToolTipMod.config.getEnchantFileLocation()));

        return 1;
    }
}
