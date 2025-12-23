package com.cousinware.arete.events.event;

import com.cousinware.arete.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class KeyPressedEvent extends Event {

    long window;
    int key;
    int scancode;
    int action;
    int modifiers;

}
