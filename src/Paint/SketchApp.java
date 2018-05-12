package Paint;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class Clipboard {
    private ArrayList<IShape> shapeList = new ArrayList<IShape>();
    
    public ArrayList<IShape> GetShapeList() {
        return shapeList;
    }
    
    // TODO: confirm capacity of shapeList
    public void SetShapeList(ArrayList<IShape> list) {
        shapeList.clear();
        for (IShape sh : list) {
            IShape newSh = SketchApp.GetSketchApp().CreateNewShape(sh.GetShapeType(), true);
            newSh.CopyProperties(sh);
            newSh.SetIsSelected(true);
            newSh.SetGroup(sh.GetGroupID());
            shapeList.add(newSh);
        }
    }
    
    public void Clear() {
        shapeList.clear();
    }
}

public final class SketchApp extends JApplet {
    private static SketchApp self = null;
    private int shapeCntr = -1;
    private int grpCntr = -1;
    private Color currColor = Color.black;
    private Graphics2D g2d = null;
    private ISketch[] sketchList = new ISketch[SketchConstants.SKETCH_MAX];
    private ArrayList<IShape> shapeList = new ArrayList<IShape>();
    private ArrayList<IShape> SelectedList = new ArrayList<IShape>();
    private Clipboard clip = new Clipboard();
    private ArrayList<GroupShape> groupList = new ArrayList<GroupShape>();
    private ArrayList<Activity> undoList = new ArrayList<Activity>();
    private ArrayList<Activity> redoList = new ArrayList<Activity>();
    private MouseEveListener mouseListener = new MouseEveListener();
    private KeyEveListener keylistener = new KeyEveListener();
    private FileLogger fileLog = new FileLogger();
    private Point moveP = new Point();
    private FileRW filerw = new FileRW();
    private JPanel controlPanel = null;
    private JPanel leftMenu = null;
    private JCheckBox logCheck = null;
    private static JFrame sketchFrame = null;
    private State moveState = null;
    private JButton undoButton = null;
    private JButton redoButton = null;
    private boolean isMoveSel = false;
    private boolean islogEnabled = false;
    private static JTextField license = new JTextField("Icons used from https://icons8.com");
    
    /**
     * Private constructor
     */
    private SketchApp() {
        init();
    }
    
    public static SketchApp GetSketchApp() {
       if(self == null) {
          self = new SketchApp();
       }
       return self;
    }
    
    public void SetGraphicsInstance() {
        g2d = (Graphics2D) getGraphics();
    }
    
