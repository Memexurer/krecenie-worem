package pl.memexurer.krecenie;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import pl.memexurer.krecenie.animated.AnimatedImageDisplayTask;
import pl.memexurer.krecenie.animated.AnimatedImageParser;
import pl.memexurer.krecenie.animation.DelegatedFrameProvider;
import pl.memexurer.krecenie.animation.GifFrameProvider;
import pl.memexurer.krecenie.animation.function.FunctionFrameProviderWrapper;
import pl.memexurer.krecenie.config.MapItem;
import pl.memexurer.krecenie.config.PluginConfiguration;
import pl.memexurer.krecenie.function.CoolTransformation;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class KrecenieWoremPlugin extends JavaPlugin implements Listener {
    private final PluginConfiguration configuration = (PluginConfiguration) ConfigManager.create(PluginConfiguration.class)
            .withConfigurer(new YamlBukkitConfigurer())
            .withBindFile(new File(getDataFolder(), "config.yml"))
            .saveDefaults()
            .load(true);

    private final Set<UUID> currentEditing = new HashSet<>();

    private static BlockFace getDirectionFromYaw(final float n) {
        double n2 = (n - 90.0f) % 360.0f;
        if (n2 < 0.0) {
            n2 += 360.0;
        }
        if (0.0 <= n2 && n2 < 45.0) {
            return BlockFace.WEST;
        }
        if (45.0 <= n2 && n2 < 135.0) {
            return BlockFace.NORTH;
        }
        if (135.0 <= n2 && n2 < 225.0) {
            return BlockFace.EAST;
        }
        if (225.0 <= n2 && n2 < 315.0) {
            return BlockFace.SOUTH;
        }
        return BlockFace.WEST;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    private void onRightClick(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame)) return;
        if (!currentEditing.contains(event.getPlayer().getUniqueId())) return;

        if (configuration.itemFrames.size() == 0) {
            configuration.direction = Direction.fromBlockFace(((ItemFrame) event.getRightClicked()).getFacing(), getDirectionFromYaw(event.getPlayer().getLocation().getYaw()));
        }
        configuration.itemFrames.add(event.getRightClicked().getUniqueId());
        configuration.save();
        event.getPlayer().sendMessage(ChatColor.AQUA + "Dodano itemframe: " + configuration.itemFrames.size() + "/25");
    }

    //todo prerendering
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (label.equals("setupmap")) {
            UUID senderId = player.getUniqueId();
            if (currentEditing.contains(senderId)) {
                currentEditing.remove(senderId);
                sender.sendMessage(ChatColor.RED + "Wylaczyles tryb edytowania.");
            } else {
                currentEditing.add(senderId);
                sender.sendMessage(ChatColor.GREEN + "Wlaczyles tryb edytowania!");
            }
        } else {
            List<ItemFrame> entityList = player.getWorld().getEntities().stream()
                    .filter(entity -> configuration.itemFrames.contains(entity.getUniqueId()))
                    .map(entity -> (ItemFrame) entity)
                    .collect(Collectors.toList());

            WallMapDisplay mapDisplay = new WallMapDisplay(entityList, entityList.stream().filter(
                    frame -> frame.getUniqueId().equals(configuration.itemFrames.get(0))
            ).findFirst().map(ItemFrame::getLocation).map(Location::toVector).orElseThrow(), configuration.direction);
            try {
                GifFrameProvider frameProvider = new GifFrameProvider(AnimatedImageParser.readAnimatedImage(new File(getDataFolder(), "maps/loop.map")));
                MapItem randomItem = configuration.getRandomMapItem();
                getServer().getScheduler().runTaskTimer(this, new AnimatedImageDisplayTask<>(
                        mapDisplay,
                        player,
                        new DelegatedFrameProvider(
                                () -> {
                                    randomItem.execute(player);
                                }, new GifFrameProvider(AnimatedImageParser.readAnimatedImage(new File(getDataFolder(), "maps/start.map"))),
                                new FunctionFrameProviderWrapper(new CoolTransformation(configuration.getRandomItem(randomItem, frameProvider.getFrameCount())), frameProvider)
                        )), 0L, 1L);
            } catch (IOException e) {
                e.printStackTrace();
                sender.sendMessage("error kurwo jebana!!");
            }
        }
        return true;
    }
}
