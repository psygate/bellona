/*
 * ruby, a bukkit/spigot player notification plugin.
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

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A simple strong reference to an object.
 *
 * @author psygate (https://github.com/psygate)
 */
public class Reference<T> {

    private T value;
    private boolean set;

    public Reference() {
        set(false);
    }

    public Reference(T value) {
        this.value = value;
        set(true);
    }

    public T getValue() {
        if (!isSet()) {
            throw new IllegalStateException("Reference not set.");
        }
        return value;
    }

    public void setValue(T value) {
        if (value == null) {
            throw new NullPointerException();
        }
        this.value = value;
        set(true);
    }

    public boolean isSet() {
        return set;
    }

    private void set(boolean set) {
        this.set = set;
    }

    public Optional<T> asOptional() {
        if (isSet()) {
            return Optional.of(value);
        } else {

            return Optional.empty();
        }
    }
}
