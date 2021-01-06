package tk.fridtjof.simpletablist;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Path;

public class ConfigManager {

    private SimpleTablist plugin;
    private Logger logger;
    private ConfigurationNode configurationNode;
    private Path path;
    private ConfigurationLoader configurationLoader;

    public ConfigManager(SimpleTablist instance) {
        this.plugin = instance;
        this.logger = instance.logger;
        this.configurationNode = instance.configurationNode;
        this.path = instance.path;
        this.configurationLoader = instance.configurationLoader;
        reload();
    }

    private Text header;
    private Text footer;

    public void reload() {
        try {
            configurationNode = configurationLoader.load();

            if (!path.toFile().exists()) {
                logger.info("Creating config!");
                configurationNode.getNode("tablist", "text", "header").setValue("§bWelcome %player% - §6This is the tablist §cheader");
                configurationNode.getNode("tablist", "text", "footer").setValue("§6This is the tablist §cfooter");
                configurationLoader.save(configurationNode);

                header = Text.of(plugin.configManager.getHeader());
                footer = Text.of(plugin.configManager.getFooter());
            }
        } catch (IOException e) {
            logger.error("Error creating the default configuration!");
        }
    }

    public Text getHeader() {
        return header;
    }

    public Text getFooter() {
        return footer;
    }
}
