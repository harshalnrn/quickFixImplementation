package com.github.harshal;

import quickfix.*;
import quickfix.field.*;
import quickfix.fix40.ExecutionReport;
import quickfix.fix40.NewOrderSingle;

import java.util.HashMap;
import java.util.Map;

/***
 * The Class TestTradeAppInitiator. (i.e client)
 * @author harshal n
 *
 * Every FIX application should have an implementation of Application interface, Application interface contains call back methods.
 * MessageCracker provides callback methods, for receiving messages from Server.
 */
public class TestTradeAppInitiator extends quickfix.fix42.MessageCracker implements Application {

    /** (non-Javadoc)
     * @see quickfix.Application#onCreate(quickfix.SessionID)
     */

    private Map<String, Double> priceMap = null;

    public TestTradeAppInitiator() {
        priceMap = new HashMap<String, Double>();
        priceMap.put("EUR/USD", 1.234);
    }


    public void onCreate(SessionID sessionId) {
        System.out.println("onCreate called when QFJ creates a new session"
                + sessionId);
    }

    /** (non-Javadoc)
     * @see quickfix.Application#onLogon(quickfix.SessionID)
     */
    
    public void onLogon(SessionID sessionId) {
        //notifies when a successful logon has completed.
        System.out.println("onLogon notifies you when a valid logon has been established with a counter party. This is called when a connection has been established and the FIX logon process has completed with both parties exchanging valid logon messages." + sessionId);
    }

    /** (non-Javadoc)
     * @see quickfix.Application#onLogout(quickfix.SessionID)
     */
    
    public void onLogout(SessionID sessionId) {
        //notifies when a session is offline
        System.out.println("onLogout notifies you when an FIX session is no longer online" + sessionId);
    }

    /** (non-Javadoc)
     * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
     */
    
    public void toAdmin(Message message, SessionID sessionId) {
//all outbound admin level messages pass through this callback.
        System.out.println("toAdmin provides you with a peek at the administrative messages that are being sent from your FIX engine to the counter party");
    }

    /** (non-Javadoc)
     * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
     */
    
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
            RejectLogon {
//every inbound admin level message will pass through this method, such as heartbeats, logons, and logouts.
        System.out.println("fromAdmin has notified you when an administrative message is sent from a counterparty to your FIX engine for sessionId : "
                + sessionId);
    }


    /** (non-Javadoc)
     * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
     */
    
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        //all outbound application level messages pass through this callback before they are sent. If a tag needs to be added to every outgoing message, this is a good place to do that.
        System.out.println("toApp is a callback, for application messages that are being sent to a counterparty. Acceptor requests them to be sent again. example: resend MTM order");
        System.out.println("Message : " + message + " for sessionid : " + sessionId);

    }

    /*** (non-Javadoc)
     * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
     */
    
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
            UnsupportedMessageType {

        //every inbound application level message will pass through this method, such as orders, executions, secutiry definitions, and market data.


        System.out.println("fromApp is one of the core entry points for your FIX application. Every application level request will come through here from counterparty. Hence reception happens here"
                +sessionId );
        crack(message, sessionId);

        //What crack() does is this:
        //
        //Converts your Message into the proper subclass (e.g. NewOrderSingle, ExecutionReport, etc)
        //Calls your user-defined onMessage(subtype) callback, if defined. If not defined, it throws an UnsupportedMessageType exception and your app will automatically send a BusinessMessageReject (35=j) to the counterparty.
    }


    @Override
    public void onMessage(quickfix.fix42.ExecutionReport message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        super.onMessage(message, sessionID);
        System.out.println("Received Execution report from server");
        System.out.println("Order Id : " + message.getOrderID().getValue());
        System.out.println("Order Status : " + message.getOrdStatus().getValue());
        System.out.println("Order Price : " + message.getPrice().getValue());
    }


}


//summary of event methods:
//onCreate, onLogon, onLogout, toAdmin, fromAdmin, toApp, fromApp




//Quick fix has library for each type of fix message.