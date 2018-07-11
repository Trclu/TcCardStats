package github.saukiya.angelcardstats.util;

import github.saukiya.angelcardstats.AngelCardStats;
import github.saukiya.angelcardstats.api.AngelCardStatsAPI;
import github.saukiya.angelcardstats.data.StatsData;
import github.saukiya.angelcardstats.data.StatsDataRead;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

public class Placeholders extends EZPlaceholderHook {

    @SuppressWarnings("unused")
    private AngelCardStats ourPlugin;

    public Placeholders(AngelCardStats ourPlugin) {
        super(ourPlugin, "acs");
        this.ourPlugin = ourPlugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String str) {
        StatsData data = StatsDataRead.getPlayerData(player.getName());
        if (str.equalsIgnoreCase("damage")) {
            return String.valueOf(data.getDamage());
        }
        if (str.equalsIgnoreCase("health")) {
            return String.valueOf(data.getHealth());
        }
        if (str.equalsIgnoreCase("defense")) {
            return String.valueOf(data.getDefense());
        }
        if (str.equalsIgnoreCase("speed")) {
            return String.valueOf(data.getSpeed());
        }
        if (str.equalsIgnoreCase("critDamage")) {
            return String.valueOf(data.getCritDamage());
        }
        if (str.equalsIgnoreCase("penetration")) {
            return String.valueOf(data.getPenetration());
        }
        if (str.equalsIgnoreCase("lifeSteal")) {
            return String.valueOf(data.getLifeSteal());
        }
        if (str.equalsIgnoreCase("crit")) {
            return String.valueOf(data.getCrit());
        }
        if (str.equalsIgnoreCase("ignition")) {
            return String.valueOf(data.getIgnition());
        }
        if (str.equalsIgnoreCase("wither")) {
            return String.valueOf(data.getWither());
        }
        if (str.equalsIgnoreCase("poison")) {
            return String.valueOf(data.getPoison());
        }
        if (str.equalsIgnoreCase("blindness")) {
            return String.valueOf(data.getBlindness());
        }
        if (str.equalsIgnoreCase("slowness")) {
            return String.valueOf(data.getSlowness());
        }
        if (str.equalsIgnoreCase("lightning")) {
            return String.valueOf(data.getLightning());
        }
        if (str.equalsIgnoreCase("real")) {
            return String.valueOf(data.getReal());
        }
        if (str.equalsIgnoreCase("tearing")) {
            return String.valueOf(data.getTearing());
        }
        if (str.equalsIgnoreCase("reflection")) {
            return String.valueOf(data.getReflection());
        }
        if (str.equalsIgnoreCase("block")) {
            return String.valueOf(data.getBlock());
        }
        if (str.equalsIgnoreCase("dodge")) {
            return String.valueOf(data.getDodge());
        }
        if (str.equalsIgnoreCase("cardValue")) {
            return String.valueOf(data.getCardValue());
        }
        if (str.contains("suit_")) {
            String suitName = str.split("_")[1];
            if (AngelCardStatsAPI.isSuit(player.getName(), suitName)) {
                return Message.getMsg(Message.PLACEHOLDER_SUIT_ON);
            } else {
                return Message.getMsg(Message.PLACEHOLDER_SUIT_OFF);
            }
        }
        //TODO 欢迎来到变量王国
        return "§c请核对你的变量是否正确!";
    }

}
