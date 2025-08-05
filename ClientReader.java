package Client;

import Common.Payload;

import java.io.ObjectInputStream;

public class ClientReader extends Thread {
    private final ObjectInputStream in;

    public ClientReader(ObjectInputStream in) {
        this.in = in;
        setDaemon(true);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object obj = in.readObject();
                if (!(obj instanceof Payload)) continue;
                Payload p = (Payload) obj;
                PayloadHandler.handle(p);
            }
        } catch (Exception e) {
            System.out.println("[CLIENT] Reader ended: " + e.getMessage());
        }
    }
}
