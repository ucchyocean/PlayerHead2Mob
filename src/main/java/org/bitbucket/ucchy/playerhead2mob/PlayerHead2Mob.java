/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2014
 */
package org.bitbucket.ucchy.playerhead2mob;

import java.util.Calendar;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author ucchy
 */
public class PlayerHead2Mob extends JavaPlugin implements Listener {

    /** エイプリルフール限定で動作させるかどうか */
    private boolean aprilfoolTimer;
    /** オーナー名 */
    private String owner;

    /**
     *
     * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
     */
    @Override
    public void onEnable() {

        // イベントリスナーとして登録
        getServer().getPluginManager().registerEvents(this, this);

        // コンフィグの読み込み処理
        saveDefaultConfig();
        reloadConfig();
        aprilfoolTimer = getConfig().getBoolean("aprilfoolTimer", true);
        owner = getConfig().getString("owner", "");
    }

    @EventHandler
    public void onSpawnMob(CreatureSpawnEvent event) {

        // エイプリルフール限定の場合は、4月1日じゃなければ動作しない。
        if ( aprilfoolTimer ) {
            Calendar cal = Calendar.getInstance();
            if ( cal.get(Calendar.MONTH) != Calendar.APRIL ||
                    cal.get(Calendar.DAY_OF_MONTH) != 1 ) {
                return;
            }
        }

        // 装備対象MOBでなければ、動作しないようにする。
        if ( event.getEntityType() != EntityType.ZOMBIE &&
                event.getEntityType() != EntityType.PIG_ZOMBIE &&
                event.getEntityType() != EntityType.SKELETON ) {
            return;
        }

        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        if ( !owner.equals("") ) {
            SkullMeta meta = (SkullMeta)item.getItemMeta();
            meta.setOwner(owner);
            item.setItemMeta(meta);
            event.getEntity().setCustomName(owner);
        }
        event.getEntity().getEquipment().setHelmet(item);
        event.getEntity().getEquipment().setHelmetDropChance(0);
    }
}
