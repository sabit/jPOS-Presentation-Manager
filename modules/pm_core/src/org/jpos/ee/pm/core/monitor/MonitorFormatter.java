/*
 * jPOS Project [http://jpos.org]
 * Copyright (C) 2000-2010 Alejandro P. Revilla
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jpos.ee.pm.core.monitor;

import java.util.Properties;

/**
 * This class is executed by PM for each line generated by the monitor. This must return
 * an HTML formatted String to output. <br/>
 * By default, the formatter returns the line "as is". 
 * Programmer can extend this class and redefine format method to change the output.
 * 
 * @author jpaoletti jeronimo.paoletti@gmail.com
 * @see http://github.com/jpaoletti/jPOS-Presentation-Manager
 * 
 * */
public class MonitorFormatter {
    private Properties properties;

    /**Called for each line generated by the monitor before output it on the result HTML. 
     * @param line Monitor line
     * @return Formatted monitor line*/
    public String format(MonitorLine line){
        return line.getValue().toString();
    }
    
    /**Getter for a specific property with a default value in case its not defined. 
     * Only works for string.
     * @param name Property name
     * @param def Default value
     * @return Property value only if its a string */
    public String getConfig (String name, String def) {
        if (properties != null) {
            Object obj = properties.get (name);
            if (obj instanceof String)
                return obj.toString();
        } 
        return def;
    }
    
    /**Getter for any property in the properties object
     * @param name The property name
     * @return The property value */
    public String getConfig (String name) {
        return getConfig (name, null);
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * @return the properties
     */
    public Properties getProperties() {
        return properties;
    }
}
