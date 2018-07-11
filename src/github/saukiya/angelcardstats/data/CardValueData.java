package github.saukiya.angelcardstats.data;

import github.saukiya.angelcardstats.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class CardValueData {
    final public static String CardValue_LIST = "CardValueList";
    public static YamlConfiguration data;
    static File dataFile = new File("plugins" + File.separator + "AngelCardStats" + File.separator + "Data.dat");
    static HashMap<String, Integer> CardValueMap = new HashMap<String, Integer>();

    static public void loadData() {
        CardValueMap.clear();
        //检测data.dat是否存在
        if (!dataFile.exists()) {
            Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §cCreate Data.dat");
            data = new YamlConfiguration();
            List<String> CardValueList = new ArrayList<String>();
            data.set(CardValue_LIST, CardValueList);
            try {
                data.save(dataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §aFind Data.dat");
        }
        data = new YamlConfiguration();
        try {
            data.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §c读取data时发生错误");
        }

        List<String> CardValueList = getCardValueList();
//		if(CardValueList.size()>0){
        for (int i = 0; i < CardValueList.size(); i++) {
            String str = CardValueList.get(i);
            CardValueMap.put(str.split(":")[0], Integer.valueOf(str.split(":")[1]));
        }
//		}
    }

    @SuppressWarnings("unchecked")
    static public List<String> getCardValueList() {
        return data.getStringList(CardValue_LIST);
    }

    static public void saveCardValueList(List<String> CardValueList) {
        data.set(CardValue_LIST, CardValueList);
        try {
            data.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public Boolean isPlayerCardValue(String playerName, int CardValue) {
        return getPlayerCardValue(playerName) >= CardValue;
    }

    static public int getPlayerCardValue(String playerName) {
        List<String> CardValueList = getCardValueList();
        int CardValue = 0;
        if (CardValueMap.containsKey(playerName)) {
            return CardValueMap.get(playerName);
        }
        for (int i = 0; i < CardValueList.size(); i++) {
            String str = CardValueList.get(i);
            if (str.split(":")[0].equalsIgnoreCase(playerName)) {
                CardValue = Integer.valueOf(str.split(":")[1]);
            }
        }
        return CardValue;
    }


    static public void setPlayerCardValue(String playerName, int setCardValue) {
        List<String> CardValueList = getCardValueList();
        for (int i = 0; i < CardValueList.size(); i++) {
            String str = CardValueList.get(i);
            if (str.split(":")[0].equalsIgnoreCase(playerName)) {
                CardValueList.set(i, playerName + ":" + setCardValue);
                saveCardValueList(CardValueList);
                CardValueMap.put(playerName, setCardValue);
                if (!str.split(":")[0].equals(playerName)) CardValueMap.remove(str.split(":")[0]);
                return;
            }
        }
        CardValueList.add(playerName + ":" + setCardValue);
        CardValueMap.put(playerName, setCardValue);
        saveCardValueList(CardValueList);
    }

    static public void sendCardValueTop(CommandSender player, int page) {
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(CardValueMap.entrySet());
        if (list.size() > 1) {
            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });
        }
        String name;
        int CardValue;
//		int value = 0;
//		int valuePage = 0;
        int maxPage = 0;
        player.sendMessage(Message.getMsg(Message.PLAYER_CARD_VALUE_TOP_TITLE));
        for (int i = (page - 1) * 10; i < list.size(); i++) {
            Map.Entry<String, Integer> mapList = list.get(i);
//		}
//        for (Map.Entry<String, Integer> mapList : list) {
//        	valuePage++;
//        	value++;
//        	if(valuePage<=(page-1)*10)continue;
            if (i >= (page) * 10) break;
            name = mapList.getKey();
            CardValue = mapList.getValue();
            if (CardValue == 0) continue;
            player.sendMessage(Message.getMsg(Message.PLAYER_CARD_VALUE_TOP, String.valueOf(i + 1), name, String.valueOf(CardValue)));
        }
        maxPage = list.size() / 10;
        if (list.size() % 10 != 0) maxPage++;
        player.sendMessage(Message.getMsg(Message.PLAYER_CARD_VALUE_TOP_END, String.valueOf(page), String.valueOf(maxPage)));
    }

}
