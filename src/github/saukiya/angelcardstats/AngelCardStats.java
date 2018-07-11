package github.saukiya.angelcardstats;

import github.saukiya.angelcardstats.data.*;
import github.saukiya.angelcardstats.event.OnDamageEvent;
import github.saukiya.angelcardstats.event.OnInventoryEvent;
import github.saukiya.angelcardstats.event.OnPlayerJoinOrQuitServerEvent;
import github.saukiya.angelcardstats.inventory.CardInventory;
import github.saukiya.angelcardstats.inventory.StatsInventory;
import github.saukiya.angelcardstats.util.Config;
import github.saukiya.angelcardstats.util.Message;
import github.saukiya.angelcardstats.util.Placeholders;
import github.saukiya.angelcardstats.util.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/*本类由 Saukiya 在 2018年2月26日 下午10:41:50 时创建
 *TIM:admin@Saukiya.cn
 *GitHub:https://github.com/Saukiya
 **/

public class AngelCardStats extends JavaPlugin implements Listener {
    static public Plugin getPlugin() {
        return AngelCardStats.getPlugin(AngelCardStats.class);
    }

    public void onEnable() {
        Long oldTimes = System.currentTimeMillis();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new OnDamageEvent(), this);
        Bukkit.getPluginManager().registerEvents(new OnInventoryEvent(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerJoinOrQuitServerEvent(), this);
        Config.loadConfig();
        Message.loadMessage();
        ItemData.loadItemData();
        CardValueData.loadData();
        SuitData.loadSuitData();
        StatsDataRead.suitLoreMap.clear();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholders(this).hook();
            Bukkit.getConsoleSender().sendMessage("[AngelCardStats] Find PlacholderAPI!");
        } else {
            Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §aNo Find PlacholderAPI!");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("AngelPoints")) {
            Bukkit.getConsoleSender().sendMessage("[AngelCardStats] Find AngelPoints!");
        } else {
            Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §cNo Find AngelPoints!");
            Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §c请安装 AngelPoints!");
            this.setEnabled(false);
        }
//		Money.setupEconomy();
        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] 加载用时: §c" + (System.currentTimeMillis() - oldTimes) + "§7 毫秒");
        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §c加载成功! 插件作者: Saukiya 定制人员: §6Angel");
    }

    @Override
    public void onDisable() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            String invName = player.getOpenInventory().getTitle();
            if (invName.equals(Message.getMsg(Message.INVENTORY_OPEN_NAME))) {
                Inventory inv = player.getOpenInventory().getTopInventory();
                PlayerData.saveInventory(inv, player);
                player.closeInventory();
            } else if (invName.equals(Message.getMsg(Message.INVENTORY_STATS_NAME))) {
                player.closeInventory();
            } else if (invName.equals(Message.getMsg(Message.INVENTORY_SUIT_LIST_NAME))) {
                player.closeInventory();
            } else if (invName.equals(Message.getMsg(Message.INVENTORY_SUIT_NAME))) {
                player.closeInventory();
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {
        if (label.equalsIgnoreCase("acs") || label.equalsIgnoreCase(this.getName())) {
            //判断是否是玩家
            if ((sender instanceof Player)) {
                //判断是否有权限
                if (!sender.hasPermission(this.getName() + ".use")) {
                    sender.sendMessage(Message.getMsg(Message.ADMIN_NO_PER_CMD));
                    return true;
                }
                //判断玩家是否有这个权限

            }
            //无参数
            if (args.length == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6==========[&c " + this.getName() + "&6 ]=========="));
                for (java.lang.reflect.Method method : this.getClass().getDeclaredMethods()) {
                    if (!method.isAnnotationPresent(PlayerCommand.class)) {
                        continue;
                    }
                    PlayerCommand sub = method.getAnnotation(PlayerCommand.class);
                    if (!(sender instanceof Player)) if ("stats save info open".contains(sub.cmd())) continue;
                    if (sender.hasPermission(this.getName() + "." + sub.cmd())) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/" + label + " " + sub.cmd() + "&6" + sub.arg() + "&7-:&b " + Message.getMsg("Command." + sub.cmd())));
                    }
                }
                return true;
            }
            if (sender instanceof Player) {
                if (!sender.hasPermission(this.getName() + "." + args[0])) {
                    sender.sendMessage(Message.getMsg(Message.ADMIN_NO_PER_CMD));
                    return true;
                }
            }
            for (java.lang.reflect.Method method : this.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(PlayerCommand.class)) {
                    continue;
                }
                PlayerCommand sub = method.getAnnotation(PlayerCommand.class);
                if (!sub.cmd().equalsIgnoreCase(args[0])) {
                    continue;
                }

                try {
                    method.invoke(getPlugin(), sender, args);
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

    @PlayerCommand(cmd = "open")
    void onOpenCommand(CommandSender sender, String args[]) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 1) {
                if (player.hasPermission(this.getName() + ".admin")) {
                    CardInventory.openCardInventory(player, 1, args[1]);
                    return;
                }
            }
            CardInventory.openCardInventory(player, 1);
        } else {
            sender.sendMessage(Message.getMsg(Message.ADMIN_NO_CONSOLE));
        }
    }

    //给予指令执行方法
    @SuppressWarnings("deprecation")
    @PlayerCommand(cmd = "give", arg = " <物品编名> [玩家] [数量]")
    void onGiveCommand(CommandSender sender, String args[]) {
        if (args.length < 2) {
            ItemData.sendItemMapToPlayer(sender, 1);
            return;
        }
        Player player = null;
        ItemStack item = ItemData.getItem(args[1]);
        int amount = 1;
        if (item == null) {
            sender.sendMessage(Message.getMsg(Message.ADMIN_NO_ITEM));
            ItemData.sendItemMapToPlayer(sender, 1, args[1]);
            return;
        }
        if (args.length >= 3) {
            String playerName = args[2];
            //判断 args[2] 是否为玩家
            if (Bukkit.getOfflinePlayer(playerName).isOnline()) {
                player = Bukkit.getPlayer(playerName);
            } else {
                if (sender instanceof Player) {
                    player = (Player) sender;
                } else {
                    sender.sendMessage(Message.getMsg(Message.ADMIN_NO_CONSOLE));
                    return;
                }
                //判断 args[2] 是否为数量
                if (Pattern.compile("[0-9]*").matcher(playerName).matches()) {
                    amount = Integer.valueOf(playerName);
                    if (amount >= 10000) {
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
            if (amount == 1 && args.length >= 4) {
                if (Pattern.compile("[0-9]*").matcher(args[3]).matches()) {
                    amount = Integer.valueOf(args[3]);
                }
            }
        } else {
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage(Message.getMsg(Message.ADMIN_NO_CONSOLE));
                return;
            }
        }
        for (int i = 0; i < amount; i++) {
            player.getInventory().addItem(item);
        }
        sender.sendMessage(Message.getMsg(Message.ADMIN_GIVE_ITEM, player.getName(), String.valueOf(amount), args[1]));
    }

    //保存指令执行方法
    @SuppressWarnings("deprecation")
    @PlayerCommand(cmd = "save", arg = " <物品编名>")
    void onSaveCommand(CommandSender sender, String args[]) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Message.getMsg(Message.ADMIN_NO_CONSOLE));
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(Message.getMsg(Message.ADMIN_NO_FORMAT));
            return;
        }
        String itemName = args[1];
        Player player = (Player) sender;
        //TODO 根据版本控制获取的物品
        ItemStack item = player.getEquipment().getItemInHand();
        if (item.getType().toString().contains("AIR")) {
            player.sendMessage(Message.getMsg(Message.ADMIN_NO_ITEM));
            return;
        }
        if (ItemData.isItem(itemName)) {
            player.sendMessage(Message.getMsg(Message.ADMIN_HAS_ITEM, itemName));
            return;
        }
        ItemData.saveItem(itemName, item);
        sender.sendMessage("§d[AngelCardStats] §c物品已经保存, 名称为: §a" + itemName);
    }

    @SuppressWarnings("deprecation")
    @PlayerCommand(cmd = "info")
    void onInfoCommand(CommandSender sender, String args[]) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            //TODO 根据版本控制获取的物品
            ItemStack item = player.getEquipment().getItemInHand();
            String itemName = item.getType().name();
            if (item.getType().toString().contains("AIR")) {
                player.sendMessage(Message.getMsg(Message.ADMIN_NO_ITEM));
                return;
            }
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                itemName = item.getItemMeta().getDisplayName();
            }
            player.sendMessage(Message.getMsg(Message.ADMIN_INFO_ITEM, itemName));
            List<ItemStack> itemList = new ArrayList<ItemStack>();
            itemList.add(item);
            StatsData data = StatsDataRead.getItemStatsData(null, itemList);
            sendStatsMessage(player, Config.NAME_DAMAGE, data.getDamage(), false);
            sendStatsMessage(player, Config.NAME_HEALTH, data.getHealth(), false);
            sendStatsMessage(player, Config.NAME_DEFENSE, data.getDefense(), false);
            sendStatsMessage(player, Config.NAME_SPEED, data.getSpeed(), true);
            sendStatsMessage(player, Config.NAME_CRIT_DAMAGE, data.getCritDamage(), true);
            sendStatsMessage(player, Config.NAME_PENETRATION, data.getPenetration(), false);
            sendStatsMessage(player, Config.NAME_LIFE_STEAL, data.getLifeSteal(), true);
            sendStatsMessage(player, Config.NAME_CRIT, data.getCrit(), true);
            sendStatsMessage(player, Config.NAME_IGNITION, data.getIgnition(), true);
            sendStatsMessage(player, Config.NAME_WITHER, data.getWither(), true);
            sendStatsMessage(player, Config.NAME_POISON, data.getPoison(), true);
            sendStatsMessage(player, Config.NAME_BLINDNESS, data.getBlindness(), true);
            sendStatsMessage(player, Config.NAME_SLOWNESS, data.getSlowness(), true);
            sendStatsMessage(player, Config.NAME_LIGHTNING, data.getLightning(), true);
            sendStatsMessage(player, Config.NAME_REAL, data.getReal(), true);
            sendStatsMessage(player, Config.NAME_TEARING, data.getTearing(), true);
            sendStatsMessage(player, Config.NAME_REFLECTION, data.getReflection(), true);
            sendStatsMessage(player, Config.NAME_BLOCK, data.getBlock(), true);
            sendStatsMessage(player, Config.NAME_DODGE, data.getDodge(), true);
            sendStatsMessage(player, Config.NAME_CARD_VALUE, Double.valueOf(data.getCardValue()), false);
        } else {
            sender.sendMessage(Message.getMsg(Message.ADMIN_NO_CONSOLE));
            return;
        }
    }

    void sendStatsMessage(Player player, String loc, Double d, Boolean percentage) {
        String message = "§b" + Config.getString(loc) + ": +" + d;
        if (percentage) message += "%";
        if (d > 0) player.sendMessage(message);
    }

    @PlayerCommand(cmd = "stats")
    void onSetCommand(CommandSender sender, String args[]) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            StatsInventory.openStatsInventory(player, 1, player.getName());
        } else {
            sender.sendMessage(Message.getMsg(Message.ADMIN_NO_CONSOLE));
            return;
        }
    }

    @PlayerCommand(cmd = "top", arg = " <页数>")
    void onTopCommand(CommandSender sender, String args[]) {
        int page = 1;
        if (args.length >= 2 && Pattern.compile("[0-9]*").matcher(args[1]).matches()) {
            page = Integer.valueOf(args[1]);
        }
        CardValueData.sendCardValueTop(sender, page);
    }

    @PlayerCommand(cmd = "reload", arg = "")
    void onReloadCommand(CommandSender sender, String args[]) {
        Long oldTimes = System.currentTimeMillis();
        Config.loadConfig();
        Message.loadMessage();
        PlayerData.loadData();
        ItemData.loadItemData();
        SuitData.loadSuitData();
        CardValueData.loadData();
        StatsDataRead.suitLoreMap.clear();
        sender.sendMessage("§d[AngelCardStats] §a加载用时:" + (System.currentTimeMillis() - oldTimes) + "毫秒");
        sender.sendMessage("§d[AngelCardStats] §a重载成功! 插件作者: §7Saukiya §a定制人员: §cAngel");
    }
}
