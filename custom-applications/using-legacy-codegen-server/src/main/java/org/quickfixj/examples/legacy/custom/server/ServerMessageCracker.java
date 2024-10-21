package org.quickfixj.examples.legacy.custom.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix50sp2.MessageCracker;
import quickfix.fix50sp2.BusinessMessageReject;
import quickfix.fix50sp2.ExecutionReport;
import quickfix.fix50sp2.NewOrderSingle;
import quickfix.fix50sp2.component.Instrument;

import java.time.LocalDateTime;
import java.util.UUID;

public class ServerMessageCracker extends MessageCracker {
    private final Logger log = LoggerFactory.getLogger(ServerMessageCracker.class);

    @Override
    public void onMessage(NewOrderSingle newOrderSingle, SessionID sessionID)
            throws FieldNotFound {
        Instrument instrumentComponent = newOrderSingle.getInstrument(); // invariant
        log.info("Received NewOrderSingle from sender [{}]: clOrdID {}, symbol {}, side {}, transactTime {}, ordType {}, securityIDSource {}, securityID {}",
                newOrderSingle.getHeader().getString(SenderCompID.FIELD),
                instrumentComponent.getSymbol().getValue(),
                newOrderSingle.getClOrdID().getValue(),
                newOrderSingle.getSide().getValue(),
                newOrderSingle.getTransactTime().getValue(),
                newOrderSingle.getOrdType().getValue(),
                instrumentComponent.isSetSecurityIDSource() ? instrumentComponent.getSecurityIDSource().getValue() : "",
                instrumentComponent.isSetSecurityID() ? instrumentComponent.getSecurityID().getValue() : "");
        try {
            Session.sendToTarget(executionReport(newOrderSingle),sessionID);
        } catch (SessionNotFound e) {
            log.error("SessionNot Found", e);
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

    private static ExecutionReport executionReport(NewOrderSingle newOrderSingle) throws FieldNotFound {
        ExecutionReport executionReport = new ExecutionReport();
        executionReport.set(newOrderSingle.getClOrdID());
        executionReport.set(newOrderSingle.getSide());
        executionReport.set(newOrderSingle.getOrdType());
        executionReport.set(new TransactTime(LocalDateTime.now()));
        executionReport.set(new OrderID(UUID.randomUUID().toString()));
        executionReport.set(new ExecID(UUID.randomUUID().toString()));
        executionReport.set(new ExecType(ExecType.TRADE));
        executionReport.set(new OrdStatus(OrdStatus.FILLED));
        executionReport.set(newOrderSingle.getInstrument());
        executionReport.set(new LeavesQty(0));
        executionReport.set(new CumQty(newOrderSingle.getOrderQtyData().getOrderQty().getValue()));
        executionReport.set(new LastQty(newOrderSingle.getOrderQtyData().getOrderQty().getValue()));
        if (newOrderSingle.isSetPrice()) {
            Price px = newOrderSingle.getPrice();
            executionReport.set(px);
            executionReport.set(new LastPx(px.getValue()));
        } else {
            executionReport.set(new LastPx(1.0d));
        }
        return executionReport;
    }
}
