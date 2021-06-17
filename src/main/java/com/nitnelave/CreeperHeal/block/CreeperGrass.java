package com.nitnelave.CreeperHeal.block;

import com.nitnelave.CreeperHeal.config.CreeperConfig;
import com.nitnelave.CreeperHeal.config.WCfgVal;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

class CreeperGrass extends CreeperBlock {

  CreeperGrass(BlockState blockState) {
    super(blockState);
    if (CreeperConfig.getWorld(getWorld()).getBool(WCfgVal.GRASS_TO_DIRT))
      blockState.setType(Material.DIRT);
  }
}
