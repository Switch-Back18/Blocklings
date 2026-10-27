package fr.switchback.blocklings.util;

import fr.switchback.blocklings.Blocklings;
import net.minecraft.util.ResourceLocation;

public class ResourceLocationBlocklings extends ResourceLocation {
    public ResourceLocationBlocklings(String name) {
        super(Blocklings.MODID + ":" + name);
    }
}
