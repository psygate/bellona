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

import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.psygate.datastructures.spatial.*;
import com.psygate.datastructures.spatial.trees.recursive.*;

/**
 *
 * @author psygate (https://github.com/psygate)
 */
final class OctreeContainer {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final UUID world;
    private OcTree<ID3BoundingBox, DisableField> octree;

    public OctreeContainer(UUID world) {
        this.world = world;
        ID3Point center = new D3Point(0, 0, 0);
        double rad = 1000;
        octree = new OcTree<>(new D3BoundingBox(center, rad), 25);
    }

    public ReentrantReadWriteLock getLock() {
        return lock;
    }

    public UUID getWorld() {
        return world;
    }

    public OcTree<ID3BoundingBox, DisableField> getOctree() {
        return octree;
    }

    public void setOctree(OcTree<ID3BoundingBox, DisableField> octree) {
        this.octree = octree;
    }

}
