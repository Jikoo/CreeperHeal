package com.nitnelave.CreeperHeal.block;

import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;

import java.util.Collections;
import java.util.List;

/**
 * Bed implementation of a CreeperMultiblock.
 *
 * @author Jikoo
 */
class CreeperBed extends CreeperMultiblock {

    CreeperBed(BlockState blockState) {
        super(blockState);

        BlockData blockData = blockState.getBlockData();
        if (!(blockData instanceof Bed)) {
            throw new IllegalArgumentException("Invalid BlockData: " + blockData.getClass().getName());
        }

        Bed bed = ((Bed) blockData);
        BlockState head, foot;

        if (bed.getPart() == Bed.Part.HEAD) {
            head = blockState;
            foot = blockState.getBlock().getRelative(bed.getFacing().getOppositeFace()).getState();
        } else {
            head = blockState.getBlock().getRelative(bed.getFacing()).getState();
            foot = blockState;
        }

        if (head.getType() != foot.getType()) {
            // Ensure materials match.
            return;
        }

        BlockData headData = head.getBlockData();
        BlockData footData = foot.getBlockData();

        if (!(headData instanceof Bed) || !(footData instanceof Bed)
                || ((Bed) headData).getPart() == ((Bed) footData).getPart()) {
            // Ensure bed halves are correct.
            return;
        }

        this.blockState = head;
        this.dependents.add(foot);
    }

    @Override
    public List<NeighborBlock> getDependentNeighbors() {
        return Collections.emptyList();
    }

}
