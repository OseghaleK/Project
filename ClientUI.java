package Client;

import Common.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ClientUI extends JFrame {
    // Simple global-ish access used by PayloadHandler
    public static ClientUI instance;

    // Connection
    private JTextField hostField = new JTextField("127.0.0.1");
    private JTextField portField = new JTextField("5555");
    private JTextField nameField = new JTextField("anon");
    private JButton connectBtn = new JButton("Connect");

    // Ready/Away
    private JToggleButton readyBtn = new JToggleButton("Ready");
    private JToggleButton awayBtn  = new JToggleButton("Away");

    // User list table
    private DefaultTableModel userModel = new DefaultTableModel(new Object[]{"ID","Name","Pts","Drawer","Away","Spec"}, 0);
    private JTable userTable = new JTable(userModel);

    // Events panel
    private JTextArea events = new JTextArea(10, 40);

    // Game area
    private BoardPanel board = new BoardPanel(16, 10); // default; will resize on DIMENSION
    private JTextField guessField = new JTextField(15);
    private JButton guessBtn = new JButton("Guess");
    private JButton clearBtn = new JButton("Clear (drawer)");

    public ClientUI() {
        super("IT114 – Drawing (MS3)");
        instance = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top: Connection + Details
        JPanel top = new JPanel(new GridLayout(2,1));
        JPanel conn = new JPanel(new FlowLayout(FlowLayout.LEFT));
        conn.add(new JLabel("Host:")); conn.add(hostField);
        conn.add(new JLabel("Port:")); conn.add(portField);
        conn.add(new JLabel("Name:")); conn.add(nameField);
        conn.add(connectBtn);
        top.add(conn);

        JPanel ready = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ready.add(readyBtn);
        ready.add(awayBtn);
        top.add(ready);
        add(top, BorderLayout.NORTH);

        // Center: board + right side lists
        JPanel center = new JPanel(new BorderLayout());
        center.add(board, BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout());
        userTable.setFillsViewportHeight(true);
        right.add(new JScrollPane(userTable), BorderLayout.CENTER);

        events.setEditable(false);
        right.add(new JScrollPane(events), BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);
        add(right, BorderLayout.EAST);

        // Bottom: guessing + clear
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(new JLabel("Guess:"));
        bottom.add(guessField);
        bottom.add(guessBtn);
        bottom.add(clearBtn);
        add(bottom, BorderLayout.SOUTH);

        // Wire actions
        connectBtn.addActionListener(e -> doConnect());
        readyBtn.addActionListener(e -> sendReady(readyBtn.isSelected()));
        awayBtn.addActionListener(e -> sendAway(awayBtn.isSelected()));
        guessBtn.addActionListener(e -> doGuess());
        clearBtn.addActionListener(e -> doClear());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void doConnect() {
        String host = hostField.getText().trim();
        int port = Integer.parseInt(portField.getText().trim());
        String nm = nameField.getText().trim();
        ClientMain.connect(host, port, nm);
        ClientMain.send(new Payload(PayloadType.NAME, nm));
        appendEvent("[UI] Connecting as " + nm + " to " + host + ":" + port);
    }

    private void sendReady(boolean val) {
        ClientMain.send(new Payload(PayloadType.READY, Boolean.toString(val)));
        appendEvent("[UI] Ready=" + val);
    }

    private void sendAway(boolean val) {
        ClientMain.send(new Payload(PayloadType.AWAY, Boolean.toString(val)));
        appendEvent("[UI] Away=" + val);
    }

    private void doGuess() {
        String g = guessField.getText().trim();
        if (g.isEmpty()) return;
        ClientMain.send(new Payload(PayloadType.GUESS, g));
        guessField.setText("");
    }

    private void doClear() {
        ClientMain.send(new Payload(PayloadType.CLEAR_BOARD, "req"));
    }

    // ---- Called by PayloadHandler ----
    public void setBoardSize(int w, int h) {
        board.setSizeGrid(w,h);
        appendEvent("[SYS] Board set " + w + "x" + h);
        pack();
    }

    public void applyDraw(int x, int y, String color) {
        board.apply(x,y,color);
    }

    public void clearBoard() {
        board.clear();
    }

    public void setDrawer(boolean drawer) {
        board.setDrawer(drawer);
    }

    public void updateUserList(List<UserInfo> users) {
        userModel.setRowCount(0);
        for (UserInfo u : users) {
            userModel.addRow(new Object[]{
                u.getId(), u.getName(), u.getPoints(),
                u.isDrawer(), u.isAway(), u.isSpectator()
            });
        }
    }

    public void appendEvent(String msg) {
        events.append(msg + "\n");
        events.setCaretPosition(events.getDocument().getLength());
    }
}
