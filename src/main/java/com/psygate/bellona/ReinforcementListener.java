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

import com.psygate.amethyst.events.AmethystBlockDamageReinforcementEvent;
import com.psygate.amethyst.events.AmethystPlayerBlockDamageReinforcementEvent;
import com.psygate.bellona.data.DisableField;
import com.psygate.bellona.data.FieldManager;
import com.psygate.bellona.data.Reference;
import com.psygate.bellona.data.VoidReadOperation;
import com.psygate.bellona.data.VoidWriteOperation;
import com.psygate.datastructures.spatial.D3BoundingBox;
import com.psygate.datastructures.spatial.D3Point;
import com.psygate.datastructures.spatial.ID3BoundingBox;
import com.psygate.datastructures.spatial.ID3Point;
import com.psygate.datastructures.spatial.trees.recursive.ImmutableOcTree;
import com.psygate.datastructures.spatial.trees.recursive.OcTree;
import com.psygate.nucleus.Nucleus;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 *
 * @author psygate (https://github.com/psygate)
 */
public class ReinforcementListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void amethystDamage(AmethystPlayerBlockDamageReinforcementEvent ev) {
        FieldManager.getInstance().apply((ImmutableOcTree<ID3BoundingBox, DisableField> tree) -> {
            Location loc = ev.getBlock().getLocation();
            long now = System.currentTimeMillis();
            ID3Point point = new D3Point(loc.getX(), loc.getY(), loc.getZ());
            tree.selectiveValueStream((box) -> box.contains(point))
                    .filter((d) -> d.getBox().contains(point))
                    .filter((d) -> d.getExpires() > now)
                    .findAny()
                    .ifPresent((DisableField d) -> {
                        ev.setNewHealth((int) (ev.getOldHealth() - ev.getMaxHealth() * Configuration.getTntDamageIncrease()));
                        Nucleus.getLogger("bellona-damage-increase").log(Level.INFO, "New health: {0}", (int) (ev.getOldHealth() - ev.getMaxHealth() * Configuration.getTntDamageIncrease()));
                    });
        }, ev.getBlock().getWorld().getUID());
    }

    @EventHandler(ignoreCancelled = true)
    public void entityExplode(EntityExplodeEvent ev) {
        if (ev.getEntityType() == EntityType.PRIMED_TNT) {
            ev.setCancelled(true);
            ev.blockList().clear();
            ev.setYield(0);
            FieldManager.getInstance().createAndModifyTree((OcTree<ID3BoundingBox, DisableField> tree, Reference<OcTree<ID3BoundingBox, DisableField>> replace) -> {
                Location loc = ev.getLocation();
                long now = System.currentTimeMillis();
                ID3Point point = new D3Point(loc.getX(), loc.getY(), loc.getZ());
                DisableField field = new DisableField(now, Configuration.getTntDuration() + now, 0, new D3BoundingBox(point, Configuration.getTntDisableSize()));
                if (!tree.envelopes(field.getBox())) {
                    Nucleus.getLogger("bellona-damage-increase").log(Level.INFO, "Building new tree for {0}", field);
                    OcTree<ID3BoundingBox, DisableField> ntree = new OcTree<>(field.getBox().merge(tree.getBounds()), tree.getMaxNodeSize());
                    ntree.putAll(tree.entries());
                    ntree.put(field.getBox(), field);
                    replace.setValue(ntree);
                } else {
                    Nucleus.getLogger("bellona-damage-increase").log(Level.INFO, "Using old tree for {0}", field);
                    tree.put(field.getBox(), field);
                }
            }, ev.getLocation().getWorld().getUID());
        }
    }
}
