package github.saukiya.angelcardstats.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import github.saukiya.angelcardstats.data.HolderMeta;
import github.saukiya.angelcardstats.data.PlayerData;
import github.saukiya.angelcardstats.util.Config;
import github.saukiya.angelcardstats.util.Message;

/*本类由 Saukiya 在 2018年2月26日 下午9:27:00 时创建
 *TIM:admin@Saukiya.cn
 *GitHub:https://github.com/Saukiya
**/

public class InventoryManager {
	static public void openCardInventory(Player player,int page){//打开
		//get 玩家当前所拥有的卡牌槽数量：默认槽+购买槽(防止之后修改默认槽后出现的变数)
		int slot = Integer.valueOf(Config.getConfig(Config.PLAYER_DEFAULT_SLOT))+PlayerData.getBuySlot(player.getName());
		int maxPage = slot/36+1;//获得最大页数
		Inventory inv = Bukkit.createInventory(new HolderMeta(page), 54,Message.getMsg(Message.INVENTORY_NAME));
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE,1,(short) 15);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§r");
		item.setItemMeta(meta);
		for(int i=0;i<9;i++){
			inv.setItem(i, item);
		}
		for(int i=45;i<54;i++){
			inv.setItem(i, item);
		}
		//TODO 卡牌属性查看
		item.setType(Material.SKULL_ITEM);
		item.setDurability((short) 3);
		ItemMeta skullMeta = item.getItemMeta();
		((SkullMeta) skullMeta).setOwner(player.getName());
		skullMeta.setDisplayName(Message.getMsg(Message.INVENTORY_SKULL_NAME));
		item.setItemMeta(skullMeta);
		inv.setItem(4, item);
		item.setDurability((short) 0);
		
		
		if(page<maxPage){//下一页
			item.setType(Material.ARROW);
			meta.setDisplayName(Message.getMsg(Message.INVENTORY_PAGE_DOWN));
			item.setItemMeta(meta);
			item.setAmount(page+1);
			inv.setItem(53, item);
		}
		
		if(page>1){//上一页
			item.setType(Material.ARROW);
			meta.setDisplayName(Message.getMsg(Message.INVENTORY_PAGE_UP));
			item.setItemMeta(meta);
			item.setAmount(page-1);
			inv.setItem(45, item);
		}
		item.setAmount(1);
		//TODO 购买卡槽按钮
		
		//TODO 出售卡牌按钮
		
		//TODO 套装按钮
		//根据 slot 锁槽
		slot = slot-((page-1)*36);//现在的slot值为24
		item.setType(Material.BARRIER);
		meta.setDisplayName(Message.getMsg(Message.INVENTORY_LOCK_SLOT));
		item.setItemMeta(meta);
		for(int i=9;i<45;i++){
			if(i-slot-9>=0){
				inv.setItem(i, item);
			}
		}
		
		
		
		player.openInventory(inv);
	}

}
