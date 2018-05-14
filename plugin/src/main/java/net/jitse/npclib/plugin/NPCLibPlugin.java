/*
 * Copyright (c) 2018 Jitse Boonstra
 */

package net.jitse.npclib.plugin;

import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.plugin.listeners.NPCListener;
import net.jitse.npclib.skin.MineSkinFetcher;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

/**
 * @author Jitse Boonstra
 */
public class NPCLibPlugin extends JavaPlugin implements Listener {

    private NPCLib npcLib;
    private NPC npc;

    @Override
    public void onEnable() {
        this.npcLib = new NPCLib(this);
        getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "[NPCLib] " + ChatColor.WHITE + "plugin enabled.");
        getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "[NPCLib] " +
                ChatColor.GRAY + "This is a test plugin usually used for development reasons. " +
                "You can spawn NPCs by pressing [shift] in game.");


        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new NPCListener(), this);
    }

    @Override
    public void onDisable() {
        if (npc != null) {
            npc.destroy(false);
        }

        getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "[NPCLib] " + ChatColor.WHITE + "plugin disabled.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (npc != null) {
            npc.show(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerShift(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            return;
        }

        if (npc != null) {
            if (npc.isActuallyShown(event.getPlayer())) {
                npc.hide(event.getPlayer());
            } else {
                npc.show(event.getPlayer());
            }
        } else {
            MineSkinFetcher.fetchSkinFromIdAsync(168841, skin -> {
                npc = npcLib.createNPC(skin, Arrays.asList(
                        ChatColor.BOLD + "NPC Library", "",
                        "Create your own", "non-player characters",
                        "with the simplistic", "API of NPCLib!"
                ));
                npc.create(event.getPlayer().getLocation());

                for (Player player : getServer().getOnlinePlayers()) {
                    npc.show(player);
                }
            });
        }
    }
}
