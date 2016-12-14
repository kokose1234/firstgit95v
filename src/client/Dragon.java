/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import constants.GameConstants;
import server.maps.AbstractAnimatedMapleMapObject;
import server.maps.MapleMapObjectType;
import tools.packet.DragonPacket;

public class Dragon extends AbstractAnimatedMapleMapObject {

    private int owner, jobId;

    public Dragon(MapleCharacter owner) {
        super();
        this.owner = owner.getId();
        this.jobId = owner.getJob().getId();
        if (!GameConstants.isEvan(owner.getJob().getId())) {
            throw new RuntimeException("Trying to create a dragon for a non-Evan");
        }
        setPosition(owner.getPosition());
        setStance(4);
    }

    @Override
    public void sendSpawnData(MapleClient client) {
        client.getSession().write(DragonPacket.OnDragonEnterField(this));
    }

    @Override
    public void sendDestroyData(MapleClient client) {
        client.getSession().write(DragonPacket.OnDragonLeaveField(this));
    }

    public int getOwner() {
        return owner;
    }

    public int getJobId() {
        return jobId;
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.SUMMON;
    }
}
