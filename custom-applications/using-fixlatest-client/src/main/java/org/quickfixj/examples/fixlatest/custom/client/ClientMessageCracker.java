package org.quickfixj.examples.fixlatest.custom.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.*;
import quickfix.fixlatest.BusinessMessageReject;
import quickfix.fixlatest.ExecutionReport;
import quickfix.fixlatest.MessageCracker;
import quickfix.fixlatest.NewOrderSingle;
import quickfix.fixlatest.component.Instrument;

import java.time.LocalDateTime;
import java.util.UUID;

public class ClientMessageCracker extends MessageCracker {
    private final Logger log = LoggerFactory.getLogger(ClientMessageCracker.class);

    @Override
    public void onMessage(ExecutionReport executionReport, SessionID sessionID)
            throws FieldNotFound {
        Instrument instrumentComponent = executionReport.getInstrumentComponent(); // invariant
        log.info("Received ExecutionReport from sender [{}]: clOrdID {}, symbol {}, side {}, transactTime {}, ordType {}",
                sessionID.getSenderCompID(),
                instrumentComponent.getSymbol().getValue(),
                executionReport.getClOrdID().getValue(),
                executionReport.getSide().getValue(),
                executionReport.getTransactTime().getValue(),
                executionReport.getOrdType().getValue());
        if (instrumentComponent.isSetSecurityIDSource()) {
            log.info("securityIDSource {}, securityID {}",
                    instrumentComponent.getSecurityIDSource().getValue(),
                    instrumentComponent.getSecurityID().getValue());
        }
    }

    @Override
    public void onMessage(BusinessMessageReject businessMessageReject, SessionID sessionID)
            throws FieldNotFound {
        log.error("Received Business Message Reject from sender [{}]: refMsgType {}, businessRejectReason{}, Text {}",
                sessionID.getSenderCompID(),
                businessMessageReject.getRefMsgType().getValue(),
                businessMessageReject.getBusinessRejectReason().getValue(),
                businessMessageReject.isSetText() ? businessMessageReject.getText().getValue() : "");
    }
}