package fr.switchback.blocklings.guis;

import fr.switchback.blocklings.entities.EntityBlockling;
import fr.switchback.blocklings.util.ResourceLocationBlocklings;
import fr.switchback.blocklings.util.helpers.GuiHelper;
import net.minecraft.entity.player.EntityPlayer;

class GuiBlocklingCombat extends GuiBlocklingAbility {
    GuiBlocklingCombat(EntityBlockling blockling, EntityPlayer player) {
        super(blockling, player);
        abilities = blockling.combatAbilities.abilities;
        BACKGROUND = new ResourceLocationBlocklings("textures/guis/inventory_overlay_5.png");
    }

    @Override
    public void initGui() {
        if (init) {
            WINDOW = new ResourceLocationBlocklings("textures/guis/inventory" + GuiHelper.Tab.COMBAT.id + ".png");
        }
        super.initGui();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        abilities = blockling.combatAbilities.abilities;
    }
}
