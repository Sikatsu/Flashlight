package me.kvq.anvil;

import me.kvq.anvil.AnvilColors;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilEvent implements Listener {

   @EventHandler
   public void onInvClick(InventoryClickEvent e) {
      if(e.getInventory().getType() == InventoryType.ANVIL && e.getWhoClicked().hasPermission("anvil.allow")) {
         ItemStack i;
         ItemMeta m;
         if(e.getRawSlot() == 2) {
            if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || !e.getCurrentItem().getItemMeta().hasDisplayName()) {
               return;
            }

            if(AnvilColors.isBlocked(e.getCurrentItem().getType())) {
               return;
            }

            i = e.getCurrentItem();
            m = i.getItemMeta();
            if(m.getDisplayName().length() > AnvilColors.p.getConfig().getInt("Anvil.Limit")) {
               m.setDisplayName(m.getDisplayName().substring(0, AnvilColors.p.getConfig().getInt("Anvil.Limit")).replaceAll("&", "§") + "§9§0");
            } else {
               m.setDisplayName(m.getDisplayName().replaceAll("&", "§") + "§9§0");
            }

            i.setItemMeta(m);
            e.setCurrentItem(i);
         } else if(e.getRawSlot() == 0) {
            if(e.getCursor() == null || e.getCursor().getType() == Material.AIR || !e.getCursor().getItemMeta().hasDisplayName()) {
               return;
            }

            i = e.getCursor();
            m = i.getItemMeta();
            if(m.getDisplayName().length() > AnvilColors.p.getConfig().getInt("Anvil.Limit")) {
               m.setDisplayName(m.getDisplayName().substring(0, AnvilColors.p.getConfig().getInt("Anvil.Limit")).replaceAll("&", "§").replaceAll("&9&0", ""));
            } else {
               m.setDisplayName(m.getDisplayName().replaceAll("§", "&").replaceAll("&9&0", ""));
            }

            i.setItemMeta(m);
            i.setAmount(i.getAmount());
         }
      }

   }

   @EventHandler
   public void onDrag(InventoryDragEvent e) {
      if(e.getInventory().getType() == InventoryType.ANVIL && e.getWhoClicked().hasPermission("anvil.allow") && (e.getNewItems().containsKey(Integer.valueOf(0)) || e.getNewItems().containsKey(Integer.valueOf(1)) || e.getNewItems().containsKey(Integer.valueOf(2)))) {
         e.setCancelled(true);
      }

   }
}
