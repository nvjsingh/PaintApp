package Paint;

import java.io.Serializable;
import java.util.ArrayList;

class State implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<IShape> oldShapes = null;
    private ArrayList<IShape> newShapes = null;
    private GroupShape oldGroup = null;
    private GroupShape newGroup = null;
    
    State () {
        
    }
    
    State (ArrayList<IShape> prevSh, GroupShape prevGrp,
            ArrayList<IShape> nextSh, GroupShape nextGrp) {
        SetOldShapes(prevSh);
        SetNewShapes(nextSh);
        SetOldGroup(prevGrp);
        SetNewGroup(nextGrp);
    }
    
    public void SetOldShapes (ArrayList<IShape> oldS) {
        if (null == oldS) {
            return;
        }
        oldShapes = new ArrayList<IShape>();
        for (IShape shapeL : oldS) {
            IShape sh = CreateShapeClone(shapeL);
            oldShapes.add(sh);
        }
    }
    
    public void SetNewShapes (ArrayList<IShape> newS) {
        if (null == newS) {
            return;
        }
        newShapes = new ArrayList<IShape>();
        for (IShape shapeL : newS) {
            IShape sh = CreateShapeClone(shapeL);
            newShapes.add(sh);
        }
    }
    
    public ArrayList<IShape> GetOldShapes () {
        return oldShapes;
    }
    
    public ArrayList<IShape> GetNewShapes () {
        return newShapes;
    }

    public void SetOldGroup(GroupShape oldG) {
        if (null == oldG) {
            return;
        }
        oldGroup = CreateGroupClone(oldG);
    }

    public void SetNewGroup(GroupShape newG) {
        if (null == newG) {
            return;
        }
        newGroup = CreateGroupClone(newG);
    }

    public GroupShape GetOldGroup() {
        return oldGroup;
    }

    public GroupShape GetNewGroup() {
        return newGroup;
    }
    
    public IShape CreateShapeClone(IShape copyFrom) {
        IShape toShape = null;
        int id = copyFrom.GetShapeID();
        switch (copyFrom.GetShapeType()) {
        case SHAPE_TYPE_FREEHAND:
            toShape = new FreeHand(id);
            break;
        case SHAPE_TYPE_STRAIGHTLINE:
            toShape = new StraightLine(id);
            break;
        case SHAPE_TYPE_RECTANGLE:
            toShape = new Rectangle(id);
            break;
        case SHAPE_TYPE_ELLIPSE:
            toShape = new Ellipse(id);
            break;
        case SHAPE_TYPE_SQUARE:
            toShape = new Square(id);
            break;
        case SHAPE_TYPE_CIRCLE:
            toShape = new Circle(id);
            break;
        case SHAPE_TYPE_POLYGON:
            toShape = new PolygonSh(id);
            break;
        default:
            break;
        }
        toShape.CopyProperties(copyFrom);
        return toShape;
    }
    
    public GroupShape CreateGroupClone(GroupShape groupFrom) {
        GroupShape grp = new GroupShape(groupFrom.GetGrpId(), groupFrom.GetMembers());
        return grp;
    }
}

public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;
    private SketchActivities type = SketchActivities.ACTIVITY_INVALID;
    private State actState = null;
    
    public Activity(SketchActivities actType, ArrayList<IShape> prevSh, GroupShape prevGrp,
            ArrayList<IShape> nextSh, GroupShape nextGrp) {
        type = actType;
        actState = new State(prevSh, prevGrp, nextSh, nextGrp);
    }
    
    public Activity(SketchActivities actType, State act) {
        type = actType;
        actState = act;
    }
    
    public void SetState(State s) {
        actState = s;
    }
    
    public State GetState () {
        return actState;
    }
    
    public SketchActivities GetActivityType() {
        return type;
    }
}
