package me.kvq.anvil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.kvq.anvil.AnvilEvent;
import me.kvq.anvil.flashlight.FLClickEvent;
import me.kvq.anvil.flashlight.Fl;
import me.kvq.anvil.flashlight.Reapply;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class AnvilColors extends JavaPlugin {

   public static AnvilColors p = null;


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
               StringBuilder var15 = new StringBuilder();
               Iterator l = Fl.pl.iterator();

               while(l.hasNext()) {
                  String var17 = (String)l.next();
                  Player a = Bukkit.getPlayer(var17);
                  var15.append((a == null?"§c":"§a") + var17 + " ");
               }

               p.sendMessage(var15.toString());
            } else if(args.length == 1 && args[0].equalsIgnoreCase("help")) {
               Iterator im = this.getConfig().getStringList("Help").iterator();

               while(im.hasNext()) {
                  String var14 = (String)im.next();
                  p.sendMessage(var14.replaceAll("&", "§"));
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

               Player is = Bukkit.getPlayer(args[0]);
               if(Fl.isOn(is)) {
                  Fl.toggle(is, false);
                  is.sendMessage(this.getConfig().getString("Flashlight.Disable").replaceAll("&", "§"));
                  p.sendMessage("§7" + is.getName() + ": " + this.getConfig().getString("Flashlight.Disable").replaceAll("&", "§"));
               } else {
                  Fl.toggle(is, true);
                  is.sendMessage(this.getConfig().getString("Flashlight.Enable").replaceAll("&", "§"));
                  p.sendMessage("§7" + is.getName() + ": " + this.getConfig().getString("Flashlight.Enable").replaceAll("&", "§"));
               }
            }
         } else {
            String l1;
            int var11;
            int var12;
            String[] var13;
            ItemStack var16;
            ItemMeta var18;
            StringBuilder var19;
            int var21;
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
                  var13 = args;
                  var12 = args.length;

                  for(var11 = 0; var11 < var12; ++var11) {
                     l1 = var13[var11];
                     if(var21 == 0) {
                        var19.append(l1.replaceAll("&", "§"));
                     } else {
                        var19.append(" " + l1.replaceAll("&", "§"));
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
                  var13 = args;
                  var12 = args.length;

                  for(var11 = 0; var11 < var12; ++var11) {
                     l1 = var13[var11];
                     if(var21 == 0) {
                        var19.append(l1.replaceAll("&", "§"));
                     } else {
                        var19.append(" " + l1.replaceAll("&", "§"));
                     }

                     ++var21;
                  }

                  Object var22 = var18.hasLore()?var18.getLore():new ArrayList();
                  ((List)var22).add(var19.toString());
                  var18.setLore((List)var22);
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
                  ArrayList var20 = new ArrayList();
                  var18.setLore(var20);
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
