package pl.memexurer.krecenie;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public enum Direction {
    NORTH(BlockFace.NORTH, BlockFace.WEST), //north -> west
    EAST(BlockFace.EAST, BlockFace.NORTH), // east -> north
    SOUTH(BlockFace.SOUTH, BlockFace.EAST), // south -> east
    WEST(BlockFace.WEST, BlockFace.SOUTH); // west -> south

    private final BlockFace blockFace;
    private final BlockFace xDirection;

    Direction(final BlockFace blockFace, final BlockFace xDirection) {
        this.blockFace = blockFace;
        this.xDirection = xDirection;
    }

    public static Direction fromBlockFace(final BlockFace blockFace, BlockFace oppositeFace) {
        if (blockFace == BlockFace.UP || blockFace == BlockFace.DOWN) {
            if (blockFace == BlockFace.UP) {
                oppositeFace = oppositeFace.getOppositeFace();
            }
            for (final Direction direction : values()) {
                if (direction.getBlockFace() == blockFace && BlockFace.DOWN == oppositeFace) {
                    return direction;
                }
            }
        }
        else {
            for (final Direction direction2 : values()) {
                if (direction2.getBlockFace() == blockFace) {
                    return direction2;
                }
            }
        }
        return null;
    }

    public static Vector vectorize(final BlockFace blockFace) {
        return new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
    }

    public Vector getNormalVector() {
        return vectorize(this.blockFace);
    }

    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    public Vector getXDirection() {
        return vectorize(this.xDirection);
    }

    public BlockFace getXDirectionFace() {
        return this.xDirection;
    }
}