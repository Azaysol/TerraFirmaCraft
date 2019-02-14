/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.api.capability.forge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;

import net.dries007.tfc.api.capability.heat.IItemHeat;
import net.dries007.tfc.api.recipes.AnvilRecipe;
import net.dries007.tfc.util.forge.ForgeStep;
import net.dries007.tfc.util.forge.ForgeSteps;

/**
 * This is an advanced IItemHeat capability that is used by items that can be forged
 * If you implement this capability, you MUST implement {@link net.dries007.tfc.api.capability.heat.CapabilityItemHeat} as well
 * You should return the same instance from the getCapability calls
 */
public interface IForgeable extends IItemHeat
{
    /**
     * Gets the current amount of work on the object
     */
    int getWork();

    /**
     * Sets the current amount of work on the object
     */
    void setWork(int work);

    /**
     * Gets the current saved recipe's registry name
     * Returns null if no recipe name is currently saved
     */
    @Nullable
    ResourceLocation getRecipeName();

    /**
     * Sets the recipe name from an {@link AnvilRecipe}. If null, sets the recipe name to null
     */
    default void setRecipe(@Nullable AnvilRecipe recipe)
    {
        setRecipe(recipe != null ? recipe.getRegistryName() : null);
    }

    /**
     * Sets the recipe name from an {@link AnvilRecipe}'s registry name.
     *
     * @param recipeName a registry name of an anvil recipe
     */
    void setRecipe(@Nullable ResourceLocation recipeName);

    /**
     * Gets the last three steps, wrapped in a {@link ForgeSteps} instance.
     * The return value is nonnull, however the individual steps might be
     */
    @Nonnull
    ForgeSteps getSteps();

    /**
     * Adds a step to the object, shuffling the last three steps down
     * @param step The step to add. In general this should not be null, although it is perfectly valid for it to be
     */
    void addStep(@Nullable ForgeStep step);

    /**
     * Resets the object's {@link IForgeable} components. Used if an item falls out of an anvil without getting worked
     * Purpose is to preserve stackability on items that haven't been worked yet.
     */
    void reset();

    /**
     * Gets the working temperature of the item
     *
     * @return a temperature
     */
    default float getWorkTemp()
    {
        return getMeltTemp() * 0.6f;
    }

    /**
     * Checks if the item is hot enough to be worked
     *
     * @return true if the item is workable
     */
    default boolean isWorkable()
    {
        return getTemperature() > getWorkTemp();
    }

    /**
     * Gets the welding temperature of the item
     *
     * @return a temperature
     */
    default float getWeldTemp()
    {
        return getMeltTemp() * 0.8f;
    }

    /**
     * Checks if the item is hot enough to be worked
     *
     * @return true if the item is weldable
     */
    default boolean isWeldable()
    {
        return getTemperature() > getWeldTemp();
    }
}
