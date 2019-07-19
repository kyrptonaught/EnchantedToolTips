package net.kyrptonaught.enchantedtooltips;

import com.mojang.brigadier.Command;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

import java.io.*;
import java.nio.file.Files;
import java.util.Iterator;

public class TranslationCommand {
    private static final Identifier UNLOCAL_PACKET = new Identifier(EnchantedToolTipMod.MOD_ID, "unlocal_packet");

    public static void registerCommand() {
        CommandRegistry.INSTANCE.register(false, (dispatcher) -> dispatcher.register(
                CommandManager.literal("genEnchantsTransaltion")
                        .executes(c -> {
                            c.getSource().getPlayer().networkHandler.client.send(createPacket());
                            return Command.SINGLE_SUCCESS;
                        })
        ));
        CommandRegistry.INSTANCE.register(true, (dispatcher) -> dispatcher.register(
                CommandManager.literal("genEnchantsTransaltion")
                        .executes(c -> {
                            c.getSource().getPlayer().interactionManager.world.sendPacket(createPacket());
                            return Command.SINGLE_SUCCESS;
                        })
        ));
    }

    private static CustomPayloadS2CPacket createPacket() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        return new CustomPayloadS2CPacket(UNLOCAL_PACKET, new PacketByteBuf(buf));
    }

    public static void registerReceivePacket() {
        ClientSidePacketRegistry.INSTANCE.register(UNLOCAL_PACKET, (packetContext, packetByteBuf) -> {
            packetContext.getTaskQueue().execute(() -> {
                PlayerEntity player = packetContext.getPlayer();
                Iterator<Identifier> ids = Registry.ENCHANTMENT.getIds().iterator();
                File outputFile = new File(FabricLoader.getInstance().getGameDirectory(), "EnchantedToolTips");
                try {
                    Files.createDirectories(outputFile.toPath());
                    String outputs = "{\n";
                    while (ids.hasNext()) {
                        String id = ids.next().toString();
                        String enchant = "enchantment." + id.replace(":", ".") + ".desc";
                        outputs += '"' + enchant + '"' + " : " + '"' + " " + '"' + ",\n";
                    }
                    FileOutputStream out = new FileOutputStream(outputFile + "/" + EnchantedToolTipMod.MOD_ID + "GennedEnchants.json", false);
                    out.write((outputs + "\n}").getBytes());
                    out.flush();
                    out.close();
                    player.addChatMessage(new TranslatableText("enchantedtooltip.command.output"), false);

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(EnchantedToolTipMod.MOD_ID + ": error writing translation file");
                    player.addChatMessage(new TranslatableText("enchantedtooltip.command.error"), false);
                }
            });
        });
    }
}
