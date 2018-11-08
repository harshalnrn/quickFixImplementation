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
        System.out.println("Successfully called onCreate for sessionId : "
                + sessionId);
    }

    /** (non-Javadoc)
     * @see quickfix.Application#onLogon(quickfix.SessionID)
     */
    
    public void onLogon(SessionID sessionId) {
        System.out.println("Successfully logged on for sessionId : " + sessionId);
    }

    /** (non-Javadoc)
     * @see quickfix.Application#onLogout(quickfix.SessionID)
     */
    
    public void onLogout(SessionID sessionId) {
        System.out.println("Successfully logged out for sessionId : " + sessionId);
    }

    /** (non-Javadoc)
     * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
     */
    
    public void toAdmin(Message message, SessionID sessionId) {
        System.out.println("Inside toAdmin method");
    }

    /** (non-Javadoc)
     * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
     */
    
    public void fromAdmin(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
            RejectLogon {
        System.out.println("Successfully called fromAdmin method for sessionId : "
                + sessionId);
    }


    /** (non-Javadoc)
     * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
     */
    
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        System.out.println("Message : " + message + " for sessionid : " + sessionId);
    }

    /*** (non-Javadoc)
     * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
     */
    
    public void fromApp(Message message, SessionID sessionId)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
            UnsupportedMessageType {

        System.out.println("Successfully called fromApp for sessionId : "
                +sessionId );
    }
}


//summary of event methods:
//onCreate, onLogon, onLogout, toAdmin, fromAdmin, toApp, fromApp