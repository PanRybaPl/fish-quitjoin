/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.panryba.mc.quitjoin;

import java.util.Collection;

/**
 *
 * @author Marcin
 */
public class StringUtils {
    public static String joinStrings(Collection<String> names) {
        StringBuilder sb = new StringBuilder(1000);

        for (String name : names) {
            if (sb.length() > 0) {
                sb.append(", ");
            }

            sb.append(name);
        }

        return sb.toString();
    }
    
}
