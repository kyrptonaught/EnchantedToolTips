package net.kyrptonaught.enchantedtooltips.config;

import blue.endless.jankson.Comment;
import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;

public class ConfigOptions implements AbstractConfigFile {

    @Comment("Should the enchant info be displayed on enchanted book")
    public boolean enableForBooks = true;
    @Comment("Should the enchant info be displayed on enchanted tools")
    public boolean enableForItems = true;
    @Comment("Should the enchant info be displayed in Enchanting Table")
    public boolean enableForEnchantTable = true;

    @Comment("Display 'Press for info' for enchant info")
    public boolean displayPressForInfo = true;
    @Comment("Always display the Enchant Information")
    public boolean alwaysShowEnchantInfo = false;
    @Comment("Display missing enchantment data")
    public boolean displayMissingEnchant = true;

    @Comment("Should the enchant info be sorted alphabetically")
    public boolean sortEnchantInfo = false;


    @Comment("Hide enchant lvl if only I")
    public boolean hideLvlI = false;
    @Comment("Display 'Description' in tooltip")
    public boolean displayDescription = true;
    @Comment("Display 'Max level' in tooltip")
    public boolean displayMaxLvl = true;
    @Comment("Display 'Max level' after title")
    public boolean combineLvlMaxLvl = false;
    @Comment("Display 'From' in tooltip")
    public boolean displayModFrom = true;
    @Comment("Display 'Applies to' in tooltip")
    public boolean displayAppliesTo = true;

    @Comment("Display 'Applies to' only on books")
    public boolean appliesToBookOnly = false;

}
