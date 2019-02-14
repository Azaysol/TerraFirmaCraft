/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.api.recipes.AnvilRecipe;
import net.dries007.tfc.client.button.GuiButtonAnvilPlan;
import net.dries007.tfc.client.button.GuiButtonAnvilStep;
import net.dries007.tfc.client.button.IButtonTooltip;
import net.dries007.tfc.network.PacketGuiButton;
import net.dries007.tfc.objects.te.TEAnvilTFC;
import net.dries007.tfc.util.forge.ForgeRule;
import net.dries007.tfc.util.forge.ForgeStep;
import net.dries007.tfc.util.forge.ForgeSteps;

import static net.dries007.tfc.api.util.TFCConstants.MOD_ID;
import static net.dries007.tfc.objects.te.TEAnvilTFC.FIELD_PROGRESS;
import static net.dries007.tfc.objects.te.TEAnvilTFC.FIELD_TARGET;

public class GuiAnvilTFC extends GuiContainerTE<TEAnvilTFC>
{
    public static final ResourceLocation ANVIL_BACKGROUND = new ResourceLocation(MOD_ID, "textures/gui/anvil.png");
    public static final int BUTTON_ID_STEP_MIN = 0;
    public static final int BUTTON_ID_STEP_MAX = 7;
    public static final int BUTTON_ID_PLAN = 8;

    public GuiAnvilTFC(Container container, InventoryPlayer playerInv, TEAnvilTFC tile)
    {
        super(container, playerInv, tile, ANVIL_BACKGROUND);
        ySize = 191;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        int buttonID = -1;
        for (ForgeStep step : ForgeStep.values())
        {
            addButton(new GuiButtonAnvilStep(++buttonID, guiLeft, guiTop, step));
        }
        addButton(new GuiButtonAnvilPlan(tile, ++buttonID, guiLeft, guiTop));
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY)
    {
        // Button Tooltips
        for (GuiButton button : buttonList)
        {
            if (button instanceof IButtonTooltip && button.isMouseOver())
            {
                IButtonTooltip tooltip = (IButtonTooltip) button;
                if (tooltip.hasTooltip())
                {
                    drawHoveringText(I18n.format(tooltip.getTooltip()), mouseX, mouseY);
                }
            }
        }
        super.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        // Draw the progress indicators
        int progress = tile.getField(FIELD_PROGRESS);
        drawTexturedModalRect(guiLeft + 13 + progress, guiTop + 100, 176, 0, 5, 5);

        int target = tile.getField(FIELD_TARGET);
        drawTexturedModalRect(guiLeft + 13 + target, guiTop + 94, 181, 0, 5, 5);

        // Draw rule icons
        AnvilRecipe recipe = tile.getRecipe();
        ForgeSteps steps = tile.getSteps();
        if (recipe != null)
        {
            for (int i = 0; i < recipe.getRules().length; i++)
            {
                ForgeRule rule = recipe.getRules()[i];
                if (rule != null)
                {
                    int xOffset = i * 19;
                    // The rule icon
                    drawScaledCustomSizeModalRect(guiLeft + 64 + xOffset, guiTop + 10, rule.getU(), rule.getV(), 32, 32, 10, 10, 256, 256);

                    // The overlay
                    if (rule.matches(steps))
                    {
                        // GREEN
                        GlStateManager.color(0f, 0.6f, 0.2f);
                    }
                    else
                    {
                        // RED
                        GlStateManager.color(1f, 0.4f, 0);
                    }
                    drawTexturedModalRect(guiLeft + 59 + xOffset, guiTop + 7, 198, rule.getW(), 20, 22);
                    GlStateManager.color(1f, 1f, 1f);
                }
            }
        }

        // Draw step icons
        for (int i = 0; i < 3; i++)
        {
            ForgeStep step = steps.getStep(i);
            if (step != null)
            {
                int xOffset = i * 19;
                drawScaledCustomSizeModalRect(guiLeft + 64 + xOffset, guiTop + 31, step.getU(), step.getV(), 32, 32, 10, 10, 256, 256);
            }
        }

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        TerraFirmaCraft.getNetwork().sendToServer(new PacketGuiButton(button.id));
        super.actionPerformed(button);
    }

}
