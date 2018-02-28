package github.saukiya.angelcardstats.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import github.saukiya.angelcardstats.data.PlayerData;

/*本类由 Saukiya 在 2018年2月27日 下午12:39:58 时创建
 *TIM:admin@Saukiya.cn
 *GitHub:https://github.com/Saukiya
**/

public class PlayerQuitServerEvent implements Listener {

	@EventHandler
	void onPlayerQuitEvent(PlayerQuitEvent event){
		PlayerData.removePlayerData(event.getPlayer().getName());
	}
}
