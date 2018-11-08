package com.github.harshal;
import quickfix.*;
import quickfix.field.BeginString;
import quickfix.field.HeartBtInt;
import quickfix.field.ResetSeqNumFlag;
import quickfix.fix40.Logon;

public class TestTradeAppAcceptorApp {

    /**
     * The main method.
     *
     * @param args
     *            the args
     */
    public static void main(String[] args) {
        SocketAcceptor socketAcceptor = null;
        try {
            SessionSettings executorSettings = new SessionSettings(
                    "D:/quickFixImplementation/src/main/resources/acceptorSettings.txt");
            Application application = new TestTradeAppAcceptor();
            FileStoreFactory fileStoreFactory = new FileStoreFactory(
                    executorSettings);
            MessageFactory messageFactory = new DefaultMessageFactory();
            FileLogFactory fileLogFactory = new FileLogFactory(executorSettings);

            socketAcceptor = new SocketAcceptor(application, fileStoreFactory,
                    executorSettings, fileLogFactory, messageFactory);
            //note 'SocketAcceptor' bean used by cceptor, to listen to sessions ,on its socket.
            //(Similar to other connections, Every connection identified by sessions)
            socketAcceptor.start();
        } catch (ConfigError e) {
            e.printStackTrace();
        }
    }



}
