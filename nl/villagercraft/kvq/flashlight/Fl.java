package nl.villagercraft.kvq.flashlight;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nl.villagercraft.kvq.FlashLight;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Fl {

   public static List pl = new ArrayList();
   public static File f = new File(FlashLight.p.getDataFolder(), "data.da");
   public static FileConfiguration c = YamlConfiguration.loadConfiguration(f);


   public static void apply(Player p) {
      p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false));
   }

   public static void remove(Player p) {
      p.removePotionEffect(PotionEffectType.NIGHT_VISION);
   }

   public static void load() {
      Iterator var1 = c.getStringList("Players").iterator();

      while(var1.hasNext()) {
         String s = (String)var1.next();
         pl.add(s);
      }

   }

   public static void save() {
      c.set("Players", pl);

      try {
         c.save(f);
      } catch (IOException var1) {
         var1.printStackTrace();
      }

   }

   public static void toggle(Player p, boolean on) {
      if(on && !isOn(p)) {
         apply(p);
         pl.add(p.getName());
      } else if(!on) {
         remove(p);
         pl.remove(p.getName());
      }

      save();
   }

   public static boolean isOn(Player p) {
      return pl.contains(p.getName());
   }

   public static boolean isOn(String p) {
      return pl.contains(p);
   }
}
