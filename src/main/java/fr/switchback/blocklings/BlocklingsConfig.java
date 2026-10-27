package fr.switchback.blocklings;

import net.minecraftforge.common.config.Config;

@Config(modid = Blocklings.MODID, name = "blocklings-config", type = Config.Type.INSTANCE)
public class BlocklingsConfig {
    @Config.Comment("List of Sword (format: modid:item)")
    @Config.Name("Swords")
    public static String[] swords = {
            "tconstruct:broadsword",
            "tconstruct:longsword",
            "tconstruct:cleaver",
            "draconicevolution:draconic_sword",
            "draconicevolution:wyvern_sword",
            "projecte:item.pe_rm_sword",
            "projecte:item.pe_dm_sword",
            "twilightforest:fiery_sword",
            "twilightforest:minotaur_axe",
            "twilightforest:ice_sword",
            "twilightforest:glass_sword",
            "bloodmagic:bound_sword",
            "bloodmagic:sentient_sword",
            "redstonearsenal:tool.sword_flux",
            "botania:starsword",
            "botania:thundersword",
            "ic2:nano_saber",
            "thaumcraft:void_sword",
            "thaumcraft:crimson_blade",
            "msmlegacy:dawn_star",
            "msmlegacy:vampiric_sword",
            "mekanism:atomicdisassembler",
            "teslacorelib:energy_sword"
    };

    @Config.Comment("List of Pickaxe (format: modid:item)")
    @Config.Name("Pickaxe")
    public static String[] pickaxes = {
            "tconstruct:pickaxe",
            "tconstruct:mattock",
            "tconstruct:hammer",
            "draconicevolution:wyvern_pick",
            "draconicevolution:draconic_pick",
            "draconicevolution:draconic_staff_of_power",
            "draconicadditions:chaotic_staff_of_power",
            "projecte:item.pe_dm_pick",
            "projecte:item.pe_rm_pick",
            "botania:manasteelpick",
            "botania:elementiumpick",
            "botania:terrapick",
            "bloodmagic:bound_pickaxe",
            "redstonearsal:tool.pickaxe_flux",
            "thermalinnovation:drill",
            "industrialforegoing:infinity_drill",
            "actuallyadditions:item_drill",
            "actuallyadditions:iron_paxel",
            "actuallyadditions:golden_paxel",
            "actuallyadditions:stone_paxel",
            "actuallyadditions:diamond_paxel",
            "actuallyadditions:wooden_paxel",
            "mekanism:atomicdisassembler",
            "bloodmagic:sentient_pickaxe",
            "thaumcraft:elemental_pick",
            "thaumcraft:void_pick",
            "roots:living_pickaxe",
            "astralsorcery:itemcrystalpickaxe"
    };

    @Config.Comment("List of Hoe (format: modid:item)")
    @Config.Name("Hoes")
    public static String[] hoes = {
            "tconstruct:scythe",
            "draconicevolution:wyvern_hoe",
            "draconicevolution:draconic_hoe",
            "projecte:item.pe_dm_hoe",
            "projecte:item.pe_rm_hoe"
    };

    @Config.Comment("List of Axe (format: modid:item)")
    @Config.Name("Axes")
    public static String[] axes = {
            "tconstruct:hatchet",
            "tconstruct:mattock",
            "tconstruct:lumberaxe",
            "draconicevolution:wyvern_axe",
            "draconicevolution:draconic_axe",
            "draconicevolution:draconic_staff_of_power",
            "draconicadditions:chaotic_staff_of_power",
            "projecte:item.pe_dm_axe",
            "projecte:item.pe_rm_axe",
            "botania:manasteelaxe",
            "botania:elementiumaxe",
            "bloodmagic:bound_axe",
            "redstonearsal:tool.axe_flux",
            "thermalinnovation:saw",
            "actuallyadditions:iron_paxel",
            "actuallyadditions:golden_paxel",
            "actuallyadditions:stone_paxel",
            "actuallyadditions:diamond_paxel",
            "actuallyadditions:wooden_paxel",
            "mekanism:atomicdisassembler",
            "bloodmagic:sentient_axe",
            "thaumcraft:void_axe",
            "roots:living_axe",
            "astralsorcery:itemcrystalaxe"
    };
}
