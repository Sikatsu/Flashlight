package nl.villagercraft.kvq.flashlight;

import nl.villagercraft.kvq.FlashLight;
import nl.villagercraft.kvq.flashlight.Fl;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Reapply {

   public static void a(final Player p) {
      (new BukkitRunnable() {
         public void run() {
            Fl.apply(p);
         }
      }).runTaskLater(FlashLight.p, 50L);
   }

   public static void b(final Player p) {
      (new BukkitRunnable() {
         public void run() {
            Fl.apply(p);
         }
      }).runTaskLater(FlashLight.p, 10L);
   }
}
