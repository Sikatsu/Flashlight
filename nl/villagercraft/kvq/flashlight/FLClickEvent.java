package nl.villagercraft.kvq.flashlight;

import nl.villagercraft.kvq.flashlight.Fl;
import nl.villagercraft.kvq.flashlight.Reapply;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class FLClickEvent implements Listener {

   @EventHandler
   public void onClick(PlayerInteractEvent e) {
      if(e.getAction() != Action.PHYSICAL && e.getItem() != null && e.getItem().getType() == Material.MILK_BUCKET && Fl.isOn(e.getPlayer())) {
         Reapply.a(e.getPlayer());
      }

   }

   @EventHandler
   public void onResp(PlayerRespawnEvent e) {
      if(Fl.isOn(e.getPlayer())) {
         Reapply.b(e.getPlayer());
      }

   }

   @EventHandler
   public void onExit(PlayerQuitEvent e) {
      if(Fl.isOn(e.getPlayer())) {
         Fl.remove(e.getPlayer());
      }

   }

   @EventHandler
   public void onJoin(PlayerJoinEvent e) {
      if(Fl.isOn(e.getPlayer())) {
         Fl.apply(e.getPlayer());
      }

   }
}
