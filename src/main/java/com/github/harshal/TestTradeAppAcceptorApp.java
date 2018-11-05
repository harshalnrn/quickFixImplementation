package com.github.harshal;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.MessageFactory;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
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
            socketAcceptor.start();
        } catch (ConfigError e) {
            e.printStackTrace();
        }
    }
}
