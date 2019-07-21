package net.kyrptonaught.enchantedtooltips;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

class GenEnchantsCommand {
    private static final Identifier genEnchant_packet = new Identifier(EnchantedToolTipMod.MOD_ID, "genenchant_packet");

    static void registerCommand() {

        CommandRegistry.INSTANCE.register(false, GenEnchantsCommand::register);
        CommandRegistry.INSTANCE.register(true, GenEnchantsCommand::register);
    }

    private static void register(CommandDispatcher dispatcher) {
        dispatcher.register((CommandManager.literal("genEnchantList").
                requires((serverCommandSource_1) -> serverCommandSource_1.hasPermissionLevel(0)).
                executes((commandContext_1) -> executeSend(commandContext_1.getSource(), false)).
                then((CommandManager.literal("overwrite")).
                        then(CommandManager.argument("shouldOverwrite", BoolArgumentType.bool()).
                                executes((commandContext_1) -> executeSend(commandContext_1.getSource(), BoolArgumentType.getBool(commandContext_1, "shouldOverwrite")))))));
    }

    private static int executeSend(ServerCommandSource serverCommandSource, boolean shouldOverwrite) {
        try {
            serverCommandSource.getPlayer().networkHandler.sendPacket(createPacket(shouldOverwrite));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private static CustomPayloadS2CPacket createPacket(boolean overwrite) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(overwrite);
        return new CustomPayloadS2CPacket(genEnchant_packet, new PacketByteBuf(buf));
    }

    static void registerReceivePacket() {
        ClientSidePacketRegistry.INSTANCE.register(genEnchant_packet, (packetContext, packetByteBuf) -> {
            boolean overwrites = packetByteBuf.readBoolean();
            packetContext.getTaskQueue().execute(() -> {
                PlayerEntity player = packetContext.getPlayer();
                for (Identifier identifier : Registry.ENCHANTMENT.getIds()) {
                    String id = identifier.toString();
                    String enchant = "enchantment." + id.replace(":", ".") + ".desc";
                    if (overwrites || !EnchantedToolTipMod.config.enchantsLookup.enchants.containsKey(enchant))
                        EnchantedToolTipMod.config.enchantsLookup.enchants.put(enchant, I18n.translate(enchant));
                }
                EnchantedToolTipMod.config.saveEnchants();
                player.addChatMessage(new TranslatableText("enchantedtooltip.command.output"), false);

            });
        });
    }
}
