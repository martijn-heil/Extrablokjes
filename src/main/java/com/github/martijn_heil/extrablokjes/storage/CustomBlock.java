package com.github.martijn_heil.extrablokjes.storage;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import static java.lang.Math.min;

public class CustomBlock {

    private final ItemStack item;
    private final boolean dropsWithoutTool;
    private final String toolMatcher;
    private final double blockHardness;

    public ItemStack getItem() { return item; }
    public boolean doesDropWithoutTool() { return dropsWithoutTool; }
    public String getToolMatcher() { return toolMatcher; }
    public double getBlockHardness() { return blockHardness; }

    public CustomBlock(ItemStack item, boolean dropsWithoutTool, String toolMatcher, double blockHardness) {
        this.item = item;
        this.dropsWithoutTool = dropsWithoutTool;
        this.toolMatcher = toolMatcher;
        this.blockHardness = blockHardness;
    }

    private enum ToolMultiplier {
        NONE(1), WOOD(2), STONE(4), IRON(6), DIAMOND(8), NETHERITE(9), GOLD(12), SHEARS(5);

        public final float multiplier;
        ToolMultiplier(float multiplier) {
            this.multiplier = multiplier;
        }

        public static ToolMultiplier getToolMultiplier(Material tool) {
            String toolName = tool.toString().toLowerCase();
            if (toolName.startsWith("wooden")) { return WOOD; }
            else if (toolName.startsWith("stone")) { return STONE; }
            else if (toolName.startsWith("iron")) { return IRON; }
            else if (toolName.startsWith("diamond")) { return DIAMOND; }
            else if (toolName.startsWith("netherite")) { return NETHERITE; }
            else if (toolName.startsWith("gold")) { return GOLD; }
            else if (toolName.equals("shears")) { return SHEARS; }
            return NONE;
        }
    }

    /**
     * Impleemnted as explained here: <a href="https://minecraft.fandom.com/wiki/Breaking">...</a>
     * @param player The player breaking
     * @param item The item used to break
     * @return The calculated damage
     */
    public float calculateNextDamage(Player player, ItemStack item) {
        Material itemMaterial = item.getType();
        String toolName = itemMaterial.toString().toLowerCase();

        float speedMultiplier = ToolMultiplier.NONE.multiplier;

        if (toolName.matches(toolMatcher)) {
            speedMultiplier = ToolMultiplier.getToolMultiplier(itemMaterial).multiplier;

            if (!(dropsWithoutTool && toolName.matches(toolMatcher))) {
                speedMultiplier = 1;
            }

            if (item.containsEnchantment(Enchantment.DIG_SPEED)) {
                int efficiencyLevel = item.getEnchantmentLevel(Enchantment.DIG_SPEED);
                speedMultiplier +=  efficiencyLevel * efficiencyLevel + 1;
            }
        }

        if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
            int hasteLevel = player.getPotionEffect(PotionEffectType.FAST_DIGGING).getAmplifier();
            speedMultiplier *= 0.2 * hasteLevel + 1;
        }

        if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
            int fatigueLevel = min(player.getPotionEffect(PotionEffectType.SLOW_DIGGING).getAmplifier(), 4);
            speedMultiplier *= Math.pow(0.3, fatigueLevel);
        }

        if (player.isInWater() && !player.hasPotionEffect(PotionEffectType.WATER_BREATHING)) {
            speedMultiplier /= 5;
        }

        // if (player.isOnGround()) deprecated
        if (player.getLocation().add(new Vector(0, -1, 0)).getBlock().isPassable()) {
            speedMultiplier /= 5;
        }

        float damage = (float) (speedMultiplier / blockHardness);
        if (dropsWithoutTool && toolName.matches(toolMatcher)) {
            damage /= 30;
        } else {
            damage /= 100;
        }

        if (damage > 1) { return 0; }

        return damage;

    }

}
