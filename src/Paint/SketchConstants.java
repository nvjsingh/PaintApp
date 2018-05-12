package Paint;

enum ShapeType {
    SHAPE_TYPE_FREEHAND,
    SHAPE_TYPE_STRAIGHTLINE,
    SHAPE_TYPE_RECTANGLE,
    SHAPE_TYPE_ELLIPSE,
    SHAPE_TYPE_SQUARE,
    SHAPE_TYPE_CIRCLE,
    SHAPE_TYPE_POLYGON,
    SHAPE_TYPE_CPOLYGON,
    SHAPE_TYPE_INVALID
}

enum SketchAppState {
    APP_STATE_DRAW,
    APP_STATE_SELECT,
    APP_STATE_MOVE,
    /*APP_STATE_DRAW_STRAIGHTLINE,
    APP_STATE_DRAW_RECTANGLE,
    APP_STATE_DRAW_SQUARE,
    APP_STATE_DRAW_ELLIPSE,
    APP_STATE_DRAW_CIRCLE,
    APP_STATE_DRAW_POLYGON,*/
    APP_STATE_INVALID
}

enum SketchAppEvent {
    EVENT_BUTN_FREEHAND,
    EVENT_BUTN_STRAIGHTLINE,
    EVENT_BUTN_RECTANGLE,
    EVENT_BUTN_SQUARE,
    EVENT_BUTN_ELLIPSE,
    EVENT_BUTN_CIRCLE,
    EVENT_BUTN_POLYGON,
    EVENT_BUTN_CPOLYGON,
    EVENT_BUTN_SELECT,
    EVENT_BUTN_GROUP,
    EVENT_BUTN_UNGROUP,
    EVENT_BUTN_CUT,
    EVENT_BUTN_COPY,
    EVENT_BUTN_PASTE,
    EVENT_BUTN_DELETE,
    EVENT_BUTN_UNDO,
    EVENT_BUTN_REDO,
    EVENT_BUTN_CLEAR,
    EVENT_BUTN_SAVE,
    EVENT_BUTN_LOAD,
    EVENT_ENABLE_LOGGING,
    
    EVENT_MOUSE_MOVED,
    EVENT_MOUSE_DRAGGED,
    EVENT_MOUSE_CLICKED,
    EVENT_MOUSE_ENTERED,
    EVENT_MOUSE_EXITED,
    EVENT_MOUSE_PRESSED,
    EVENT_MOUSE_RELEASED,

    EVENT_INVALID
}

enum SketchActivities {
    ACTIVITY_NEW,
    ACTIVITY_MOVE,
    ACTIVITY_DELETE,
    ACTIVITY_EDIT,
    ACTIVITY_INVALID
}

public class SketchConstants {
    public static int SKETCH_LINE         = 0;
    public static int SKETCH_STRAIGHTLINE = 1;
    public static int SKETCH_RECTANGLE    = 2;
    public static int SKETCH_SQUARE       = 3;
    public static int SKETCH_ELLIPSE      = 4;
    public static int SKETCH_CIRCLE       = 5;
    public static int SKETCH_POLYGON      = 6;
    public static int SKETCH_CPOLYGON     = 7;
    public static int SKETCH_MAX          = 8;
    
    public static int FRAME_WIDTH         = 2000;
    public static int FRAME_HEIGHT        = 1125;
    
    public static int selDelta = 4;
    public static int selCircleRad = 3;
    public static int selCircleDia = 2 * selCircleRad;
    
    public static String[] ShapeStr = {
            "FreeHand",
            "StraighLine",
            "Rectangle",
            "Square",
            "Ellipse",
            "Circle",
            "OpenPolygon",
            "ClosedPolygon"
    };
    
    private SketchConstants() {}
}