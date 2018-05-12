package Paint;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.AWTEvent;
import java.awt.Graphics2D;
import java.awt.Point;

public interface ISketch {
    // TODO: Choose appropriate value for color
    public static final int color = 0;
    public static final int stroke = 5;
    public static final SketchApp canvas = SketchApp.GetSketchApp();
    //public static final Graphics2D g2d = canvas.GetGraphicInstance();
    
    public void Draw(SketchAppEvent skEv, AWTEvent ev);
    public void Edit(IShape shape);
}

class SketchFreeHand implements ISketch {
    private FreeHand fHand = null;
    private Point startP = new Point(0, 0);
    @Override
    public void Draw(SketchAppEvent skEv, AWTEvent ev) {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        if (null == g2d) {
            System.out.print("[DBG] g2d is null\n");
            return;
        }
        MouseEvent mEv = (MouseEvent)ev;
        switch(skEv) {
        case EVENT_MOUSE_PRESSED:
            fHand = (FreeHand)SketchApp.GetSketchApp().CreateNewShape(
                    ShapeType.SHAPE_TYPE_FREEHAND, false);
            startP.setLocation(mEv.getX(), mEv.getY());
            fHand.SetLine(startP, startP);
            break;
        case EVENT_MOUSE_DRAGGED:
            Point p = new Point(mEv.getX(), mEv.getY());
            fHand.SetLine(startP, p);
            startP.setLocation(p);
            SketchApp.GetSketchApp().RePaint();
            break;
        case EVENT_MOUSE_RELEASED:
            ArrayList<IShape> logArr = new ArrayList<IShape>();
            logArr.add(fHand);
            SketchApp.GetSketchApp().LogActivity(SketchActivities.ACTIVITY_NEW,
                    null, null, logArr, null);
            break;
        default:
            break;
        }
    }

    @Override
    public void Edit(IShape shape) {
        //Future Implementation
    }
}

class SketchStLine implements ISketch {
    private StraightLine stLine = null;
    private Point startP = new Point(0, 0);
    @Override
    public void Draw(SketchAppEvent skEv, AWTEvent ev) {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        if (null == g2d) {
            System.out.print("[DBG] g2d is null\n");
            return;
        }
        MouseEvent mEv = (MouseEvent)ev;
        switch(skEv) {
        case EVENT_MOUSE_PRESSED:
            stLine = (StraightLine)SketchApp.GetSketchApp().CreateNewShape(
                    ShapeType.SHAPE_TYPE_STRAIGHTLINE, false);
            startP.setLocation(mEv.getX(), mEv.getY());
            stLine.SetLine(startP, startP);
            break;
        case EVENT_MOUSE_DRAGGED:
            stLine.SetLine(startP, new Point(mEv.getX(), mEv.getY()));
            SketchApp.GetSketchApp().RePaint();
            break;
        case EVENT_MOUSE_RELEASED:
            stLine.SetLine(startP, new Point(mEv.getX(), mEv.getY()));
            SketchApp.GetSketchApp().RePaint();
            
            ArrayList<IShape> logArr = new ArrayList<IShape>();
            logArr.add(stLine);
            SketchApp.GetSketchApp().LogActivity(SketchActivities.ACTIVITY_NEW,
                    null, null, logArr, null);
        default:
            break;
        }
    }

    @Override
    public void Edit(IShape shape) {
        //Future Implementation
    }
}

