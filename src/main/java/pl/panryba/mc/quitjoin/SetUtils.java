/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.panryba.mc.quitjoin;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Marcin
 */
public class SetUtils {
    public static Set<String> diff(Set<String> target, Set<String> source) {
        Set<String> result = new HashSet<>();
        
        for (String targetValue : target) {
            if (!source.contains(targetValue)) {
                result.add(targetValue);
            }
        }        
        
        return result;
    }
}
