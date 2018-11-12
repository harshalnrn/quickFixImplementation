package com.github.harshal;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix42.Logon;
import quickfix.fix42.NewOrderSingle;

import java.io.IOException;
import java.util.Scanner;

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
            while(!Session.lookupSession(sessionId).isLoggedOn()){
                System.out.println("Waiting for login success");
                Thread.sleep(1000);
                System.out.println("Logged In...");
            }
            sendLogonRequest(sessionId);
            Thread.sleep(5000);

            bookSingleOrder(sessionId);

          /*  System.out.println("Type to quit");
            Scanner scanner = new Scanner(System.in);
            scanner.next();
            Session.lookupSession(sessionId).disconnect("Done",false);*/
            socketInitiator.stop();


        } catch (ConfigError e) {
            e.printStackTrace();
        }
        catch(SessionNotFound e){
            e.printStackTrace();
        }
        catch (InterruptedException e) {
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
        boolean sent = Session.sendToTarget(logon, sessionId); // note : toApp gets called from sendToTarget method of Session class

        //Here we create a real FIX message for quickfix to send it.
        // toApp is an event handler that gets called when a message is sent. If you put a call to quickfix.Session.sendToTarget, it will then call toApp again when it gets sent
    }

    private static void bookSingleOrder(SessionID sessionID){
        //In real world this won't be a hardcoded value rather than a sequence.
        ClOrdID orderId = new ClOrdID("1");
        //to be executed on the exchange
        HandlInst instruction = new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE_NO_BROKER_INTERVENTION);
        //Since its FX currency pair name
        Symbol mainCurrency = new Symbol("EUR/USD");
        //Which side buy, sell
        Side side = new Side(Side.BUY);
        //Time of transaction
        TransactTime transactionTime = new TransactTime();
        //Type of our order, here we are assuming this is being executed on the exchange
        OrdType orderType = new OrdType(OrdType.FOREX_MARKET);
        NewOrderSingle newOrderSingle = new NewOrderSingle(orderId,instruction,mainCurrency, side, transactionTime,orderType);
        //Quantity
        newOrderSingle.set(new OrderQty(100));
        try {
            Session.sendToTarget(newOrderSingle, sessionID);
        } catch (SessionNotFound e) {
            e.printStackTrace();
        }
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