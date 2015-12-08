/*
 * bellona, a bukkit/spigot warfare plugin.
 * Copyright (C) 2015  psygate (https://github.com/psygate)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/gpl-3.0.html>.
 * 
 */
package com.psygate.bellona;

import com.psygate.nucleus.util.NucleusHelper;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author psygate (https://github.com/psygate)
 */
public class Configuration {

    private static Configuration instance;
    private final double tntDisableSize;
    private final long tntDuration;
    private final double tntDamageIncrease;

    private Configuration(FileConfiguration conf) {
        tntDisableSize = conf.getInt("tnt_disable_size");
        tntDuration = NucleusHelper.parseTimeStringToMillis(conf.getString("tnt_duration"));
        tntDamageIncrease = conf.getDouble("tnt_damage_increase");
        assert tntDamageIncrease > 0;
    }

    public static void init(FileConfiguration conf) {
        instance = new Configuration(conf);
    }

    public static double getTntDisableSize() {
        return instance.tntDisableSize;
    }

    public static long getTntDuration() {
        return instance.tntDuration;
    }

    public static double getTntDamageIncrease() {
        return instance.tntDamageIncrease;
    }

}
