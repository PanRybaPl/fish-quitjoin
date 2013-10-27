/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.panryba.mc.quitjoin;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.ChatColor;

public class PluginApi {

    private final Object sync;
    
    private Set<String> previousJoined;
    private Set<String> previousQuitted;
    private Set<String> joined;
    private Set<String> quitted;
    
    private final BroadcastOutput output;

    public PluginApi(BroadcastOutput output) {
        this.output = output;
        this.sync = new Object();

        this.joined = new HashSet<>();
        this.previousJoined = new HashSet<>();

        this.quitted = new HashSet<>();
        this.previousQuitted = new HashSet<>();
    }

    void broadcast() {
        String joinedStr;
        String quittedStr;

        Set<String> joinDiff;
        Set<String> quitDiff;

        synchronized (sync) {
            joinDiff = SetUtils.diff(this.joined, this.previousJoined);
            this.previousJoined = this.joined;
            this.joined = new HashSet<>();
            
            quitDiff = SetUtils.diff(this.quitted, this.previousQuitted);
            this.previousQuitted = this.quitted;
            this.quitted = new HashSet<>();
        }

        joinedStr = StringUtils.joinStrings(joinDiff);
        quittedStr = StringUtils.joinStrings(quitDiff);

        if (!joinedStr.isEmpty()) {
            output.broadcast(ChatColor.GRAY + "dolaczyli: " + joinedStr);
        }

        if (!quittedStr.isEmpty()) {
            output.broadcast(ChatColor.GRAY + "wyszli: " + quittedStr);
        }
    }

    void joined(String name) {
        synchronized (sync) {
            this.joined.add(name);
            this.quitted.remove(name);
        }
    }

    void quit(String name) {
        synchronized (sync) {
            this.quitted.add(name);
            this.joined.remove(name);
        }
    }
}
