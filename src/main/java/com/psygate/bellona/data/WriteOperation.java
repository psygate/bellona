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

import com.psygate.datastructures.spatial.ID3BoundingBox;
import com.psygate.datastructures.spatial.trees.recursive.OcTree;

/**
 *
 * @author psygate (https://github.com/psygate)
 * @param <T>
 */
public interface WriteOperation<T> {

    public T call(OcTree<ID3BoundingBox, DisableField> tree, Reference<OcTree<ID3BoundingBox, DisableField>> replace);
}
