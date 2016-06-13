package com.everhomes.aclink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DoorCommandAction implements Runnable {
    private final Long sequence;
    
    @Autowired
    private DoorAccessService doorAccessService;
    
    public DoorCommandAction(final String seq) {
        this.sequence = Long.valueOf(seq);
    }

    @Override
    public void run() {
        //Sequence is also DoorCommand.Id
        doorAccessService.onDoorMessageTimeout(sequence);
    }
}
