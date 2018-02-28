package github.saukiya.angelcardstats.event;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import github.saukiya.angelcardstats.AngelCardStats;
import github.saukiya.angelcardstats.data.HolderMeta;
import github.saukiya.angelcardstats.data.ItemData;
import github.saukiya.angelcardstats.data.PlayerData;
import github.saukiya.angelcardstats.listener.InventoryManager;
import github.saukiya.angelcardstats.util.Message;

public class InventoryEvent implements Listener {
	
	@EventHandler
	void onInventoryClickEvent(InventoryClickEvent event){
		Inventory inv = event.getInventory();
		Player player = (Player) event.getView().getPlayer();
		int slot = event.getRawSlot();
		System.out.println(slot);
		if(inv.getName().equals(Message.getMsg(Message.INVENTORY_NAME))){
			if(!event.getClick().equals(ClickType.LEFT)){
				event.setCancelled(true);
				return;
			}
			int page = ((HolderMeta) inv.getHolder()).getPage();
			ItemStack item = event.getCurrentItem();
			if(item == null)return;
			
			if(slot <=53){
				if(slot>9&&slot<45){
					//TODO 检测是否点击的是锁槽
				}else{
					event.setCancelled(true);
					if(item.getType().equals(Material.ARROW)){
						InventoryManager.openCardInventory(player, item.getAmount());
					}
				}
			}else{
				//TODO 判断在玩家背包中是否点击的是卡牌。
				if(item.getAmount()!=0){
					if(!ItemData.isCard(item)){
						player.sendMessage(Message.getMsg(Message.PLAYER_IS_NOT_CARD_ITEM));
						event.setCancelled(true);
					}else{
						//TODO 判定手持数量是否大于1
							if(item.getAmount()>1){
								int amount = item.getAmount()-1;
//								
								Inventory playerInv = player.getInventory();
								ItemStack itemClone = item.clone();
								itemClone.setAmount(amount);
								for(int i=0;i<36;i++){
									ItemStack iItem = playerInv.getItem(i);
									if(iItem==null){
										item.setAmount(1);
										playerInv.setItem(i, itemClone);
										player.updateInventory();
										return;
									}
//									if(iItem.getItemMeta().equals(item.getItemMeta())){
//										item.setAmount(1);
//										itemClone.setAmount(itemClone.getAmount()+iItem.getAmount());
//										playerInv.setItem(i, itemClone);
//										player.updateInventory();
//										return;
//									}
								}
								event.setCancelled(true);
							}
					}
				}
			}
		}
	}

	@EventHandler
	void onInventoryCloseEvent(InventoryCloseEvent event){
		Inventory inv = event.getInventory();
		Player player = (Player) event.getView().getPlayer();
		PlayerData.saveInventory(inv,player);
	}
	

}
