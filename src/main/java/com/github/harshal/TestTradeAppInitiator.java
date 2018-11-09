package com.github.harshal;

import quickfix.*;

/***
 * The Class TestTradeAppInitiator. (i.e client)
 * @author harshal n
 *
 * Every FIX application should have an implementation of Application interface, Application interface contains call back methods.
 * MessageCracker provides callback methods, for receiving messages from Server.
 */
public class TestTradeAppInitiator extends MessageCracker implements Application {

    /** (non-Javadoc)
     * @see quickfix.Application#onCreate(quickfix.SessionID)
     */
    
    public void onCreate(SessionID sessionId) {
        System.out.println("onCreate called when QFJ creates a new session"
                + sessionId);
    }

    /** (non-Javadoc)
     * @see quickfix.Application#onLogon(quickfix.SessionID)
     */
    
    public void onLogon(SessionID sessionId) {
        System.out.println("onLogon notifies you when a valid logon has been established with a counter party. This is called when a connection has been established and the FIX logon process has completed with both parties exchanging valid logon messages." + sessionId);
    }

    /** (non-Javadoc)
     * @see quickfix.Application#onLogout(quickfix.SessionID)
     */
    
    public void onLogout(SessionID sessionId) {
        System.out.println("onLogout notifies you when an FIX session is no longer online" + sessionId);
    }

    /** (non-Javadoc)
     * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
     */
    
    public void toAdmin(Message message, SessionID sessionId) {

        System.out.println("toAdmin provides you with a peek at the administrative messages that are being sent from your FIX engine to the counter party");
    }

    /** (non-Javadoc)
     * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
     */
    
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
            RejectLogon {

        System.out.println("fromAdmin has notified you when an administrative message is sent from a counterparty to your FIX engine for sessionId : "
                + sessionId);
    }


    /** (non-Javadoc)
     * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
     */
    
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        System.out.println("toApp is a callback, for application messages that are being sent to a counterparty. Acceptor requests them to be sent again. example: resend MTM order");
        System.out.println("Message : " + message + " for sessionid : " + sessionId);

    }

    /*** (non-Javadoc)
     * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
     */
    
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
            UnsupportedMessageType {

        System.out.println("fromApp is one of the core entry points for your FIX application. Every application level request will come through here from counterparty. Hence reception happens here"
                +sessionId );
    }
}


//summary of event methods:
//onCreate, onLogon, onLogout, toAdmin, fromAdmin, toApp, fromApp



//Here admin, App denote the counterparty /acceptor.