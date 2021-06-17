package com.nitnelave.CreeperHeal.utils;

import com.github.jikoo.planarwrappers.function.ThrowingTriFunction;
import com.nitnelave.CreeperHeal.PluginHandler;
import com.nitnelave.CreeperHeal.config.WCfgVal;
import com.nitnelave.CreeperHeal.config.WorldConfig;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * A handler for the Factions plugin.
 *
 * @author nitnelave
 */
public abstract class FactionHandler {

  private static boolean isFactionsEnabled = false;
  private static Method boardSingleton;
  private static Method boardGetFaction;
  private static Method factionIsWilderness;
  private static ThrowingTriFunction<String, Integer, Integer, Object, ReflectiveOperationException>
      getLocation;

  static {
    setFactionsEnabled(PluginHandler.isFactionsEnabled());
  }

  private static boolean isWilderness(Block block) throws Exception {
    Chunk chunk = block.getChunk();
    Object faction =
        boardGetFaction.invoke(
            boardSingleton.invoke(null),
            getLocation.apply(chunk.getWorld().getName(), chunk.getX(), chunk.getZ()));
    return faction != null && !(boolean) factionIsWilderness.invoke(faction);
  }

  /**
   * Set whether the Factions plugin is enabled.
   *
   * @param enabled Whether it is enabled.
   */
  public static void setFactionsEnabled(boolean enabled) {
    isFactionsEnabled = enabled;
    if (enabled) {
      setup();
    }
  }

  private static void setup() {
    if (!isFactionsEnabled) {
      return;
    }

    // Set up FactionsUUID.
    try {
      Class<?> boardClazz = Class.forName("com.massivecraft.factions.Board");
      boardSingleton = boardClazz.getMethod("getInstance");
      Class<?> factionLocationClazz = Class.forName("com.massivecraft.factions.FLocation");
      boardGetFaction = boardClazz.getMethod("getFactionAt", factionLocationClazz);
      Constructor<?> locationConstructor =
          factionLocationClazz.getConstructor(String.class, int.class, int.class);
      getLocation = locationConstructor::newInstance;
      Class<?> factionClazz = Class.forName("com.massivecraft.factions.Faction");
      factionIsWilderness = factionClazz.getMethod("isWilderness");

      // FactionsUUID set up successfully, we're done.
      return;
    } catch (ReflectiveOperationException e) {
      // Eat first reflection error - may be MassiveCraft Factions.
    }

    try {
      // MassiveCraft's (discontinued) Factions
      Class<?> massiveBoard = Class.forName("com.massivecraft.factions.entity.BoardColl");
      boardSingleton = massiveBoard.getDeclaredMethod("get");
      Class<?> massivePS = Class.forName("com.massivecraft.massivecore.ps.PS");
      boardGetFaction = massiveBoard.getDeclaredMethod("getFactionAt", massivePS);
      Method locationValueOf =
          massivePS.getDeclaredMethod("valueOf", String.class, int.class, int.class);
      getLocation =
          (worldName, chunkX, chunkZ) -> locationValueOf.invoke(null, worldName, chunkX, chunkZ);
      Class<?> massiveFaction = Class.forName("com.massivecraft.factions.entity.Faction");
      factionIsWilderness = massiveFaction.getDeclaredMethod("isNone");
    } catch (ReflectiveOperationException e) {
      // Factions failed to set up, disable Factions support.
      isFactionsEnabled = false;
    }
  }

  /**
   * Check if an explosion should be ignored (blocks not replaced).
   *
   * @param list The list of exploded blocks.
   * @param world The CH configuration for the world.
   * @return Whether the explosion should be ignored.
   */
  public static boolean shouldIgnore(List<Block> list, WorldConfig world) {
    if (!isFactionsEnabled) return false;

    boolean wild = world.getBool(WCfgVal.FACTIONS_IGNORE_WILDERNESS);
    boolean territory = world.getBool(WCfgVal.FACTIONS_IGNORE_TERRITORY);
    if (wild == territory) return wild;

    try {
      for (Block block : list) {
        if (wild != isWilderness(block)) {
          return false;
        }
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Check if a block should be ignored (not replaced).
   *
   * @param block The block to be checked.
   * @param world The CH configuration for the world.
   * @return Whether the replacement should be ignored.
   */
  public static boolean shouldIgnore(Block block, WorldConfig world) {
    ArrayList<Block> l = new ArrayList<>();
    l.add(block);
    return shouldIgnore(l, world);
  }
}
