package nl.villagercraft.kvq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nl.villagercraft.kvq.AnvilEvent;
import nl.villagercraft.kvq.flashlight.FLClickEvent;
import nl.villagercraft.kvq.flashlight.Fl;
import nl.villagercraft.kvq.flashlight.Reapply;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class FlashLight extends JavaPlugin {

   public static FlashLight p = null;


   public void onEnable() {
      p = this;
      this.saveDefaultConfig();
      Bukkit.getPluginManager().registerEvents(new FLClickEvent(), this);
      Bukkit.getPluginManager().registerEvents(new AnvilEvent(), this);
      Fl.load();
      Iterator var2 = Bukkit.getOnlinePlayers().iterator();

      while(var2.hasNext()) {
         Player p = (Player)var2.next();
         if(Fl.isOn(p)) {
            Reapply.b(p);
         }
      }

   }

   public void onDisable() {
      Iterator var2 = Bukkit.getOnlinePlayers().iterator();

      while(var2.hasNext()) {
         Player p = (Player)var2.next();
         Fl.remove(p);
      }

   }

   public static boolean isBlocked(Material m) {
      Iterator var2 = p.getConfig().getStringList("BlockedItems").iterator();

      while(var2.hasNext()) {
         String s = (String)var2.next();
         Material mt = Material.getMaterial(s);
         if(mt == m) {
            return true;
         }
      }

      return false;
   }

   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
      if(sender instanceof ConsoleCommandSender) {
         return false;
      } else {
         Player p = (Player)sender;
         if(cmd.getName().equalsIgnoreCase("fl")) {
            if(!p.hasPermission("fl.allow")) {
               p.sendMessage(this.getConfig().getString("NoPermission").replaceAll("&", "§"));
               return false;
            }

            if(args.length == 0) {
               if(Fl.isOn(p)) {
                  p.sendMessage(this.getConfig().getString("Flashlight.Disable").replaceAll("&", "§"));
                  Fl.toggle(p, false);
               } else {
                  p.sendMessage(this.getConfig().getString("Flashlight.Enable").replaceAll("&", "§"));
                  Fl.toggle(p, true);
               }
            } else if(args.length == 1 && args[0].equalsIgnoreCase("list")) {
               if(!p.hasPermission("fl.admin")) {
                  p.sendMessage(this.getConfig().getString("NoPermission").replaceAll("&", "§"));
                  return false;
               }

               p.sendMessage(this.getConfig().getString("Flashlight.Line").replaceAll("&", "§"));
               StringBuilder var161 = new StringBuilder();
               Iterator var181 = Fl.pl.iterator();

               while(var181.hasNext()) {
                  String var12 = (String)var181.next();
                  Player var13 = Bukkit.getPlayer(var12);
                  var161.append((var13 == null?"§c":"§a") + var12 + " ");
               }

               p.sendMessage(var161.toString());
            } else if(args.length == 1 && args[0].equalsIgnoreCase("help")) {
               Iterator var15 = this.getConfig().getStringList("Help").iterator();

               while(var15.hasNext()) {
                  String var11 = (String)var15.next();
                  p.sendMessage(var11.replaceAll("&", "§"));
               }
            } else if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
               if(!p.hasPermission("fl.admin")) {
                  p.sendMessage(this.getConfig().getString("NoPermission").replaceAll("&", "§"));
                  return false;
               }

               p.reloadConfig();
               p.sendMessage(this.getConfig().getString("Reload").replaceAll("&", "§"));
            } else if(args.length == 1 && Bukkit.getPlayer(args[0]) != null) {
               if(!p.hasPermission("fl.admin")) {
                  p.sendMessage(this.getConfig().getString("NoPermission").replaceAll("&", "§"));
                  return false;
               }

               Player l1 = Bukkit.getPlayer(args[0]);
               if(Fl.isOn(l1)) {
                  Fl.toggle(l1, false);
                  l1.sendMessage(this.getConfig().getString("Flashlight.Disable").replaceAll("&", "§"));
                  p.sendMessage("§7" + l1.getName() + ": " + this.getConfig().getString("Flashlight.Disable").replaceAll("&", "§"));
               } else {
                  Fl.toggle(l1, true);
                  l1.sendMessage(this.getConfig().getString("Flashlight.Enable").replaceAll("&", "§"));
                  p.sendMessage("§7" + l1.getName() + ": " + this.getConfig().getString("Flashlight.Enable").replaceAll("&", "§"));
               }
            }
         } else {
            ItemStack var16;
            ItemMeta var18;
            StringBuilder var19;
            int var21;
            String var17;
            int var191;
            int var201;
            String[] var211;
            if(cmd.getName().equalsIgnoreCase("rename")) {
               if(!p.hasPermission("rename.allow")) {
                  p.sendMessage(this.getConfig().getString("Rename.ItemBlocked").replaceAll("&", "§"));
                  return false;
               }

               if(args.length == 0) {
                  p.sendMessage("/rename <name>");
               } else {
                  var16 = p.getItemInHand();
                  if(var16 == null || var16.getType() == Material.AIR) {
                     p.sendMessage(this.getConfig().getString("Rename.NoIS").replaceAll("&", "§"));
                     return false;
                  }

                  if(isBlocked(var16.getType())) {
                     p.sendMessage(this.getConfig().getString("Rename.ItemBlocked").replaceAll("&", "§"));
                     return false;
                  }

                  var18 = var16.getItemMeta();
                  var19 = new StringBuilder();
                  var21 = 0;
                  var211 = args;
                  var201 = args.length;

                  for(var191 = 0; var191 < var201; ++var191) {
                     var17 = var211[var191];
                     if(var21 == 0) {
                        var19.append(var17.replaceAll("&", "§"));
                     } else {
                        var19.append(" " + var17.replaceAll("&", "§"));
                     }

                     ++var21;
                  }

                  var18.setDisplayName(var19.toString());
                  var16.setItemMeta(var18);
                  p.setItemInHand(var16);
                  p.updateInventory();
                  p.sendMessage(this.getConfig().getString("Rename.Rename").replaceAll("&", "§"));
               }
            } else if(cmd.getName().equalsIgnoreCase("addlore")) {
               if(!p.hasPermission("relore.allow")) {
                  p.sendMessage(this.getConfig().getString("NoPermission").replaceAll("&", "§"));
                  return false;
               }

               if(args.length == 0) {
                  p.sendMessage("/addlore <lore>");
               } else {
                  var16 = p.getItemInHand();
                  if(var16 == null || var16.getType() == Material.AIR) {
                     p.sendMessage(this.getConfig().getString("Rename.NoIS").replaceAll("&", "§"));
                     return false;
                  }

                  if(isBlocked(var16.getType())) {
                     p.sendMessage(this.getConfig().getString("Rename.ItemBlocked").replaceAll("&", "§"));
                     return false;
                  }

                  var18 = var16.getItemMeta();
                  var19 = new StringBuilder();
                  var21 = 0;
                  var211 = args;
                  var201 = args.length;

                  for(var191 = 0; var191 < var201; ++var191) {
                     var17 = var211[var191];
                     if(var21 == 0) {
                        var19.append(var17.replaceAll("&", "§"));
                     } else {
                        var19.append(" " + var17.replaceAll("&", "§"));
                     }

                     ++var21;
                  }

                  Object var20 = var18.hasLore()?var18.getLore():new ArrayList();
                  ((List)var20).add(var19.toString());
                  var18.setLore((List)var20);
                  var16.setItemMeta(var18);
                  p.setItemInHand(var16);
                  p.updateInventory();
                  p.sendMessage(this.getConfig().getString("Rename.Relore").replaceAll("&", "§"));
               }
            } else if(cmd.getName().equalsIgnoreCase("removelore")) {
               if(!p.hasPermission("relore.allow")) {
                  p.sendMessage(this.getConfig().getString("NoPermission").replaceAll("&", "§"));
                  return false;
               }

               var16 = p.getItemInHand();
               if(var16 != null && var16.getType() != Material.AIR) {
                  if(isBlocked(var16.getType())) {
                     p.sendMessage(this.getConfig().getString("Rename.ItemBlocked").replaceAll("&", "§"));
                     return false;
                  }

                  var18 = var16.getItemMeta();
                  ArrayList var22 = new ArrayList();
                  var18.setLore(var22);
                  var16.setItemMeta(var18);
                  p.setItemInHand(var16);
                  p.updateInventory();
                  p.sendMessage(this.getConfig().getString("Rename.Relore").replaceAll("&", "§"));
                  return false;
               }

               p.sendMessage(this.getConfig().getString("Rename.NoIS").replaceAll("&", "§"));
               return false;
            }
         }

         return false;
      }
   }
}
