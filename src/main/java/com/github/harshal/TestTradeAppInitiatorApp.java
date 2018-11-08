package com.github.harshal;
import quickfix.*;
import quickfix.field.BeginString;
import quickfix.field.EncryptMethod;
import quickfix.field.HeartBtInt;
import quickfix.field.ResetSeqNumFlag;
import quickfix.fix42.Logon;

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
            //
            socketInitiator = new SocketInitiator(initiatorApplication, fileStoreFactory, initiatorSettings, fileLogFactory, messageFactory);
            System.out.println("before starting of session on initiator socket");
            socketInitiator.start(); // this establishes session and connects to acceptor (server)
            SessionID sessionId = socketInitiator.getSessions().get(0);
            System.out.println("session id is " +sessionId);

            Session.lookupSession(sessionId).logon();
            sendLogonRequest(sessionId);

        } catch (ConfigError e) {
            e.printStackTrace();
        }
        catch(SessionNotFound e){
            e.printStackTrace();
        }
    }


    private static void sendLogonRequest(SessionID sessionId)
            throws SessionNotFound {
        Logon logon = new Logon();
        Message.Header header = logon.getHeader();
        header.setField(new BeginString("FIX.4.2"));
        logon.set(new HeartBtInt(30));
        logon.set(new ResetSeqNumFlag(true));
        logon.set(new EncryptMethod(0));
        boolean sent = Session.sendToTarget(logon, sessionId); // note : toApp gets called from sendToTarget method
        System.out.println("Response received on call method for Logon Message Sent : " + sent);
        //Here we create a real FIX message for quickfix to send it.
        // toApp is an event handler that gets called when a message is sent. If you put a call to quickfix.Session.sendToTarget, it will then call toApp again when it gets sent
    }



}
//note: Quicfix identifies an unique session by BeginString, SenderCompID, TargetCompID.
//example: [FIX.4.2:MY-INITIATOR-SERVICE->MY-ACCEPTOR-SERVICE]
/*
My understanding is that I just need to start the SocketInitiator to connect to the third party server/acceptor
and send the messages using send() method in session. When server sends the message back,
I will get the callback in the Application class toApp() method.*/


/*
Initiator : The party which initiates the connection to the remote FIX server (always using an IP address and a port number).
        Acceptor: The party which runs the FIX process and monitors a particular port for incoming FIX connections.*/


//IMP: SocketConnectport=SocketAcceptport