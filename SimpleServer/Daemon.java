import java.util.*;
/**
 * Write a description of class Daemon here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Daemon
{
    private static Vector<ServerThread> list= new Vector<ServerThread>(3);
    public Daemon()
    {
    }
    public static int addServer(ServerThread x) {
        list.add(x);
        MainClass.frame.setTitle("Server Console (" + list.size() + " clients connected)");
        return list.size()-1;
    }
    public static void removeServer(int x) {
        for (int i = x;i<list.size();i++)
            list.get(i).decrementThreadNumber();
        list.remove(x);
        MainClass.frame.setTitle("Server Console (" + list.size() + " clients connected)");
    }
    public static void sendMessage(String message) {
        for (ServerThread x: list)
            x.sendMessageToClient(message);
    }
}
