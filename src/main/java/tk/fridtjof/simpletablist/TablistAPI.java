package tk.fridtjof.simpletablist;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.tab.TabList;
import org.spongepowered.api.text.Text;

public class TablistAPI {

    static SimpleTablist plugin = SimpleTablist.getInstance();

    public static void sendTablistToAllPlayers() {
        for(Player players : Sponge.getServer().getOnlinePlayers()) {
            sendTablist(players);
        }
    }

    public static void sendTablist(Player player) {
        Text header = plugin.configManager.getHeader();
        Text footer = plugin.configManager.getFooter();
        sendTablist(player, header, footer);
    }

    public static void sendTablist(Player player, Text header, Text footer) {
        TabList tablist = player.getTabList();

        if (plugin.PH) {
            header = plugin.getPlaceholderService().get().replacePlaceholders(header, player, player);
            footer = plugin.getPlaceholderService().get().replacePlaceholders(footer, player, player);
        }

        tablist.setHeader(header);
        tablist.setFooter(footer);
    }
}
