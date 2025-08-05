package Client;

import Common.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientMain {
    private static ObjectOutputStream out;
    private static ObjectInputStream  in;
    private static Socket socket;
    private static String name = "anon";
    private static int myId = -1;
    private static boolean isDrawer = false;

    public static void main(String[] args) {
        // Launch UI
        javax.swing.SwingUtilities.invokeLater(ClientUI::new);
    }

    public static void connect(String host, int port, String setName) {
        try {
            name = setName;
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in  = new ObjectInputStream(socket.getInputStream());
            new ClientReader(in).start();

            // send initial name
            send(new Payload(PayloadType.NAME, name));
        } catch (Exception e) {
            System.out.println("[SYSTEM] Failed to connect: " + e.getMessage());
        }
    }

    public static void send(Payload p) {
        try {
            if (out != null) {
                out.writeObject(p);
                out.flush();
            }
        } catch (Exception e) {
            System.out.println("[SYSTEM] Send failed: " + e.getMessage());
        }
    }

    // helpers used by PayloadHandler
    public static void setMyId(int id){ myId = id; }
    public static int getMyId(){ return myId; }
    public static void setIsDrawer(boolean b){ isDrawer = b; }
    public static boolean isDrawer(){ return isDrawer; }
}
