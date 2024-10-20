package org.quickfixj.examples.fixlatest.custom.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.fixlatest.MessageCracker;


public class ServerApplicationAdapter extends ApplicationAdapter {

    private final MessageCracker messageCracker;
    private final Logger log = LoggerFactory.getLogger(ServerApplicationAdapter.class);

    public ServerApplicationAdapter(MessageCracker messageCracker) {
        this.messageCracker = messageCracker;
    }

    @Override
    public void onLogon(SessionID sessionID) {
        log.info("logged on sender[{}] -> target[{}]", sessionID.getSenderCompID(), sessionID.getTargetCompID());
    }

    @Override
    public void onLogout(SessionID sessionID) {
        log.info("logged out sender[{}] -> target[{}]", sessionID.getSenderCompID(), sessionID.getTargetCompID());
    }

    @Override
    public void fromApp(Message message, SessionID sessionID)
            throws FieldNotFound, IncorrectTagValue, UnsupportedMessageType {
        this.messageCracker.crack(message, sessionID);
    }
}