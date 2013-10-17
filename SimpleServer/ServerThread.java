import java.net.*;
import java.util.*;
import java.io.*;
/**
 * Write a description of class ServerThread here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ServerThread extends Thread
{
    private int threadNumber = 0;
    private String name = "";
    private boolean go = true;
    private static final char[] etxchar = {3};
    private static final String ETX = new String(etxchar);
    Socket x;
    InputStream socketIn;
    OutputStream socketOut;
    Scanner scanner;
    public ServerThread(Socket socket) {
        try {
            x = socket;
            socketIn = x.getInputStream();
            socketOut = x.getOutputStream();
            scanner = new Scanner(socketIn);
            scanner.useDelimiter(ETX);
            while (!scanner.hasNext()) {};
            name = scanner.next();
            System.out.println(name + " connected!");
        } catch (IOException e) {
            go = false;
        }
    }

    public void decrementThreadNumber() {
        threadNumber--;
    }
    
    public void setThreadNumber(int x) {
        threadNumber = x;
    }
    
    public int getThreadNumber() {
        return threadNumber;
    }
    
    public void stopServer() {
        go = false;
    }

    public void sendMessageToClient(String message) {
        try {
            socketOut.write((message+ETX).getBytes());
        } catch (IOException e) {}
    }

    public void sendMessageToEveryone(String message) {
        Daemon.sendMessage(message);
    }
    
    @Override
    public void run() {
        sendMessageToEveryone(name + " connected!");
        while (go) {
            if (scanner.hasNext()) {
                String message = name + " said: " + scanner.next();
                sendMessageToEveryone(message);
                System.out.println(message);
            }
            if (x.isClosed()) {
                go = false;
                sendMessageToEveryone(name + " disconnected!");
                System.out.println(name + " disconnected!");
                Daemon.removeServer(threadNumber);
            }
        }
    }
}
