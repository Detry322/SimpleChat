import java.net.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
/**
 * Write a description of class MainClass here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MainClass
{
    private static JTextArea console = new JTextArea();
    public static JFrame frame;
    private static boolean couldConnect = true;
    private static final int PORT = 19960;
    private static void updateTextArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    console.append(text);
                    console.setCaretPosition(console.getDocument().getLength());

                }
            });
    }

    private static void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    updateTextArea(String.valueOf((char) b));
                }

                @Override
                public void write(byte[] b, int off, int len) throws IOException {
                    updateTextArea(new String(b, off, len));
                }

                @Override
                public void write(byte[] b) throws IOException {
                    write(b, 0, b.length);
                }
            };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    public static void main(String[] args) {
        frame = new JFrame("Server console");
        console.setEditable(false);
        console.setFont(new Font("Courier", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(console); 
        frame.getContentPane().add(scrollPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize( 700 , 300 );
        frame.setLocation( 100, 100 );
        frame.setVisible( true );
        redirectSystemStreams();
        try {
            ServerSocket x = new ServerSocket(PORT);
            System.out.println("Listening on port " + PORT + "...");
            while (couldConnect) {
                Socket socket = x.accept();
                ServerThread p = new ServerThread(socket);
                p.start();
                p.setThreadNumber(Daemon.addServer(p));
            }
        } catch (IOException e) {
            System.out.println("Couldn't bind to port " + PORT + ".");
            couldConnect = false;
        }
    }
}