    public void init() {
        getContentPane().setBackground( Color.white );
        controlPanel = new JPanel();
        leftMenu = new JPanel();
        leftMenu.setLayout(new FlowLayout(FlowLayout.LEFT));
        leftMenu.setBackground(Color.white);
        controlPanel.setLayout(new GridLayout(16, 2));
        controlPanel.setBackground( Color.white );
        Dimension d = new Dimension(10,10);
        int height = 50;
        int width = 50;
        int clrheight = 30;
        int clrwidth =30;

        //final JButton lineBtn = new JButton("FreeHand");
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("images\\freehand.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        final JButton lineBtn = new JButton(new ImageIcon(dimg));
        lineBtn.setToolTipText("FreeHand");
        controlPanel.add(lineBtn);
        lineBtn.addActionListener(new ActionLineButton());
        lineBtn.setSize(d);
        
        try {
            img = ImageIO.read(new File("images\\line.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        //final JButton slBtn = new JButton("StraightLine");
        final JButton slBtn = new JButton(new ImageIcon(dimg));
        slBtn.setToolTipText("StraightLine");
        controlPanel.add(slBtn);
        slBtn.addActionListener(new ActionSlButton());
        slBtn.setSize(d);

        try {
            img = ImageIO.read(new File("images\\rectangle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        //final JButton rectBtn = new JButton("Rectangle");
        final JButton rectBtn = new JButton(new ImageIcon(dimg));
        rectBtn.setToolTipText("Rectangle");
        controlPanel.add(rectBtn);
        rectBtn.addActionListener(new ActionRectButton());
        rectBtn.setSize(d);

        try {
            img = ImageIO.read(new File("images\\square.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        //final JButton sqBtn = new JButton("Square");
        final JButton sqBtn = new JButton(new ImageIcon(dimg));
        sqBtn.setToolTipText("Square");
        controlPanel.add(sqBtn);
        sqBtn.addActionListener(new ActionSqButton());
        sqBtn.setSize(d);

        try {
            img = ImageIO.read(new File("images\\ellipse.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        //final JButton ellBtn = new JButton("Ellipse");
        final JButton ellBtn = new JButton(new ImageIcon(dimg));
        ellBtn.setToolTipText("Ellipse");
        controlPanel.add(ellBtn);
        ellBtn.addActionListener(new ActionEllButton());
        ellBtn.setSize(d);

        try {
            img = ImageIO.read(new File("images\\circle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        //final JButton circleBtn = new JButton("Circle");
        final JButton circleBtn = new JButton(new ImageIcon(dimg));
        circleBtn.setToolTipText("Circle");
        controlPanel.add(circleBtn);
        circleBtn.addActionListener(new ActionCircleButton());
        circleBtn.setSize(d);

        try {
            img = ImageIO.read(new File("images\\opolygon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        //final JButton polyButton = new JButton("OpenPolygon");
        final JButton polyButton = new JButton(new ImageIcon(dimg));
        polyButton.setToolTipText("OpenPolygon");
        controlPanel.add(polyButton);
        polyButton.addActionListener(new ActionPolyButton());
        polyButton.setSize(d);

        try {
            img = ImageIO.read(new File("images\\cpolygon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        //final JButton cPolyButton = new JButton("ClosedPolygon");
        final JButton cPolyButton = new JButton(new ImageIcon(dimg));
        cPolyButton.setToolTipText("ClosedPolygon");
        controlPanel.add(cPolyButton);
        cPolyButton.addActionListener(new ActionCPolyButton());
        cPolyButton.setSize(d);

        try {
            img = ImageIO.read(new File("images\\select.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        //final JButton selButton = new JButton("Selection");
        final JButton selButton = new JButton(new ImageIcon(dimg));
        selButton.setToolTipText("Selection");
        controlPanel.add(selButton);
        selButton.addActionListener(new ActionSelButton());
        selButton.setSize(d);

        try {
            img = ImageIO.read(new File("images\\cut.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        //final JButton cutButton = new JButton("cut");
        final JButton cutButton = new JButton(new ImageIcon(dimg));
        cutButton.setToolTipText("Cut");
        controlPanel.add(cutButton);
        cutButton.addActionListener(new ActionCutButton());
        cutButton.setSize(d);

        try {
            img = ImageIO.read(new File("images\\copy.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        //final JButton copyButton = new JButton("copy");
        final JButton copyButton = new JButton(new ImageIcon(dimg));
        copyButton.setToolTipText("Copy");
        controlPanel.add(copyButton);
        copyButton.addActionListener(new ActionCopyButton());
        copyButton.setSize(d);

        try {
            img = ImageIO.read(new File("images\\paste.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        //final JButton pasteButton = new JButton("paste");
        final JButton pasteButton = new JButton(new ImageIcon(dimg));
        pasteButton.setToolTipText("Paste");
        controlPanel.add(pasteButton);
        pasteButton.addActionListener(new ActionPasteButton());
        pasteButton.setSize(d);

        try {
            img = ImageIO.read(new File("images\\group.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        //final JButton grpButton = new JButton("Group");
        final JButton grpButton = new JButton(new ImageIcon(dimg));
        grpButton.setToolTipText("Group");
        controlPanel.add(grpButton);
        grpButton.addActionListener(new ActionGrpButton());
        grpButton.setSize(d);

        try {
            img = ImageIO.read(new File("images\\ungroup.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        //final JButton ungrpButton = new JButton("Ungroup");
        final JButton ungrpButton = new JButton(new ImageIcon(dimg));
        ungrpButton.setToolTipText("Ungroup");
        controlPanel.add(ungrpButton);
        ungrpButton.addActionListener(new ActionUngrpButton());
        ungrpButton.setSize(d);

        try {
            img = ImageIO.read(new File("images\\undo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        undoButton = new JButton(new ImageIcon(dimg));
        undoButton.setToolTipText("Undo");
        controlPanel.add(undoButton);
        undoButton.addActionListener(new ActionUndoButton());
        undoButton.setEnabled(false);
        undoButton.setSize(d);

        try {
            img = ImageIO.read(new File("images\\redo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        redoButton = new JButton(new ImageIcon(dimg));
        redoButton.setToolTipText("Redo");
        controlPanel.add(redoButton);
        redoButton.addActionListener(new ActionRedoButton());
        redoButton.setEnabled(false);
        redoButton.setSize(d);

        try {
            img = ImageIO.read(new File("images\\delete.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH); 
        //final JButton delButton = new JButton("delete");
        final JButton delButton = new JButton(new ImageIcon(dimg));
        delButton.setToolTipText("Delete");
        controlPanel.add(delButton);
        delButton.addActionListener(new ActionDelButton());
        delButton.setSize(d);

        try {
            img = ImageIO.read(new File("images\\clear.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        final JButton clearButton = new JButton(new ImageIcon(dimg));
        clearButton.setToolTipText("Clear");
        controlPanel.add(clearButton);
        clearButton.addActionListener(new ActionClearButton());
        clearButton.setSize(d);

        try {
            img = ImageIO.read(new File("images\\save.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        //final JButton saveButton = new JButton("save");
        final JButton saveButton = new JButton(new ImageIcon(dimg));
        saveButton.setToolTipText("Save");
        controlPanel.add(saveButton);
        saveButton.addActionListener(new ActionSaveButton());
        saveButton.setSize(d);

        try {
            img = ImageIO.read(new File("images\\load.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        //final JButton loadButton = new JButton("load");
        final JButton loadButton = new JButton(new ImageIcon(dimg));
        loadButton.setToolTipText("Load");
        controlPanel.add(loadButton);
        loadButton.addActionListener(new ActionLoadButton());
        loadButton.setSize(d);

        try {
            img = ImageIO.read(new File("images\\black.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(clrwidth, clrheight, Image.SCALE_SMOOTH);
        final JButton blackColor = new JButton(new ImageIcon(dimg));
        blackColor.setToolTipText("black");
        controlPanel.add(blackColor);
        blackColor.addActionListener(new ActionBlackClrButton());
        blackColor.setSize(d);

        try {
            img = ImageIO.read(new File("images\\blue.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(clrwidth, clrheight, Image.SCALE_SMOOTH);
        final JButton blueColor = new JButton(new ImageIcon(dimg));
        blueColor.setToolTipText("blue");
        controlPanel.add(blueColor);
        blueColor.addActionListener(new ActionBlueClrButton());
        blueColor.setSize(d);

        try {
            img = ImageIO.read(new File("images\\brown.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(clrwidth, clrheight, Image.SCALE_SMOOTH);
        final JButton brownColor = new JButton(new ImageIcon(dimg));
        brownColor.setToolTipText("brown");
        controlPanel.add(brownColor);
        brownColor.addActionListener(new ActionBrownClrButton());
        brownColor.setSize(d);

        try {
            img = ImageIO.read(new File("images\\green.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(clrwidth, clrheight, Image.SCALE_SMOOTH);
        final JButton greenColor = new JButton(new ImageIcon(dimg));
        greenColor.setToolTipText("green");
        controlPanel.add(greenColor);
        greenColor.addActionListener(new ActionGreenClrButton());
        greenColor.setSize(d);

        try {
            img = ImageIO.read(new File("images\\purple.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(clrwidth, clrheight, Image.SCALE_SMOOTH);
        final JButton purpleColor = new JButton(new ImageIcon(dimg));
        purpleColor.setToolTipText("purple");
        controlPanel.add(purpleColor);
        purpleColor.addActionListener(new ActionPurpleClrButton());
        purpleColor.setSize(d);

        try {
            img = ImageIO.read(new File("images\\red.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(clrwidth, clrheight, Image.SCALE_SMOOTH);
        final JButton redColor = new JButton(new ImageIcon(dimg));
        redColor.setToolTipText("red");
        controlPanel.add(redColor);
        redColor.addActionListener(new ActionRedClrButton());
        redColor.setSize(d);

        try {
            img = ImageIO.read(new File("images\\white.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(clrwidth, clrheight, Image.SCALE_SMOOTH);
        final JButton whiteColor = new JButton(new ImageIcon(dimg));
        whiteColor.setToolTipText("white");
        controlPanel.add(whiteColor);
        whiteColor.addActionListener(new ActionWhiteClrButton());
        whiteColor.setSize(d);

        try {
            img = ImageIO.read(new File("images\\yellow.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dimg = img.getScaledInstance(clrwidth, clrheight, Image.SCALE_SMOOTH);
        final JButton yellowColor = new JButton(new ImageIcon(dimg));
        yellowColor.setToolTipText("Load");
        controlPanel.add(yellowColor);
        yellowColor.addActionListener(new ActionYellowClrButton());
        yellowColor.setSize(d);

        logCheck = new JCheckBox("enable logs", false);
        controlPanel.add(logCheck);
        logCheck.addActionListener(new ActionEnableLogging());
        //JCheckBox.setSize(d);
        
        leftMenu.add(controlPanel);
        this.add(leftMenu);
        this.add(license, BorderLayout.SOUTH);
        
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
        controlPanel.addKeyListener(keylistener);
        
        sketchList[SketchConstants.SKETCH_LINE] = new SketchFreeHand();
        sketchList[SketchConstants.SKETCH_STRAIGHTLINE] = new SketchStLine();
        sketchList[SketchConstants.SKETCH_RECTANGLE] = new SketchRect();
        sketchList[SketchConstants.SKETCH_SQUARE] = new SketchSquare();
        sketchList[SketchConstants.SKETCH_ELLIPSE] = new SketchEllipse();
        sketchList[SketchConstants.SKETCH_CIRCLE] = new SketchCircle();
        sketchList[SketchConstants.SKETCH_POLYGON] = new SketchPolygon(true);
        sketchList[SketchConstants.SKETCH_CPOLYGON] = new SketchPolygon(false);
    }
    
    public ISketch GetSketch(int type) {
        return sketchList[type];
    }
    
    public Graphics2D GetGraphicInstance() {
        //System.out.print("[TEST] print " + this + "\n");
        return g2d;
    }
    
    public Color GetCurrColor() {
        return currColor;
    }

    public void SetCurrColor(Color newColor) {
        this.currColor = newColor;
    }
    
    public IShape CreateNewShape(ShapeType type, boolean clip) {
        IShape newShape = null;
        shapeCntr++;
        switch (type) {
        case SHAPE_TYPE_FREEHAND:
            newShape = new FreeHand(shapeCntr);
            break;
        case SHAPE_TYPE_STRAIGHTLINE:
            newShape = new StraightLine(shapeCntr);
            break;
        case SHAPE_TYPE_RECTANGLE:
            newShape = new Rectangle(shapeCntr);
            break;
        case SHAPE_TYPE_ELLIPSE:
            newShape = new Ellipse(shapeCntr);
            break;
        case SHAPE_TYPE_SQUARE:
            newShape = new Square(shapeCntr);
            break;
        case SHAPE_TYPE_CIRCLE:
            newShape = new Circle(shapeCntr);
            break;
        case SHAPE_TYPE_POLYGON:
            newShape = new PolygonSh(shapeCntr, true);
            break;
        case SHAPE_TYPE_CPOLYGON:
            newShape = new PolygonSh(shapeCntr, false);
            break;
        default:
            break;
        }
        if (false == clip) {
            shapeList.add(newShape);
        }
        return newShape;
    }
    
    public void paint( Graphics g ) {
        super.paint(g);
        for(IShape sh : shapeList) {
            sh.Paint();
        }
    }
    
    public void RePaint() {
        repaint();
    }
    
    public boolean CheckSelection(SketchAppEvent skEv, AWTEvent ev) {
        MouseEvent mEv = (MouseEvent)ev;
        for(IShape sh : shapeList) {
            if(sh.CheckSelection(new Point(mEv.getX(), mEv.getY()))) {
                if (false == mEv.isControlDown()) {
                    ClearSelection();
                }
                int grpId = sh.GetGroupID();
                if (true == sh.GetIsSelected()) {
                    /**
                     * Check if this shape is associated with a group,
                     * if yes, add the whole group to the selection
                     */
                    if (-1 != grpId) {
                        AddGroupToSelection(grpId);
                    } else {
                        AddToSelectionList(sh);
                    }
                } else {
                    if (-1 != grpId) {
                        RemGroupFromSelection(grpId);
                    } else {
                        RemFromSelectionList(sh);
                    }
                }
                RePaint();
                return true;
            }
        }
        ClearSelection();
        RePaint();
        return false;
    }
    
    public void AddToSelectionList(IShape sh) {
        sh.SetIsSelected(true);
        SelectedList.add(sh);
    }

    public void AddGroupToSelection(int grpId) {
        for (GroupShape grp : groupList) {
            if (grp.GetGrpId() == grpId) {
                ArrayList<IShape> mem = grp.GetMembers();
                for (IShape sh : mem) {
                    AddToSelectionList(sh);
                }
            }
        }
    }
    
    public void RemFromSelectionList(IShape sh) {
        sh.SetIsSelected(false);
        SelectedList.remove(sh);
    }
    
    public void RemGroupFromSelection(int grpId) {
        for (GroupShape grp : groupList) {
            if (grp.GetGrpId() == grpId) {
                ArrayList<IShape> mem = grp.GetMembers();
                for (IShape sh : mem) {
                    RemFromSelectionList(sh);
                }
            }
        }
    }
    
    /**
     * @brief Clear all selected shapes
     */
    public void ClearSelection() {
        for(IShape sh : SelectedList) {
            sh.SetIsSelected(false);
        }
        SelectedList.clear();
    }
    
    public boolean MoveSelection(SketchAppEvent skEv, AWTEvent ev) {
        MouseEvent mEv = (MouseEvent)ev;
        if (SketchAppEvent.EVENT_MOUSE_PRESSED == skEv) {
            moveP.setLocation(mEv.getX(), mEv.getY());
        }
        else if (SketchAppEvent.EVENT_MOUSE_DRAGGED == skEv) {
            if (false == isMoveSel) {
                    /**
                     * Accept moving state if user moves more than
                     * 4 pixel length
                     */
                    if (4 < moveP.distance(new Point(mEv.getX(), mEv.getY()))) {
                        moveState = new State();
                        moveState.SetOldShapes(SelectedList);
                        isMoveSel = true;
                    } else {
                        return false;
                    }
            }
            for(IShape sh : SelectedList) {
                sh.Move(new Point(mEv.getX() - moveP.x, mEv.getY() - moveP.y));
            }
            moveP.setLocation(mEv.getX(), mEv.getY());
            RePaint();
        }
        else if (SketchAppEvent.EVENT_MOUSE_RELEASED == skEv) {
            if (null != moveState) {
                moveState.SetNewShapes(SelectedList);
                LogActivity(SketchActivities.ACTIVITY_MOVE,
                        moveState);
                moveState = null;
            }
            isMoveSel = false;
        }
        return isMoveSel;
    }
    
    public void GroupShapes() {
        CreateNewGroup(SelectedList);
    }
    
    public GroupShape CreateNewGroup(ArrayList<IShape> shList) {
        grpCntr++;
        for(IShape sh : shList) {
            /**
             * Check if shapes are already a member of a group
             * Remove all of those groups
             */
            int id = sh.GetGroupID();
            if (-1 != id) {
                RemoveGroupWithID(id);
            }
        }
        GroupShape grp = new GroupShape(grpCntr, shList);
        groupList.add(grp);
        LogActivity(SketchActivities.ACTIVITY_NEW,
                null, null, null, grp);
        return grp;
    }
    
    public void Ungroup() {
        for(IShape sh : SelectedList) {
            int id = sh.GetGroupID();
            if (-1 != id) {
                RemoveGroupWithID(id);
            }
        }
    }
    
    public void RemoveGroupWithID(int id) {
        for(GroupShape grp : groupList) {
            if (grp.GetGrpId() == id) {
                LogActivity(SketchActivities.ACTIVITY_DELETE,
                        null, grp, null, null);
                grp.UnGroup();
                groupList.remove(grp);
                break;
            }
        }
    }
    
    public void DeleteSelection(boolean log) {
        for(IShape sh : SelectedList) {
            int id = sh.GetGroupID();
            if (true == log) {
                LogActivity(SketchActivities.ACTIVITY_DELETE,
                        SelectedList, null, null, null);
            }
            if (-1 != id) {
                RemoveGroupWithID(id);
            }
        }
        
        for(IShape sh : SelectedList) {
            shapeList.remove(sh);
        }
        SelectedList.clear();
        RePaint();
    }
    
    public void CutToClipboard() {
        AddSelectionToClipboard();
        DeleteSelection(false);
    }
    
    public void CopyToClipboard() {
        AddSelectionToClipboard();
    }
    
    public void AddSelectionToClipboard() {
        clip.Clear();
        clip.SetShapeList(SelectedList);
    }
    
    public IShape CreateNewAndCopy(IShape sh) {
        IShape newSh = CreateNewShape(sh.GetShapeType(), false);
        newSh.CopyProperties(sh);
        newSh.Move(new Point(10, 10));
        AddToSelectionList(newSh);
        sh.SetIsSelected(false);
        return newSh;
    }
    
    public void PasteFromClipboard() {
        ClearSelection();
        ArrayList<IShape> shList = clip.GetShapeList();
        
        for (int i = 0; i < shList.size(); i++) {
            IShape sh = shList.get(i);
            if (true == sh.GetIsSelected()) {
                IShape newSh = CreateNewAndCopy(sh);
                newSh.SetGroup(-1);
                int grpId = sh.GetGroupID();
                if (-1 != grpId) {
                    ArrayList<IShape> newShList = new ArrayList<IShape>();
                    newShList.add(newSh);
                    for (int j = i + 1; j < shList.size(); j++) {
                        IShape shNxt = shList.get(j);
                        if (grpId == shNxt.GetGroupID()) {
                            IShape memSh = CreateNewAndCopy(shNxt);
                            memSh.SetGroup(-1);
                            newShList.add(memSh);
                        }
                    }
                    CreateNewGroup(newShList);
                }
            }
        }
        LogActivity(SketchActivities.ACTIVITY_NEW,
                null, null, SelectedList, null);
        AddSelectionToClipboard();
        RePaint();
    }
    
    public void LogActivity(SketchActivities type, ArrayList<IShape> prevSh, GroupShape prevGrp,
            ArrayList<IShape> nextSh, GroupShape nextGrp) {
        Activity act = new Activity(type, prevSh, prevGrp, nextSh, nextGrp);
        undoList.add(act);
        redoList.clear();
        undoButton.setEnabled(true);
        redoButton.setEnabled(false);
    }
    
    public void LogActivity(SketchActivities type, State st) {
        Activity act = new Activity(type, st);
        undoList.add(act);
        redoList.clear();
        undoButton.setEnabled(true);
        redoButton.setEnabled(false);
    }
    
    public void ApplyUndo() {
        if (0 == undoList.size()) {
            return;
        }
        ClearSelection();
        int size = undoList.size();
        if (1 > size) {
            return;
        }
        Activity act = undoList.get(size - 1);
        undoList.remove(size - 1);
        
        GroupShape grp = act.GetState().GetNewGroup();
        if (null != grp) {
            for (GroupShape grpL : groupList) {
                if (grpL.GetGrpId() == grp.GetGrpId()) {
                    grpL.UnGroup();
                    groupList.remove(grpL);
                    break;
                }
            }
        }
        
        grp = act.GetState().GetOldGroup();
        if (null != grp) {
            GroupShape grpN = new GroupShape(grp.GetGrpId(), grp.GetMembers());
            groupList.add(grpN);
        }
        
        ArrayList<IShape> shpList = act.GetState().GetNewShapes();
        if (null != shpList) {
            for (IShape shp: shpList) {
                for (IShape shCurr : shapeList) {
                    if (shCurr.GetShapeID() == shp.GetShapeID()) {
                        AddToSelectionList(shCurr);
                        DeleteSelection(false);
                        break;
                    }
                }
            }
        }
        
        shpList = act.GetState().GetOldShapes();
        if (null != shpList) {
            for (IShape shp: shpList) {
                IShape newSh = act.GetState().CreateShapeClone(shp);
                newSh.CopyProperties(shp);
                AddToSelectionList(newSh);
                shapeList.add(newSh);
            }
        }
        
        redoList.add(act);
        if (0 == undoList.size()) {
            undoButton.setEnabled(false);
        }
        redoButton.setEnabled(true);
        RePaint();
    }
    
    public void ApplyRedo() {
        if (0 == redoList.size()) {
            return;
        }
        ClearSelection();
        int size = redoList.size();
        if (1 > size) {
            return;
        }
        Activity act = redoList.get(size - 1);
        redoList.remove(size - 1);
        
        GroupShape grp = act.GetState().GetOldGroup();
        if (null != grp) {
            for (GroupShape grpL : groupList) {
                if (grpL.GetGrpId() == grp.GetGrpId()) {
                    grpL.UnGroup();
                    groupList.remove(grpL);
                    break;
                }
            }
        }
        
        grp = act.GetState().GetNewGroup();
        if (null != grp) {
            GroupShape grpN = new GroupShape(grp.GetGrpId(), grp.GetMembers());
            groupList.add(grpN);
        }
        
        ArrayList<IShape> shpList = act.GetState().GetOldShapes();
        if (null != shpList) {
            for (IShape shp: shpList) {
                for (IShape shCurr : shapeList) {
                    if (shCurr.GetShapeID() == shp.GetShapeID()) {
                        AddToSelectionList(shCurr);
                        DeleteSelection(false);
                        break;
                    }
                }
            }
        }
        
        shpList = act.GetState().GetNewShapes();
        if (null != shpList) {
            for (IShape shp: shpList) {
                IShape newSh = act.GetState().CreateShapeClone(shp);
                newSh.CopyProperties(shp);
                AddToSelectionList(newSh);
                shapeList.add(newSh);
            }
        }
        
        undoList.add(act);
        undoButton.setEnabled(true);
        if (0 == redoList.size()) {
            redoButton.setEnabled(false);
        }
        RePaint();
    }
    
    public void ClearAll() {
        for(IShape sh : shapeList) {
            AddToSelectionList(sh);
        }
        for(IShape sh : SelectedList) {
            int id = sh.GetGroupID();
            LogActivity(SketchActivities.ACTIVITY_DELETE,
                    SelectedList, null, null, null);
            if (-1 != id) {
                RemoveGroupWithID(id);
            }
        }
        
        for(IShape sh : SelectedList) {
            shapeList.remove(sh);
        }
        SelectedList.clear();
        shapeList.clear();
        groupList.clear();
        RePaint();
    }
    
    public void SaveSketch() {
        ReadWritePackage pack = new ReadWritePackage();
        pack.shapes = shapeList;
        pack.grps = groupList;
        pack.undo = undoList;
        pack.redo = redoList;
        filerw.WritePack(pack);
    }
    
    public void LoadSketch() {
        ReadWritePackage pack;
        pack = filerw.ReadPack();
        if (null == pack) {
            return;
        }
        shapeList = pack.shapes;
        groupList = pack.grps;
        undoList = pack.undo;
        redoList = pack.redo;
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);
        if (0 != undoList.size()) {
            undoButton.setEnabled(true);
        }
        if (0 != redoList.size()) {
            redoButton.setEnabled(true);
        }
        RePaint();
    }
    
    public void FillSelection(Color color) {
        moveState = new State();
        moveState.SetOldShapes(SelectedList);
        for(IShape sh : SelectedList) {
            sh.FillShape(color);
        }
        moveState.SetNewShapes(SelectedList);
        LogActivity(SketchActivities.ACTIVITY_EDIT,
                moveState);
        moveState = null;
        RePaint();
    }
    
    public void LogInFile(String str) {
        if (true == islogEnabled) {
            fileLog.Log(str);
        }
    }
    
    public class ActionLineButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_FREEHAND, evt);
        }
        
    }
    
    public class ActionSlButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_STRAIGHTLINE, evt);
        }
    }
    
    public class ActionRectButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_RECTANGLE, evt);
        }
    }
    
    public class ActionSqButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_SQUARE, evt);
        }
    }
    
    public class ActionEllButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_ELLIPSE, evt);
        }
    }
    
    public class ActionCircleButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_CIRCLE, evt);
        }
    }
    
    public class ActionPolyButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_POLYGON, evt);
        }
    }
    
    public class ActionCPolyButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_CPOLYGON, evt);
        }
    }
    
    public class ActionSelButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_SELECT, evt);
        }
    }
    
    public class ActionGrpButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_GROUP, evt);
        }
    }
    
    public class ActionUngrpButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_UNGROUP, evt);
        }
    }
    
    public class ActionCutButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_CUT, evt);
        }
    }
    
    public class ActionCopyButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_COPY, evt);
        }
    }
    
    public class ActionPasteButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_PASTE, evt);
        }
    }
    
    public class ActionDelButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_DELETE, evt);
        }
    }
    
    public class ActionUndoButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_UNDO, evt);
        }
    }
    
    public class ActionRedoButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_REDO, evt);
        }
    }
    
    public class ActionClearButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_CLEAR, evt);
        }
    }
    
    public class ActionSaveButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_SAVE, evt);
        }
    }
    
    public class ActionLoadButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            StateMachine.GetStateMachine().EventHandler(SketchAppEvent.EVENT_BUTN_LOAD, evt);
        }
    }
    
    public class ActionBlackClrButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            if (SketchAppState.APP_STATE_SELECT != StateMachine.GetStateMachine().GetSketchState()) {
                currColor = Color.black;
            } else {
                FillSelection(Color.black);
            }
        }
    }
    
    public class ActionBlueClrButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            if (SketchAppState.APP_STATE_SELECT != StateMachine.GetStateMachine().GetSketchState()) {
                currColor = Color.blue;
            } else {
                FillSelection(Color.blue);
            }
        }
    }
    
    public class ActionBrownClrButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            if (SketchAppState.APP_STATE_SELECT != StateMachine.GetStateMachine().GetSketchState()) {
                currColor = new Color(165, 42, 42);
            } else {
                FillSelection(new Color(165, 42, 42));
            }
        }
    }
    
    public class ActionGreenClrButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            if (SketchAppState.APP_STATE_SELECT != StateMachine.GetStateMachine().GetSketchState()) {
                currColor = Color.green;
            } else {
                FillSelection(Color.green);
            }
        }
    }
    
    public class ActionPurpleClrButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            if (SketchAppState.APP_STATE_SELECT != StateMachine.GetStateMachine().GetSketchState()) {
                currColor = new Color(160, 32, 240);
            } else {
                FillSelection(new Color(160, 32, 240));
            }
        }
    }
    
    public class ActionRedClrButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            if (SketchAppState.APP_STATE_SELECT != StateMachine.GetStateMachine().GetSketchState()) {
                currColor = Color.red;
            } else {
                FillSelection(Color.red);
            }
        }
    }
    
    public class ActionWhiteClrButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            if (SketchAppState.APP_STATE_SELECT != StateMachine.GetStateMachine().GetSketchState()) {
                currColor = Color.white;
            } else {
                FillSelection(Color.white);
            }
        }
    }
    
    public class ActionYellowClrButton implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            if (SketchAppState.APP_STATE_SELECT != StateMachine.GetStateMachine().GetSketchState()) {
                currColor = Color.yellow;
            } else {
                FillSelection(Color.yellow);
            }
        }
    }
    
    public class ActionEnableLogging implements ActionListener {
        public void actionPerformed( ActionEvent evt ) {
            if (true == logCheck.isSelected()) {
                fileLog.OpenLog();
                islogEnabled = true;
            } else {
                fileLog.CloseFile();
                islogEnabled = false;
            }
        }
    }
    
    public static void main(String args[]) {
        sketchFrame = new JFrame();
        sketchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sketchFrame.getContentPane().setBackground( Color.white );
        SketchApp canvas = SketchApp.GetSketchApp();
        sketchFrame.setPreferredSize(new Dimension(
                SketchConstants.FRAME_WIDTH, SketchConstants.FRAME_HEIGHT));
        sketchFrame.add(canvas);
        sketchFrame.pack();
        sketchFrame.setVisible(true);
        canvas.SetGraphicsInstance();
    }
}