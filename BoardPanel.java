+package Client;

import Common.CoordPayload;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/** Simple grid drawing panel. Left click to paint if you are the drawer. */
public class BoardPanel extends JPanel {
    private int cols, rows;
    private char[][] grid; // ' ' blank, 'X' black (MS3 Phase1 keeps one color)
    private boolean isDrawer = false;

    private static final int CELL = 24;

    public BoardPanel(int cols, int rows) {
        setSizeGrid(cols, rows);
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (!isDrawer) return;
                int x = e.getX() / CELL;
                int y = e.getY() / CELL;
                if (inBounds(x,y) && grid[y][x] != 'X') {
                    // One color in Phase 1
                    ClientMain.send(new CoordPayload(x,y,"black"));
                }
            }
        });
    }

    public void setSizeGrid(int cols, int rows) {
        this.cols = cols; this.rows = rows;
        grid = new char[rows][cols];
        for (char[] r: grid) Arrays.fill(r, ' ');
        setPreferredSize(new Dimension(cols*CELL, rows*CELL));
        revalidate(); repaint();
    }

    public void setDrawer(boolean drawer){ this.isDrawer = drawer; }

    public void apply(int x, int y, String color) {
        if (inBounds(x,y)) {
            grid[y][x] = 'X';
            repaint();
        }
    }

    public void clear() {
        for (char[] r: grid) Arrays.fill(r, ' ');
        repaint();
    }

    private boolean inBounds(int x,int y){ return y>=0 && y<rows && x>=0 && x<cols; }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // grid
        g.setColor(Color.LIGHT_GRAY);
        for (int i=0;i<=cols;i++) g.drawLine(i*CELL,0,i*CELL,rows*CELL);
        for (int j=0;j<=rows;j++) g.drawLine(0,j*CELL,cols*CELL,j*CELL);

        // cells
        for (int y=0;y<rows;y++){
            for(int x=0;x<cols;x++){
                if (grid[y][x]=='X') {
                    g.setColor(Color.BLACK);
                    g.fillRect(x*CELL+1, y*CELL+1, CELL-1, CELL-1);
                }
            }
        }
    }
}
