import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;

public class MainClass
{
    private static String name = "";
    private static JFrame frame;
    private static int ilovecake = 0;
    private static char[] etxarray = {3};
    private static String ETX = new String(etxarray);
    private static PipedInputStream pin = new PipedInputStream();  
    private static PipedOutputStream pout;
    private static OutputStream os;
    public static Scanner scanner;
    private static JTextArea console = new JTextArea();
    private static JTextField textField;
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

    public static void addStuffToContentPane(Container x) {
        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        pout.write((new String(textField.getText() + ETX)).getBytes());
                    } catch (Exception z) {}
                    if (ilovecake<2) {
                        System.out.println(textField.getText());
                        ilovecake++;
                    } else {
                        try {
                            os.write((scanner.next() + ETX).getBytes());
                        } catch (IOException x) {

                        }
                    }
                    textField.setText("");
                } 
            });
        console.setEditable(false);
        console.setFont(new Font("Courier", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(console); 
        scrollPane.setPreferredSize(new Dimension(700,frame.getHeight()-25));
        textField.setPreferredSize(new Dimension(700,25));
        x.add(scrollPane,BorderLayout.PAGE_START);
        x.add(textField,BorderLayout.PAGE_END);
    }

    public static void createAndShowGUI() {
        frame = new JFrame("Client console");
        frame.setSize( 700 , 300 );
        addStuffToContentPane(frame.getContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation( 100, 100 );
        frame.setVisible( true );
    }

    public static void main(String[] args) {
        try {
            pout = new PipedOutputStream(pin);

            createAndShowGUI();
            redirectSystemStreams();
            scanner = new Scanner(pin);
            scanner.useDelimiter(ETX);
            System.out.print("Enter a name: ");
            name = scanner.next();
            System.out.print("Enter an IP to connect to: ");
            final Socket socket = new Socket(scanner.next(),PORT);
            os = socket.getOutputStream();
            InputStream is = socket.getInputStream();
            Scanner otherpeople = (new Scanner(is)).useDelimiter(ETX);
            os.write((name + ETX).getBytes());
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

                        public void run() {
                            try {
                                os.;
                            } catch (IOException e) {
                            }
                        }

                    }));
            while (true) {
                System.out.println(otherpeople.next());
            }
        } catch (Exception e) {}
    }
}
