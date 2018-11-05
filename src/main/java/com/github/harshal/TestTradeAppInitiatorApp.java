package com.github.harshal;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.MessageFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

public class TestTradeAppInitiatorApp {

    /**
     * The main method.
     *
     * @param args
     *            the args
     */

    public static void main(String[] args) {
        SocketInitiator socketInitiator = null;
        try {
            SessionSettings initiatorSettings = new SessionSettings(
                    "D:/quickFixImplementation/src/main/resources/initiatorSettings.txt");
            Application initiatorApplication = new TestTradeAppInitiator();
            FileStoreFactory fileStoreFactory = new FileStoreFactory(
                    initiatorSettings);
            FileLogFactory fileLogFactory = new FileLogFactory(
                    initiatorSettings);
            MessageFactory messageFactory = new DefaultMessageFactory();
            socketInitiator = new SocketInitiator(initiatorApplication, fileStoreFactory, initiatorSettings, fileLogFactory, messageFactory);
            socketInitiator.start();
            SessionID sessionId = socketInitiator.getSessions().get(0);
            Session.lookupSession(sessionId).logon();
        } catch (ConfigError e) {
            e.printStackTrace();
        }
    }

}
