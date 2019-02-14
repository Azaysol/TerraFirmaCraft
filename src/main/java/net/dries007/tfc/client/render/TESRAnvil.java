/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import net.dries007.tfc.objects.te.TEAnvilTFC;

import static net.dries007.tfc.objects.te.TEAnvilTFC.SLOT_HAMMER;

public class TESRAnvil extends TileEntitySpecialRenderer<TEAnvilTFC>
{
    @Override
    public void render(TEAnvilTFC te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (cap != null)
        {
            int rotation = te.getBlockMetadata();
            float yOffset = te.isStone() ? 0.875f : 0.6875f;

            // Current Item
            ItemStack stack = cap.getStackInSlot(SLOT_HAMMER);
            if (!stack.isEmpty())
            {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x + 0.5, y + 0.003125D + yOffset, z + 0.5);
                GlStateManager.scale(0.35f, 0.35f, 0.35f);
                GlStateManager.rotate(90f, 1f, 0f, 0f);
                GlStateManager.rotate(90f * (float) rotation, 0f, 0f, 1f);
                GlStateManager.translate(-0.7, 0, 0);
                Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public boolean isGlobalRenderer(TEAnvilTFC te)
    {
        return false;
    }
}
