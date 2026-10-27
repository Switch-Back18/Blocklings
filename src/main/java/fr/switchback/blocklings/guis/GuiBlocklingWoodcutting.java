package fr.switchback.blocklings.guis;

import fr.switchback.blocklings.entities.EntityBlockling;
import fr.switchback.blocklings.util.ResourceLocationBlocklings;
import fr.switchback.blocklings.util.helpers.GuiHelper;
import net.minecraft.entity.player.EntityPlayer;

class GuiBlocklingWoodcutting extends GuiBlocklingAbility {
    GuiBlocklingWoodcutting(EntityBlockling blockling, EntityPlayer player) {
        super(blockling, player);
        abilities = blockling.woodcuttingAbilities.abilities;
        BACKGROUND = new ResourceLocationBlocklings("textures/guis/inventory_overlay_2.png");
    }

    @Override
    public void initGui() {
        if (init) {
            WINDOW = new ResourceLocationBlocklings("textures/guis/inventory" + GuiHelper.Tab.WOODCUTTING.id + ".png");
        }
        super.initGui();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        abilities = blockling.woodcuttingAbilities.abilities;
    }
}
