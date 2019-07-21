package net.kyrptonaught.enchantedtooltips.config;

import blue.endless.jankson.Comment;

public class ConfigOptions {

    @Comment("Should the enchant info be displayed on enchanted book")
    public boolean enableForBooks = true;
    @Comment("Should the enchant info be displayed on enchanted tools")
    public boolean enableForItems = true;

    @Comment("Display 'Description' in tooltip")
    public boolean displayDescription = true;
    @Comment("Display 'Max level' in tooltip")
    public boolean displayMaxLvl = true;
    @Comment("Display 'From' in tooltip")
    public boolean displayModFrom = true;
    @Comment("Display 'Applies to' in tooltip")
    public boolean displayAppliesTo = true;

}