class SketchRect implements ISketch{
    private Rectangle shape = null;
    private Point startP = new Point(0, 0);
    @Override
    public void Draw(SketchAppEvent skEv, AWTEvent ev) {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        if (null == g2d) {
            System.out.print("[DBG] g2d is null\n");
            return;
        }
        MouseEvent mEv = (MouseEvent)ev;
        switch(skEv) {
        case EVENT_MOUSE_PRESSED:
            shape = (Rectangle)SketchApp.GetSketchApp().CreateNewShape(
                    ShapeType.SHAPE_TYPE_RECTANGLE, false);
            startP.setLocation(mEv.getX(), mEv.getY());
            shape.SetShape(startP, startP);
            break;
        case EVENT_MOUSE_DRAGGED:
            shape.SetShape(startP, new Point(mEv.getX(), mEv.getY()));
            SketchApp.GetSketchApp().RePaint();
            break;
        case EVENT_MOUSE_RELEASED:
            shape.SetShape(startP, new Point(mEv.getX(), mEv.getY()));
            SketchApp.GetSketchApp().RePaint();
            
            ArrayList<IShape> logArr = new ArrayList<IShape>();
            logArr.add(shape);
            SketchApp.GetSketchApp().LogActivity(SketchActivities.ACTIVITY_NEW,
                    null, null, logArr, null);
        default:
            break;
        }
    }

    @Override
    public void Edit(IShape shape) {
        //Future implementation
    }
}

class SketchEllipse implements ISketch{
    private Ellipse shape = null;
    private Point startP = new Point(0, 0);
    @Override
    public void Draw(SketchAppEvent skEv, AWTEvent ev) {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        if (null == g2d) {
            System.out.print("[DBG] g2d is null\n");
            return;
        }
        MouseEvent mEv = (MouseEvent)ev;
        switch(skEv) {
        case EVENT_MOUSE_PRESSED:
            shape = (Ellipse)SketchApp.GetSketchApp().CreateNewShape(
                    ShapeType.SHAPE_TYPE_ELLIPSE, false);
            startP.setLocation(mEv.getX(), mEv.getY());
            shape.SetShape(startP, startP);
            break;
        case EVENT_MOUSE_DRAGGED:
            shape.SetShape(startP, new Point(mEv.getX(), mEv.getY()));
            SketchApp.GetSketchApp().RePaint();
            break;
        case EVENT_MOUSE_RELEASED:
            shape.SetShape(startP, new Point(mEv.getX(), mEv.getY()));
            SketchApp.GetSketchApp().RePaint();
            
            ArrayList<IShape> logArr = new ArrayList<IShape>();
            logArr.add(shape);
            SketchApp.GetSketchApp().LogActivity(SketchActivities.ACTIVITY_NEW,
                    null, null, logArr, null);
        default:
            break;
        }
    }

    @Override
    public void Edit(IShape shape) {
        //Future implementation
    }
}

class SketchSquare implements ISketch{
    private Square shape = null;
    private Point startP = new Point(0, 0);
    @Override
    public void Draw(SketchAppEvent skEv, AWTEvent ev) {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        if (null == g2d) {
            System.out.print("[DBG] g2d is null\n");
            return;
        }
        MouseEvent mEv = (MouseEvent)ev;
        switch(skEv) {
        case EVENT_MOUSE_PRESSED:
            shape = (Square)SketchApp.GetSketchApp().CreateNewShape(
                    ShapeType.SHAPE_TYPE_SQUARE, false);
            startP.setLocation(mEv.getX(), mEv.getY());
            shape.SetShape(startP, startP);
            break;
        case EVENT_MOUSE_DRAGGED:
            shape.SetShape(startP, new Point(mEv.getX(), mEv.getY()));
            SketchApp.GetSketchApp().RePaint();
            break;
        case EVENT_MOUSE_RELEASED:
            shape.SetShape(startP, new Point(mEv.getX(), mEv.getY()));
            SketchApp.GetSketchApp().RePaint();
            
            ArrayList<IShape> logArr = new ArrayList<IShape>();
            logArr.add(shape);
            SketchApp.GetSketchApp().LogActivity(SketchActivities.ACTIVITY_NEW,
                    null, null, logArr, null);
        default:
            break;
        }
    }

    @Override
    public void Edit(IShape shape) {
        //Future implementation
    }
}

