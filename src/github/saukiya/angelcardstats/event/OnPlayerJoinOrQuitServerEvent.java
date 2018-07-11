package github.saukiya.angelcardstats.event;

import github.saukiya.angelcardstats.data.PlayerData;
import github.saukiya.angelcardstats.data.StatsDataRead;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/*本类由 Saukiya 在 2018年2月27日 下午12:39:58 时创建
 *TIM:admin@Saukiya.cn
 *GitHub:https://github.com/Saukiya
 **/

public class OnPlayerJoinOrQuitServerEvent implements Listener {

    @EventHandler
    void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        int level = player.getLevel();
        PlayerData.removePlayerDataAndSaveLevel(player.getName(), level);
        StatsDataRead.clearPlayerData(player);
    }

    @EventHandler
    void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        StatsDataRead.loadPlayerStats(player);
    }
}
