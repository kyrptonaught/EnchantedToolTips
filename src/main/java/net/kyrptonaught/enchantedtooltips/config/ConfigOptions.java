package net.kyrptonaught.enchantedtooltips.config;

import blue.endless.jankson.Comment;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigOptions {

    @Comment("Should the enchant info be displayed on enchanted book")
    public boolean enableForBookS = true;
    @Comment("Should the enchant info be displayed on enchanted tools")
    public boolean enableForItems = true;

    public boolean diaplyDesc = true;
    public boolean displayMaxLvl = true;
    public boolean displayModFrom = true;
    public boolean displayAplliesTo = true;

}
