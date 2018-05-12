package Paint;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

/**
 * Listen to all mouse events.
 * Check the state and delegate event
 */
public class MouseEveListener extends MouseInputAdapter {
    public void mouseMoved(MouseEvent e) {
        StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_MOUSE_MOVED, e);
    }

    public void mouseDragged(MouseEvent e) {
        StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_MOUSE_DRAGGED, e);
    }

    public void mouseClicked(MouseEvent e) {
        StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_MOUSE_CLICKED, e);
    }

    public void mouseEntered(MouseEvent e) {
        StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_MOUSE_ENTERED, e);
    }

    public void mouseExited(MouseEvent e) {
        StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_MOUSE_EXITED, e);
    }

    public void mousePressed(MouseEvent e) {
        StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_MOUSE_PRESSED, e);
    }

    public void mouseReleased(MouseEvent e) {
        StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_MOUSE_RELEASED, e);
    }
}

class KeyEveListener implements KeyListener {
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown()) {
            //SketchApp.GetSketchApp().SetCtrlPressed(true);
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        if (!e.isControlDown()) {
            //SketchApp.GetSketchApp().SetCtrlPressed(false);
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    
}