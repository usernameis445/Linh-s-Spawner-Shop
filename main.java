package me.karina.spawnershop;

import org.bukkit.*;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

import java.util.*;

public class SpawnerShop extends JavaPlugin implements Listener {

    public static Economy econ;

    HashMap<Integer,String> mobs = new HashMap<>();

    public void onEnable(){

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(this,this);

        setupVault();

        mobs.put(10,"ZOMBIE");
        mobs.put(11,"SKELETON");
        mobs.put(12,"CREEPER");
        mobs.put(13,"SPIDER");
        mobs.put(14,"BLAZE");
        mobs.put(15,"IRON_GOLEM");
        mobs.put(16,"ENDERMAN");
    }


    public boolean onCommand(org.bukkit.command.CommandSender s,
    org.bukkit.command.Command c,String l,String[] a){

        if(s instanceof Player p){
            open(p);
        }

        return true;
    }


    void open(Player p){

        Inventory inv = Bukkit.createInventory(null,27,"Spawner Shop");

        for(int slot:mobs.keySet()){

            String mob=mobs.get(slot);

            ItemStack item=new ItemStack(Material.SPAWNER);

            ItemMeta meta=item.getItemMeta();

            meta.setDisplayName("§a"+mob+" Spawner");

            meta.setLore(Arrays.asList(
            "§7Price: §e$"+getConfig().getDouble("prices."+mob),
            "§7Click = 1",
            "§7Shift click = 64"));

            item.setItemMeta(meta);

            inv.setItem(slot,item);
        }

        p.openInventory(inv);
    }


    @EventHandler
    public void click(InventoryClickEvent e){

        if(!e.getView().getTitle().equals("Spawner Shop"))
            return;

        e.setCancelled(true);

        Player p=(Player)e.getWhoClicked();

        String mob=mobs.get(e.getSlot());

        if(mob==null)return;


        if(econ==null){
            p.sendMessage("§cVault economy missing!");
            return;
        }


        int amount=p.isSneaking()?64:1;

        double price=getConfig()
        .getDouble("prices."+mob)*amount;


        if(econ.getBalance(p)<price){

            p.sendMessage("§cNot enough money!");
            return;