class SketchCircle implements ISketch{
    private Circle shape = null;
    private Point startP = new Point(0, 0);
    @Override
    public void Draw(SketchAppEvent skEv, AWTEvent ev) {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        if (null == g2d) {
            System.out.print("[DBG] g2d is null\n");
            return;
        }
        MouseEvent mEv = (MouseEvent)ev;
        switch(skEv) {
        case EVENT_MOUSE_PRESSED:
            shape = (Circle)SketchApp.GetSketchApp().CreateNewShape(
                    ShapeType.SHAPE_TYPE_CIRCLE, false);
            startP.setLocation(mEv.getX(), mEv.getY());
            shape.SetShape(startP, startP);
            break;
        case EVENT_MOUSE_DRAGGED:
            shape.SetShape(startP, new Point(mEv.getX(), mEv.getY()));
            SketchApp.GetSketchApp().RePaint();
            break;
        case EVENT_MOUSE_RELEASED:
            shape.SetShape(startP, new Point(mEv.getX(), mEv.getY()));
            SketchApp.GetSketchApp().RePaint();
            
            ArrayList<IShape> logArr = new ArrayList<IShape>();
            logArr.add(shape);
            SketchApp.GetSketchApp().LogActivity(SketchActivities.ACTIVITY_NEW,
                    null, null, logArr, null);
        default:
            break;
        }
    }

    @Override
    public void Edit(IShape shape) {
        //Future implementation
    }
}

class SketchPolygon implements ISketch {
    private PolygonSh poly = null;
    private Point startP = new Point(0, 0);
    private boolean isDrawing = false;
    private boolean isOpen = true;
    
    SketchPolygon(boolean b) {
        isOpen = b;
    }
    @Override
    public void Draw(SketchAppEvent skEv, AWTEvent ev) {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        if (null == g2d) {
            System.out.print("[DBG] g2d is null\n");
            return;
        }
        ShapeType type = ShapeType.SHAPE_TYPE_POLYGON;
        if (false == isOpen) {
            type = ShapeType.SHAPE_TYPE_CPOLYGON;
        }
        MouseEvent mEv = (MouseEvent)ev;
        switch(skEv) {
        case EVENT_MOUSE_PRESSED:
            if (false == isDrawing) {
                poly = (PolygonSh)SketchApp.GetSketchApp().CreateNewShape(
                        type, false);
                isDrawing = true;
                startP.setLocation(mEv.getX(), mEv.getY());
                poly.SetLine(startP, startP);
            }
            break;
        case EVENT_MOUSE_DRAGGED:
            poly.SetLine(startP, new Point(mEv.getX(), mEv.getY()));
            SketchApp.GetSketchApp().RePaint();
            break;
        case EVENT_MOUSE_MOVED:
            if (true == isDrawing) {
                poly.SetLine(startP, new Point(mEv.getX(), mEv.getY()));
                SketchApp.GetSketchApp().RePaint();
            }
            break;
        case EVENT_MOUSE_RELEASED:
            if (MouseEvent.BUTTON3 == mEv.getButton()) {
                poly.SetLine(new Point(0, 0), new Point(0, 0));
                poly.FinishShape();
                SketchApp.GetSketchApp().RePaint();
                isDrawing = false;
                
                ArrayList<IShape> logArr = new ArrayList<IShape>();
                logArr.add(poly);
                SketchApp.GetSketchApp().LogActivity(SketchActivities.ACTIVITY_NEW,
                        null, null, logArr, null);
                break;
            }
            if (startP.getX() == mEv.getX() && startP.getY() == mEv.getY()) {
                break;
            }
            poly.SetLine(startP, new Point(mEv.getX(), mEv.getY()));
            poly.FinishSide();
            startP.setLocation(mEv.getX(), mEv.getY());
            poly.SetLine(startP, startP);
            SketchApp.GetSketchApp().RePaint();
            break;
        default:
            break;
        }
    }

    @Override
    public void Edit(IShape shape) {
        //Future Implementation
    }
}