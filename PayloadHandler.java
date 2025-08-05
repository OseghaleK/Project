package Client;

import Common.*;

public class PayloadHandler {
    public static void handle(Payload p) {
        switch (p.getType()) {
            case ASSIGN_ID: {
                int id = Integer.parseInt(p.getMessage());
                ClientMain.setMyId(id);
                ClientUI.instance.appendEvent("[SYSTEM] Assigned id=" + id);
                break;
            }
            case SERVER_NOTIFICATION:
                ClientUI.instance.appendEvent("[SERVER] " + p.getMessage());
                break;

            case DIMENSION: {
                DimensionPayload dp = (DimensionPayload)p;
                ClientUI.instance.setBoardSize(dp.getWidth(), dp.getHeight());
                break;
            }
            case DRAW_SYNC: {
                CoordPayload cp = (CoordPayload)p;
                ClientUI.instance.applyDraw(cp.getX(), cp.getY(), cp.getColor());
                break;
            }
            case CLEAR_BOARD:
                ClientUI.instance.clearBoard();
                break;

            case USER_LIST: {
                UserListPayload ul = (UserListPayload)p;
                ClientUI.instance.updateUserList(ul.getUsers());
                break;
            }
            case ROUND_START: {
                RoundStartPayload rsp = (RoundStartPayload)p;
                boolean amDrawer = (rsp.getDrawerId() == ClientMain.getMyId());
                ClientMain.setIsDrawer(amDrawer);
                ClientUI.instance.setDrawer(amDrawer);
                ClientUI.instance.appendEvent("[ROUND] Drawer=" + rsp.getDrawerId() + "  Word: " + rsp.getBlanks());
                break;
            }
            case ROUND_END: {
                RoundEndPayload rep = (RoundEndPayload)p;
                ClientUI.instance.appendEvent("[ROUND] End. Word = " + rep.getWord());
                break;
            }
            case POINTS:
                ClientUI.instance.appendEvent("[SCORE] " + p.toString());
                break;

            case TIMER_TICK: {
                TimerPayload tp = (TimerPayload)p;
                ClientUI.instance.appendEvent("[TIMER] " + tp.getSeconds() + "s");
                break;
            }

            default:
                ClientUI.instance.appendEvent("[??] " + p);
        }
    }
}
