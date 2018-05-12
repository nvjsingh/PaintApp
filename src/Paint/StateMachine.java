package Paint;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;

final class StateMachine {
    private static StateMachine self = null;
    private SketchAppState state = SketchAppState.APP_STATE_SELECT;
    private int drawType = SketchConstants.SKETCH_MAX;
    
    /**
     * Private constructor
     */
    private StateMachine() {
    }
    
    public static StateMachine GetStateMachine() {
        if(self == null) {
           self = new StateMachine();
        }
        return self;
     }
    
    public SketchAppState GetSketchState() {
        return state;
    }
    
    public void EventHandler(SketchAppEvent sketchEv, AWTEvent ev) {
        switch(sketchEv) {
        case EVENT_BUTN_FREEHAND:
            state = SketchAppState.APP_STATE_DRAW;
            drawType = SketchConstants.SKETCH_LINE;
            break;
        case EVENT_BUTN_STRAIGHTLINE:
            state = SketchAppState.APP_STATE_DRAW;
            drawType = SketchConstants.SKETCH_STRAIGHTLINE;
            break;
        case EVENT_BUTN_RECTANGLE:
            state = SketchAppState.APP_STATE_DRAW;
            drawType = SketchConstants.SKETCH_RECTANGLE;
            break;
        case EVENT_BUTN_SQUARE:
            state = SketchAppState.APP_STATE_DRAW;
            drawType = SketchConstants.SKETCH_SQUARE;
            break;
        case EVENT_BUTN_ELLIPSE:
            state = SketchAppState.APP_STATE_DRAW;
            drawType = SketchConstants.SKETCH_ELLIPSE;
            break;
        case EVENT_BUTN_CIRCLE:
            state = SketchAppState.APP_STATE_DRAW;
            drawType = SketchConstants.SKETCH_CIRCLE;
            break;
        case EVENT_BUTN_POLYGON:
            state = SketchAppState.APP_STATE_DRAW;
            drawType = SketchConstants.SKETCH_POLYGON;
            break;
        case EVENT_BUTN_CPOLYGON:
            state = SketchAppState.APP_STATE_DRAW;
            drawType = SketchConstants.SKETCH_CPOLYGON;
            break;
        case EVENT_BUTN_SELECT:
            state = SketchAppState.APP_STATE_SELECT;
            break;
        case EVENT_BUTN_GROUP:
            SketchApp.GetSketchApp().GroupShapes();
            break;
        case EVENT_BUTN_UNGROUP:
            SketchApp.GetSketchApp().Ungroup();
            break;
        case EVENT_BUTN_CUT:
            SketchApp.GetSketchApp().CutToClipboard();
            break;
        case EVENT_BUTN_COPY:
            SketchApp.GetSketchApp().CopyToClipboard();
            break;
        case EVENT_BUTN_PASTE:
            SketchApp.GetSketchApp().PasteFromClipboard();
            break;
        case EVENT_BUTN_DELETE:
            SketchApp.GetSketchApp().DeleteSelection(true);
            break;
        case EVENT_BUTN_UNDO:
            SketchApp.GetSketchApp().ApplyUndo();
            break;
        case EVENT_BUTN_REDO:
            SketchApp.GetSketchApp().ApplyRedo();
            break;
        case EVENT_BUTN_CLEAR:
            SketchApp.GetSketchApp().ClearAll();
            break;
        case EVENT_BUTN_SAVE:
            SketchApp.GetSketchApp().SaveSketch();
            break;
        case EVENT_BUTN_LOAD:
            SketchApp.GetSketchApp().LoadSketch();
            break;
            
        case EVENT_MOUSE_RELEASED:
            if (SketchAppState.APP_STATE_SELECT == state) {
                SketchApp.GetSketchApp().CheckSelection(sketchEv, ev);
                SketchApp.GetSketchApp().LogInFile(new Timestamp(System.currentTimeMillis()).toString() + " "
                        + ((MouseEvent)ev).getX() + " " + ((MouseEvent)ev).getX() + " "
                        + "[RLSD] " + sketchEv.toString());
                break;
            }
            if (SketchAppState.APP_STATE_MOVE == state) {
                SketchApp.GetSketchApp().MoveSelection(sketchEv, ev);
                state = SketchAppState.APP_STATE_SELECT;
                SketchApp.GetSketchApp().LogInFile(new Timestamp(System.currentTimeMillis()).toString() + " "
                        + ((MouseEvent)ev).getX() + " " + ((MouseEvent)ev).getX() + " "
                        + "[MOVE] " + sketchEv.toString());
                break;
            }
        case EVENT_MOUSE_PRESSED:
            if (SketchAppState.APP_STATE_SELECT == state) {
                SketchApp.GetSketchApp().MoveSelection(sketchEv, ev);
                SketchApp.GetSketchApp().LogInFile(new Timestamp(System.currentTimeMillis()).toString() + " "
                        + ((MouseEvent)ev).getX() + " " + ((MouseEvent)ev).getX() + " "
                        + "[MOVE] " + sketchEv.toString());
                break;
            }
        case EVENT_MOUSE_CLICKED:
        case EVENT_MOUSE_MOVED:
        case EVENT_MOUSE_DRAGGED:
            if (SketchAppState.APP_STATE_SELECT == state || 
                    SketchAppState.APP_STATE_MOVE == state) {
                if (SketchAppEvent.EVENT_MOUSE_MOVED != sketchEv) {
                    if (SketchApp.GetSketchApp().MoveSelection(sketchEv, ev)) {
                        state = SketchAppState.APP_STATE_MOVE;
                        SketchApp.GetSketchApp().LogInFile(new Timestamp(System.currentTimeMillis()).toString() + " "
                                + ((MouseEvent)ev).getX() + " " + ((MouseEvent)ev).getX() + " "
                                + "[MOVE] " + sketchEv.toString());
                    }
                }
            }
            if (SketchAppState.APP_STATE_DRAW == state) {
                SketchApp.GetSketchApp().GetSketch(drawType).Draw(sketchEv, ev);
                if (SketchAppEvent.EVENT_MOUSE_MOVED != sketchEv) {
                    SketchApp.GetSketchApp().LogInFile(new Timestamp(System.currentTimeMillis()).toString() + " "
                            + ((MouseEvent)ev).getX() + " " + ((MouseEvent)ev).getX() + " "
                            + "[DRAW] " + sketchEv.toString() + " "
                            + SketchConstants.ShapeStr[drawType]);
                }
            }
            ((MouseEvent)ev).consume();
            break;
        case EVENT_MOUSE_ENTERED:
            ((MouseEvent)ev).consume();
            break;
        case EVENT_MOUSE_EXITED:
            ((MouseEvent)ev).consume();
            break;
        default:
            break;
        }
    }
}