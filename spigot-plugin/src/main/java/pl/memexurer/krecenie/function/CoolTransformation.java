package pl.memexurer.krecenie.function;

import org.bukkit.Bukkit;

public class CoolTransformation implements Transformation {
    private final int endAt;

    public CoolTransformation(int endAt) {
        this.endAt = endAt;
    }

    @Override
    public Integer applyInt(Integer frame, Integer max) {
     ////   Bukkit.broadcastMessage("Frame: " + ((double) frame / max));
    //    Bukkit.broadcastMessage("End at: " + endAt);
        return (int) (((double) frame / max) * endAt);
    }
}
