package tk.fridtjof.simpletablist;

import com.google.inject.Inject;
import me.rojo8399.placeholderapi.PlaceholderService;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@Plugin(
        id = "simpletablist",
        name = "Simpletablist",
        version = "1.2.3",
        description = "This plugin allows you to customize your tablist.",
        url = "https://www.fridtjof.tk/",
        authors = {
                "Fridtjof_DE"
        },
        dependencies = {@Dependency(id = "spongeapi", version = "7.2.0"), @Dependency(id = "placeholderapi", version = "4.4", optional = true)}
)
public class SimpleTablist {

    @Inject
    public Logger logger;

     //instancing
    private static SimpleTablist instance;

    public SimpleTablist() {
        instance = this;
    }

    public static SimpleTablist getInstance() {
        return instance;
    }

    public static String VERSION = "1.2.3";
    public boolean PH;

    //PlaceholderAPI
    private Optional<PlaceholderService> ph;
    public Optional<PlaceholderService> getPlaceholderService() {
        return ph;
    }
    private boolean loadPlaceholderService() {
        try {
            ph = Sponge.getServiceManager().provide(PlaceholderService.class);
            if (ph.isPresent()) {
                logger.info("PlaceholderAPI has been found, hooking in!");
                PH = true;
                return true;
            }
        } catch (NoClassDefFoundError ignored) {}
        logger.info("PlaceholderAPI was not found, continuing without!");
        PH = false;
        return false;
    }

    //bStats
    //private final Metrics2 metrics;
    //@Inject
    //public SimpleTablist(Metrics2.Factory metricsFactory) {
    //    int pluginId = 7529;
    //    metrics = metricsFactory.make(pluginId);
    //}

    @Inject
    @DefaultConfig(sharedRoot = false)
    public Path path;
    @Inject
    @DefaultConfig(sharedRoot = false)
    public ConfigurationLoader<CommentedConfigurationNode> configurationLoader;
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;
    public ConfigurationNode configurationNode;

    ConfigManager configManager;

    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) {
        configManager = new ConfigManager(instance);
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        Player player = event.getTargetEntity();
        refreshTablist(player);
    }

    public void refreshTablist(Player player) {
        int delay = 20;

        if (delay > 0) {
            Task.builder().execute(new Runnable() {
                @Override
                public void run() {
                    TablistAPI.sendTablist(player);
                    logger.info(player.getName());
                }
            }).delayTicks((long) delay).submit(instance);
        }
    }

    @Listener
    public void onPlayerDisconnect(ClientConnectionEvent.Disconnect event) {

    }

    @Listener
    public void reload(GameReloadEvent event) {
        logger.info("Reloading...");
        configManager.reload();
        TablistAPI.sendTablistToAllPlayers();
    }

    @Listener
    public void onGameStartedServer(GameStartedServerEvent event) {
        loadPlaceholderService();
        logger.info("Enabling...");

        //UpdateChecker
        if(UpdateChecker.updateAvailable()) {
            logger.warn("New update is available at https://ore.spongepowered.org/Fridtjof_DE/SimpleTablist");
        } else if(!UpdateChecker.updateAvailable()) {
            logger.info("No new version detected!");
        } else {
            logger.error("Could not check for update!");
        }
    }
}