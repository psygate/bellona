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
import com.psygate.nucleus.Nucleus;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/**
 *
 * @author psygate (https://github.com/psygate)
 */
public class FieldManager {

    private static final Logger LOG = Nucleus.getLogger(FieldManager.class.getName());

    private static FieldManager instance = null;
    private final ConcurrentMap<UUID, OctreeContainer> map = new ConcurrentHashMap<>();
    private final OctreeContainer nullcontainer = new OctreeContainer(new UUID(0, 0));

    public <T> Optional<T> apply(final ReadOperation<T> op, final UUID world) {
        Reference<T> ref = new Reference<>();

        map.computeIfPresent(world, (UUID t, OctreeContainer con) -> {
            try {
                con.getLock().readLock().lock();
                ref.setValue((op.call(con.getOctree())));
                return con;
            } finally {
                con.getLock().readLock().unlock();
            }
        });

        return ref.asOptional();
    }

    public <T> Optional<T> apply(final WriteOperation<T> op, final UUID world) {
        Reference<T> ref = new Reference<>();
        Reference<OcTree<ID3BoundingBox, DisableField>> replace = new Reference<>();

        map.computeIfPresent(world, (UUID t, OctreeContainer con) -> {
            try {
                con.getLock().writeLock().lock();
                ref.setValue((op.call(con.getOctree(), replace)));
                replace.asOptional().ifPresent((tree) -> con.setOctree(tree));
                return con;
            } finally {
                con.getLock().writeLock().unlock();
            }
        });

        return ref.asOptional();
    }

    public void apply(final VoidReadOperation op, final UUID world) {

        map.computeIfPresent(world, (UUID t, OctreeContainer con) -> {
            try {
                con.getLock().readLock().lock();
                op.call(con.getOctree());
                return con;
            } finally {
                con.getLock().readLock().unlock();
            }
        });
    }

    public void apply(final VoidWriteOperation op, final UUID world) {
        Reference<OcTree<ID3BoundingBox, DisableField>> replace = new Reference<>();

        map.computeIfPresent(world, (UUID t, OctreeContainer con) -> {
            try {
                con.getLock().writeLock().lock();
                op.call(con.getOctree(), replace);
                replace.asOptional().ifPresent((tree) -> con.setOctree(tree));
                return con;
            } finally {
                con.getLock().writeLock().unlock();
            }
        });
    }

    public <T> T createAndModifyTree(final WriteOperation<T> op, final UUID world) {

        final Reference<T> ref = new Reference<>();
        Reference<OcTree<ID3BoundingBox, DisableField>> replace = new Reference<>();

        map.compute(world, (UUID t, OctreeContainer con) -> {

            if (con == null) {
                OctreeContainer newcon = new OctreeContainer(t);
                ref.setValue(op.call(newcon.getOctree(), replace));
                replace.asOptional().ifPresent((tree) -> newcon.setOctree(tree));
                return newcon;
            } else {
                try {
                    con.getLock().writeLock().lock();
                    ref.setValue(op.call(con.getOctree(), replace));
                    replace.asOptional().ifPresent((tree) -> con.setOctree(tree));
                } finally {
                    con.getLock().writeLock().unlock();
                }

                return con;
            }
        });

        return ref.getValue();
    }

    public void createAndModifyTree(final VoidWriteOperation op, final UUID world) {
        Reference<OcTree<ID3BoundingBox, DisableField>> replace = new Reference<>();

        map.compute(world, (UUID t, OctreeContainer con) -> {
            if (con == null) {
                OctreeContainer newcon = new OctreeContainer(t);
                op.call(newcon.getOctree(), replace);
                replace.asOptional().ifPresent((tree) -> newcon.setOctree(tree));
                return newcon;
            } else {
                try {
                    con.getLock().writeLock().lock();
                    op.call(con.getOctree(), replace);
                    replace.asOptional().ifPresent((tree) -> con.setOctree(tree));
                } finally {
                    con.getLock().writeLock().unlock();
                }

                return con;
            }
        }
        );
    }

    public static FieldManager getInstance() {
        if (instance == null) {
            instance = new FieldManager();
        }

        return instance;

    }
}
