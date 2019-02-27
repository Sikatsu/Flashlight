package me.kvq.anvil.flashlight;

import me.kvq.anvil.AnvilColors;
import me.kvq.anvil.flashlight.Fl;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Reapply {

   public static void a(final Player p) {
      (new BukkitRunnable() {
         public void run() {
            Fl.apply(p);
         }
      }).runTaskLater(AnvilColors.p, 50L);
   }

   public static void b(final Player p) {
      (new BukkitRunnable() {
         public void run() {
            Fl.apply(p);
         }
      }).runTaskLater(AnvilColors.p, 10L);
   }
}
