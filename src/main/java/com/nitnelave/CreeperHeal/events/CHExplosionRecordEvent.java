package com.nitnelave.CreeperHeal.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

/**
 * CHExplosionRecordEvent is fired as soon as CreeperHeal starts processing an EntityExplodeEvent,
 * listening allows you to manipulate the blocks which will be or healed.
 */
public class CHExplosionRecordEvent extends Event implements Cancellable {
  private static final HandlerList handlers = new HandlerList();
  private final List<Block> healBlocks;
  private final List<Block> protectBlocks;
  private final Location location;
  private final ExplosionReason reason;
  private boolean cancelled = false;

  public CHExplosionRecordEvent(List<Block> blocks, Location location, ExplosionReason reason) {
    this.healBlocks = new ArrayList<>(blocks);
    this.protectBlocks = new ArrayList<>();
    this.location = location;
    this.reason = reason;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  /**
   * Blocks removed from this list will explode naturally if they were in the explosion.
   *
   * <p>Adding blocks to this list will mark them for deletion (if normally exploded (config
   * dependent)), then healed when appropriate.
   *
   * <p>Recommended to use protectBlock, processBlock and explodeBlock for readability.
   *
   * @return A mutable list of blocks that are currently listed for CreeperHeal processing (not
   *     guaranteed to heal).
   */
  public List<Block> getBlocks() {
    return healBlocks;
  }

  /** Deprecated due to changed behavior. Now acts as it originally should have. */
  @Deprecated
  public void setBlocks(List<Block> blockList) {
    healBlocks.clear();
    healBlocks.addAll(blockList);
  }

  public List<Block> getProtectedBlocks() {
    return protectBlocks;
  }

  /** @return The location of the entity causing the explosion. */
  public Location getLocation() {
    return location;
  }

  public ExplosionReason getReason() {
    return reason;
  }

  /** Marks a block to be protected by CreeperHeal. */
  public void protectBlock(Block block) {
    healBlocks.remove(block);
    if (!protectBlocks.contains(block)) protectBlocks.add(block);
  }

  /**
   * Adds the block to the list for CreeperHeal to process (not guaranteed to heal depends on
   * config).
   */
  public void processBlock(Block block) {
    if (!healBlocks.contains(block)) healBlocks.add(block);
    protectBlocks.remove(block);
  }

  /**
   * Prevents CreeperHeal processing the block, letting minecraft/bukkit/plugins deal with the block
   * as appropriate.
   */
  public void explodeBlock(Block block) {
    healBlocks.remove(block);
    protectBlocks.remove(block);
  }

  public enum ExplosionReason {
    CREEPER,
    TNT,
    DRAGON,
    OTHER,
    GHAST
  }
}
