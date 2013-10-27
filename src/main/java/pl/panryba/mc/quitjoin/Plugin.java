package pl.panryba.mc.quitjoin;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Plugin extends JavaPlugin implements Listener {
    private BukkitTask broadcastTask;
    private PluginApi api;
    
    private class BroadcastRunner implements Runnable {
        private final PluginApi api;

        private BroadcastRunner(PluginApi api) {
            this.api = api;
        }

        @Override
        public void run() {
            this.api.broadcast();
        }
        
    }
    
    @Override
    public void onEnable() {
        this.api = new PluginApi(new BroadcastOutput() {
            @Override
            public void broadcast(String message) {
                Bukkit.broadcastMessage(message);
            }
        });
        
        getServer().getPluginManager().registerEvents(this, this);
        this.broadcastTask = getServer().getScheduler().runTaskTimer(this, new Plugin.BroadcastRunner(this.api), 20 * 30, 20 * 30);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Listener)this);
        
        if(this.broadcastTask != null) {
            this.broadcastTask.cancel();
            this.broadcastTask = null;
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.api.joined(event.getPlayer().getName());
        event.setJoinMessage("");
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.api.quit(event.getPlayer().getName());
        event.setQuitMessage("");
    }
}
