package github.saukiya.angelcardstats.data;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/*本类由 Saukiya 在 2018年2月27日 下午10:29:30 时创建
 *TIM:admin@Saukiya.cn
 *GitHub:https://github.com/Saukiya
**/

public class HolderMeta  implements InventoryHolder{
	private final int page;
	//缺点：在用yum重载的过程中，已经被打开的GUI是无法被监听到HolderMeta
	public HolderMeta(int page){
		this.page = page;
	}
	public int getPage() {
		return page;
	}
	@Override
	public Inventory getInventory() {
		return null;
	}


}
