/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.panryba.mc.quitjoin;

import java.util.HashSet;
import java.util.Set;

public class PluginApi {

    private final Object sync;
    
    private Set<String> previous;
    private Set<String> current;
    
    private final BroadcastOutput output;
    private final LanguageStrings strings;

    public PluginApi(BroadcastOutput output, LanguageStrings strings) {
        this.strings = strings;
        this.output = output;        
        this.sync = new Object();
        this.previous = new HashSet<>();
        this.current = new HashSet<>();
    }

    void broadcast() {
        Set<String> joined;
        Set<String> left;
        
        synchronized(sync) {
            joined = SetUtils.diff(current, previous);
            left = SetUtils.diff(previous, current);
            
            this.previous = new HashSet<>(this.current);
        }
       
        String joinedStr = StringUtils.joinStrings(joined);
        String leftStr = StringUtils.joinStrings(left);

        if (!joinedStr.isEmpty()) {
            output.broadcast(this.strings.getJoined(joinedStr));
        }

        if (!leftStr.isEmpty()) {
            output.broadcast(this.strings.getLeft(leftStr));
        }
    }

    void joined(String name) {
        synchronized (sync) {
            this.current.add(name);
        }
    }

    void left(String name) {
        synchronized (sync) {
            this.current.remove(name);
        }
    }
}
