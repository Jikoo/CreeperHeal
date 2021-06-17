package com.nitnelave.CreeperHeal.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;

import java.util.Set;

/**
 * Additional supplementary tags.
 *
 * @author Jikoo
 */
public class CreeperTag implements Tag<Material> {

  public static final CreeperTag INFESTED_BLOCKS =
      new CreeperTag(
          "infested_blocks",
          Material.INFESTED_CHISELED_STONE_BRICKS,
          Material.INFESTED_COBBLESTONE,
          Material.INFESTED_CRACKED_STONE_BRICKS,
          Material.INFESTED_MOSSY_STONE_BRICKS,
          Material.INFESTED_STONE,
          Material.INFESTED_STONE_BRICKS);

  public static final CreeperTag STANDING_BANNERS =
      new CreeperTag(
          "standing_banners",
          Material.BLACK_BANNER,
          Material.BLUE_BANNER,
          Material.BROWN_BANNER,
          Material.CYAN_BANNER,
          Material.GRAY_BANNER,
          Material.GREEN_BANNER,
          Material.LIGHT_BLUE_BANNER,
          Material.LIGHT_GRAY_BANNER,
          Material.LIME_BANNER,
          Material.MAGENTA_BANNER,
          Material.ORANGE_BANNER,
          Material.PINK_BANNER,
          Material.PURPLE_BANNER,
          Material.RED_BANNER,
          Material.WHITE_BANNER,
          Material.YELLOW_BANNER);

  public static final CreeperTag WALL_BANNERS =
      new CreeperTag(
          "wall_banners",
          Material.BLACK_WALL_BANNER,
          Material.BLUE_WALL_BANNER,
          Material.BROWN_WALL_BANNER,
          Material.CYAN_WALL_BANNER,
          Material.GRAY_WALL_BANNER,
          Material.GREEN_WALL_BANNER,
          Material.LIGHT_BLUE_WALL_BANNER,
          Material.LIGHT_GRAY_WALL_BANNER,
          Material.LIME_WALL_BANNER,
          Material.MAGENTA_WALL_BANNER,
          Material.ORANGE_WALL_BANNER,
          Material.PINK_WALL_BANNER,
          Material.PURPLE_WALL_BANNER,
          Material.RED_WALL_BANNER,
          Material.WHITE_WALL_BANNER,
          Material.YELLOW_WALL_BANNER);

  private final NamespacedKey key;
  private final Set<Material> tagged;

  private CreeperTag(String key, Material... elements) {
    //noinspection deprecation
    this.key = new NamespacedKey("creeperheal", key);
    tagged = CreeperUtils.createFinalHashSet(elements);
  }

  @Override
  public boolean isTagged(Material keyed) {
    return tagged.contains(keyed);
  }

  @Override
  public Set<Material> getValues() {
    return tagged;
  }

  @Override
  public NamespacedKey getKey() {
    return key;
  }
}
