package github.saukiya.angelcardstats;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import github.saukiya.angelcardstats.util.PlayerCommand;
import github.saukiya.angelcardstats.data.ItemData;
import github.saukiya.angelcardstats.data.PlayerData;
import github.saukiya.angelcardstats.data.StatsDataRead;
import github.saukiya.angelcardstats.event.InventoryEvent;
import github.saukiya.angelcardstats.event.PlayerQuitServerEvent;
import github.saukiya.angelcardstats.listener.InventoryManager;
import github.saukiya.angelcardstats.util.Config;
import github.saukiya.angelcardstats.util.Message;
import github.saukiya.angelcardstats.util.Money;

/*本类由 Saukiya 在 2018年1月5日 下午10:41:50 时创建
 *TIM:admin@Saukiya.cn
 *GitHub:https://github.com/Saukiya
**/

public class AngelCardStats extends JavaPlugin implements Listener{
	public void onEnable()
	{
		Long oldTimes = System.currentTimeMillis();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new InventoryEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitServerEvent(), this);
        Config.loadConfig();
        PlayerData.loadData();
		Message.loadMessage();
		ItemData.loadItemData();
//		Money.setupEconomy();
		Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §a加载用时:"+(System.currentTimeMillis()-oldTimes)+"毫秒");
        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §a加载成功! 插件作者: Saukiya 定制人员: §6Angel");
	}
	@Override
	public void onDisable(){
		if(Bukkit.getOnlinePlayers().size()==0)return;
        for(Player player:Bukkit.getOnlinePlayers()){
        	player.closeInventory();
        }
	}
	static public Plugin getPlugin(){
		return AngelCardStats.getPlugin(AngelCardStats.class);
	}
	public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {
        if(label.equalsIgnoreCase("acs") || label.equalsIgnoreCase(this.getName())){
                //判断是否是玩家
                if((sender instanceof Player)){
                    //判断是否有权限
                    if(!sender.hasPermission(this.getName()+ ".use")){
        				sender.sendMessage(Message.getMsg(Message.ADMIN_NO_PER_CMD));
                        return true;
                    }
                }
                //无参数
                if(args.length==0){
                	sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6==========[&c "+ this.getName() +"&6 ]=========="));
                        for(java.lang.reflect.Method method : this.getClass().getDeclaredMethods()){
                                if(!method.isAnnotationPresent(PlayerCommand.class)){
                                        continue;
                                }
                                PlayerCommand sub=method.getAnnotation(PlayerCommand.class);
                                if(sender.hasPermission(this.getName()+"." + sub.cmd())){
                                	sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/"+ label + " " +sub.cmd()+"&6"+sub.arg()+"&7-:&b "+Message.getMsg("Command."+ sub.cmd())));
                                }
                        }
                        return true;
                }
                for(java.lang.reflect.Method method:this.getClass().getDeclaredMethods()){
                        if(!method.isAnnotationPresent(PlayerCommand.class)){
                                continue;
                        }
                        PlayerCommand sub=method.getAnnotation(PlayerCommand.class);
                        if(!sub.cmd().equalsIgnoreCase(args[0])){
                                continue;
                        }
                        //判断玩家是否有这个权限
                		if(sender instanceof Player){
                			if(!sender.hasPermission(this.getName()+ "." + args[0])) {
                				sender.sendMessage(Message.getMsg(Message.ADMIN_NO_PER_CMD));
                	            return true;
                			}
                		}
                        try {
                                method.invoke(this, sender,args);
                        } catch (IllegalAccessException e) {
                                e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                        } catch (InvocationTargetException e) {
                                e.printStackTrace();
                        }
                        return true;
                }
                sender.sendMessage(Message.getMsg(Message.ADMIN_NO_CMD, args[0]));
            return true;
        }
        return false;
	}

	@PlayerCommand(cmd="open")
	void onOpenCommand(CommandSender sender,String args[]){
		if(sender instanceof Player){
			//TODO 打开菜单
			Player player = (Player) sender;
			InventoryManager.openCardInventory(player, 1);
		}else{
			sender.sendMessage(Message.getMsg(Message.ADMIN_NO_CONSOLE));
		}
	}
	
	//TODO 给予指令执行方法
	@SuppressWarnings("deprecation")
	@PlayerCommand(cmd="give",arg=" <物品编名> [玩家] [数量]")
	public void onGiveCommand(CommandSender sender,String args[]){
		if (args.length < 2) {
			ItemData.sendItemMapToPlayer(sender, 1);
			return;
		}
		Player player = null;
		ItemStack item = ItemData.getItem(args[1]);
		int amount = 1;
		if(item == null) {
			sender.sendMessage(Message.getMsg(Message.ADMIN_NO_ITEM));
			return;
		}
		if(args.length >=3) {
			String playerName = args[2];
			//判断 args[2] 是否为玩家
			if(Bukkit.getOfflinePlayer(playerName).isOnline()) {
				player = Bukkit.getPlayer(playerName);
			}else {
				if(sender instanceof Player){
					player = (Player) sender;
				}else{
					sender.sendMessage(Message.getMsg(Message.ADMIN_NO_CONSOLE));
					return;
				}
				//判断 args[2] 是否为数量
				if(Pattern.compile("[0-9]*").matcher(playerName).matches()) {
					amount = Integer.valueOf(playerName);
					if(amount >= 10000){
						sender.sendMessage(Message.getMsg(Message.ADMIN_NO_FORMAT));
						return;
					}
				}
				//否则返回 玩家不在线
				else {
					sender.sendMessage(Message.getMsg(Message.ADMIN_NO_ONLINE));
					return;
				}
			}
			if(amount == 1 && args.length >= 4) {
				if(Pattern.compile("[0-9]*").matcher(args[3]).matches()) {
					amount = Integer.valueOf(args[3]);
				}
			}
		}else{
			if(sender instanceof Player){
				player = (Player) sender;
			}else{
				sender.sendMessage(Message.getMsg(Message.ADMIN_NO_CONSOLE));
				return;
			}
		}
		for(int i=0;i<amount;i++) {
			player.getInventory().addItem(item);
		}
		sender.sendMessage(Message.getMsg(Message.ADMIN_GIVE_ITEM, player.getName(),String.valueOf(amount),args[1]));
	}
	
	//TODO 保存指令执行方法
	@PlayerCommand(cmd="save",arg=" <物品编名>")
	public void onSaveCommand(CommandSender sender,String args[]){
		if (args.length < 2) {
			sender.sendMessage(Message.getMsg(Message.ADMIN_NO_FORMAT));
			return;
		}
		String itemName = args[1];
		Player player = (Player) sender;
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if(itemStack.getType().toString().contains("AIR")) {
			player.sendMessage(Message.getMsg(Message.ADMIN_NO_ITEM));
			return;
		}
		if(ItemData.isItem(itemName)) {
			player.sendMessage(Message.getMsg(Message.ADMIN_HAS_ITEM,itemName));
			return;
		}
		ItemData.saveItem(itemName, itemStack);
	}
	@PlayerCommand(cmd="set")
	void onSetCommand(CommandSender sender,String args[]){
		if(sender instanceof Player){
			Player player = (Player) sender;
			ItemStack item = player.getEquipment().getItemInMainHand();
			player.getEquipment().setItemInMainHand(ItemData.setCard(item,null));//替换

		}
	}
	@PlayerCommand(cmd="isCard")
	void onIsCardCommand(CommandSender sender,String args[]){
		if(sender instanceof Player){
			Player player = (Player) sender;
			ItemStack item = player.getEquipment().getItemInMainHand();
			if(ItemData.isCard(item)){
				sender.sendMessage("卡牌！");
			}else{
				sender.sendMessage("不是！");
			}

		}
	}
	
	@PlayerCommand(cmd="reload")
	void onReloadCommand(CommandSender sender,String args[]){
		Long oldTimes = System.currentTimeMillis();
        Config.loadConfig();
        PlayerData.loadData();
		Message.loadMessage();
		ItemData.loadItemData();
		sender.sendMessage("[AngelCardStats] §a重载用时:"+(System.currentTimeMillis()-oldTimes)+"毫秒");
		sender.sendMessage("[AngelCardStats] §a重载成功! 插件作者: §7Saukiya §a定制人员: §cAngel");
	}
	
}
