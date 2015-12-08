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
package com.psygate.bellona.data;

import com.psygate.datastructures.spatial.D3BoundingBox;

/**
 *
 * @author psygate (https://github.com/psygate)
 */
public class DisableField {

    private long creation;
    private long expires;
    private double damageMultiplier;
    private D3BoundingBox box;

    public DisableField(long creation, long expires, double damageMultiplier, D3BoundingBox box) {
        this.creation = creation;
        this.expires = expires;
        this.damageMultiplier = damageMultiplier;
        this.box = box;
    }

    public long getCreation() {
        return creation;
    }

    public void setCreation(long creation) {
        this.creation = creation;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(double damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    public D3BoundingBox getBox() {
        return box;
    }

    public void setBox(D3BoundingBox box) {
        this.box = box;
    }

    @Override
    public String toString() {
        return "DisableField{" + "creation=" + creation + ", expires=" + expires + ", damageMultiplier=" + damageMultiplier + ", box=" + box + '}';
    }

}
