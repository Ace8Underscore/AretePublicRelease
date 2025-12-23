package com.cousinware.arete.utils.rotations;

import com.cousinware.arete.events.event.PacketEvent;
import com.google.common.eventbus.Subscribe;

public interface Rotation {

    @Subscribe
    void packetListener(PacketEvent.Send event);


}
