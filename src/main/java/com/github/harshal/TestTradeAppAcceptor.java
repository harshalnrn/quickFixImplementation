package com.github.harshal;


import quickfix.*;
import quickfix.field.*;
import quickfix.fix40.ExecutionReport;
import quickfix.fix40.NewOrderSingle;

import java.util.HashMap;
import java.util.Map;

/***
 * The Class TestTradeAppExecutor.
 * @author harshal n
 */
public class TestTradeAppAcceptor extends quickfix.fix42.MessageCracker implements Application {

    /** (non-Javadoc)
     * @see quickfix.Application#onCreate(quickfix.SessionID)
     */

    private Map<String, Double> priceMap = null;

    public TestTradeAppAcceptor() {
        priceMap = new HashMap<String, Double>();
        priceMap.put("EUR/USD", 1.234);
    }
    public void onCreate(SessionID sessionId) {
        System.out.println("Executor Session Created with SessionID = " + sessionId);
    }

    /** (non-Javadoc)
     * @see quickfix.Application#onLogon(quickfix.SessionID)
     */
    
    public void onLogon(SessionID sessionId) {

    }

    /** (non-Javadoc)
     * @see quickfix.Application#onLogout(quickfix.SessionID)
     */
    
    public void onLogout(SessionID sessionId) {

    }

    /** (non-Javadoc)
     * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
     */
    
    public void toAdmin(Message message, SessionID sessionId) {

    }

    /** (non-Javadoc)
     * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
     */
    
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
            RejectLogon {

    }

    /** (non-Javadoc)
     * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
     */
    
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {

    }

    /** (non-Javadoc)
     * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
     */


    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
            UnsupportedMessageType {
        //cracks the message which invokes onMessage callback
        crack(message, sessionId);
    }

    //overiding onMessage method.it retrieves information from execution report, sent by server post our request.


    @Override
    public void onMessage(quickfix.fix42.NewOrderSingle message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        super.onMessage(message, sessionID);
        OrdType orderType = message.getOrdType();
        Symbol currencyPair = message.getSymbol();
        /*
         * Assuming that we are directly dealing with Market
         */
        Price price = null;
        if (OrdType.FOREX_MARKET == orderType.getValue()) {
            if(this.priceMap.containsKey(currencyPair.getValue())){
                price = new Price(this.priceMap.get(currencyPair.getValue()));
            } else {
                price = new Price(1.4589);
            }

        }
        //We are hardcoding this to 1, but in real world this may be something like a sequence.
        OrderID orderNumber = new OrderID("1");
        //Id of the report, a unique identifier, once again this will be somthing like a sequence
        ExecID execId = new ExecID("1");
        //In this case this is a new order with no exception hence mark it as NEW
        ExecTransType exectutionTransactioType = new ExecTransType(ExecTransType.NEW);
        //This execution report is for confirming a new Order
        // ExecType purposeOfExecutionReport =new ExecType(ExecType.FILL);
        //Represents status of the order, since this order was successful mark it as filled.
        OrdStatus orderStatus = new OrdStatus(OrdStatus.FILLED);
        //Represents the currencyPair
        Symbol symbol = currencyPair;
        //Represents side
        Side side = message.getSide();
        //What is the quantity left for the day, we will take 100 as a hardcoded value, we can also keep a note of this from say limit module.
        OrderQty orderQty = new OrderQty(100);
        //Total quantity of all the trades booked in this application, here it is hardcoded to be 100.
        LastShares lastshares=new LastShares();

        LastPx lastPx=new LastPx();

        CumQty cummulativeQuantity = new CumQty(100);
        //Average Price, say make it 1.235
        AvgPx avgPx = new AvgPx(1.235);
        ExecutionReport executionReport = new ExecutionReport(orderNumber,execId, exectutionTransactioType,orderStatus, symbol, side, orderQty,lastshares,lastPx,cummulativeQuantity, avgPx);
        executionReport.set(price);
        try {
            Session.sendToTarget(executionReport, sessionID);     //sending order in-form of execution report object, to client over session.
        } catch (SessionNotFound e) {
            e.printStackTrace();
        }
    }


    //Quick fix has library for each type of fix message.


}