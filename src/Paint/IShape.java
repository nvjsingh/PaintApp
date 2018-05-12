package Paint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.awt.Polygon;

import java.io.Serializable;

public class IShape implements Serializable{
    private static final long serialVersionUID = 1L;
    protected int shapeID;
    protected Color shColor;
    protected int inGroup;
    protected ShapeType type;
    protected Color fillColor = null;
    //protected Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
    protected boolean isSelected = false;
    
    public IShape(int id, ShapeType shType) {
        shapeID = id;
        shColor = SketchApp.GetSketchApp().GetCurrColor();
        inGroup = -1;
        type = shType;
    }
    
    public int GetShapeID() {
        return shapeID;
    }
    
    public void SetShapeID(int id) {
        shapeID = id;
    }
    
    public int GetGroupID() {
        return inGroup;
    }
    
    public void SetGroup(int group) {
        inGroup = group;
    }
    
    public ShapeType GetShapeType() {
        return type;
    }
    
    public Color GetShapeColor() {
        return shColor;
    }
    
    public void SetColor(Color c) {
        shColor = c;
    }
    
    public void Paint() {
        
    }
    
    public boolean CheckSelection(Point p) {
        return false;
    }
    
    public boolean GetIsSelected() {
        return isSelected;
    }
    
    public void SetIsSelected(boolean state) {
        isSelected = state;
    }
    
    public void Move(Point gradient) {
        
    }
    
    public void CopyProperties(IShape sh) {
        shColor = sh.shColor;
        inGroup = sh.inGroup;
        fillColor = sh.fillColor;
    }
    
    public void FillShape(Color color) {
        
    }
}

class FreeHand extends IShape {
    private static final long serialVersionUID = 1L;
    private ArrayList<Line2D> lines = new ArrayList<Line2D>();
    public FreeHand(int id) {
        super(id, ShapeType.SHAPE_TYPE_FREEHAND);
    }
    
    public int SetLine(Point2D start, Point2D end) {
        lines.add(new Line2D.Float(start, end));
        return 0;
    }
    
    public ArrayList<Line2D> GetLinesList() {
        return lines;
    }
    
    public void Paint() {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(shColor);
        for(Line2D line : lines) {
            g2d.draw(line);
        }
        if (true == isSelected) {
            ApplySelection();
        }
    }
    
    public boolean CheckSelection(Point p) {
        for(Line2D line : lines) {
            Point2D p1 = line.getP1();
            Point2D p2 = line.getP2();
            double len = p1.distance(p2);
            if ((len < p.distance(p1)) || (len < p.distance(p2))) {
                continue;
            }
            double d = line.ptLineDist(p);
            if (SketchConstants.selDelta > d) {
                isSelected ^= true;
                return true;
            }
        }
        return false;
    }
    
    public void ApplySelection() {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(Color.black);
        Point2D p = lines.get(0).getP1();
        g2d.drawOval((int)p.getX() - SketchConstants.selCircleRad,
                (int)p.getY() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
        p = lines.get(lines.size() - 1).getP2();
        g2d.drawOval((int)p.getX() - SketchConstants.selCircleRad,
                (int)p.getY() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
    }
    
    public void Move(Point positiveGradient) {
        for (Line2D line : lines) {
            line.setLine(line.getX1() + positiveGradient.x,
                    line.getY1() + positiveGradient.y,
                    line.getX2() + positiveGradient.x,
                    line.getY2() + positiveGradient.y);
        }
    }
    
    public void CopyProperties(IShape sh) {
        super.CopyProperties(sh);
        ArrayList<Line2D> shLines = ((FreeHand)sh).GetLinesList();
        for (Line2D line : shLines) {
            line = (Line2D) line.clone();
            lines.add(line);
        }
    }
}

class StraightLine extends IShape {
    private static final long serialVersionUID = 1L;
    private Line2D line2d = new Line2D.Float(0, 0, 0, 0);
    public StraightLine(int id) {
        super(id, ShapeType.SHAPE_TYPE_STRAIGHTLINE);
    }
    
    public Point2D GetStart() {
        return line2d.getP1();
    }
    
    public Point2D GetEnd() {
        return line2d.getP2();
    }
    
    public int SetLine(Point2D start, Point2D end) {
        line2d.setLine(start, end);
        return 0;
    }
    
    public Line2D GetLine2D() {
        return line2d;
    }
    
    public void Paint() {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(shColor);
        g2d.draw(line2d);
        if (true == isSelected) {
            ApplySelection();
        }
    }
    
    public boolean CheckSelection(Point p) {
        Point2D p1 = line2d.getP1();
        Point2D p2 = line2d.getP2();
        double len = p1.distance(p2);
        if ((len < p.distance(p1)) || (len < p.distance(p2))) {
            return false;
        }
        double d = line2d.ptLineDist(p);
        if (SketchConstants.selDelta > d) {
            isSelected ^= true;
            return true;
        }
        return false;
    }
    
    public void ApplySelection() {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(Color.black);
        Point2D p = line2d.getP1();
        g2d.drawOval((int)p.getX() - SketchConstants.selCircleRad,
                (int)p.getY() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
        p = line2d.getP2();
        g2d.drawOval((int)p.getX() - SketchConstants.selCircleRad,
                (int)p.getY() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
    }
    
    public void Move(Point positiveGradient) {
        line2d.setLine(line2d.getX1() + positiveGradient.x,
                line2d.getY1() + positiveGradient.y,
                line2d.getX2() + positiveGradient.x,
                line2d.getY2() + positiveGradient.y);
    }
    
    public void CopyProperties(IShape sh) {
        super.CopyProperties(sh);
        line2d.setLine(((StraightLine)sh).GetLine2D());
    }
}

class Rectangle extends IShape {
    private static final long serialVersionUID = 1L;
    private Rectangle2D shape2d = new Rectangle2D.Float(0, 0, 0, 0);
    public Rectangle(int id) {
        super(id, ShapeType.SHAPE_TYPE_RECTANGLE);
    }
    
    public Point2D GetTopLeftP() {
        Point2D topLeft = new Point2D.Double(shape2d.getX(), shape2d.getY());
        return topLeft;
    }
    
    public Point2D GetBottomRightP() {
        Point2D bottomRight = new Point2D.Double(shape2d.getX() + (int)shape2d.getWidth(),
                shape2d.getY() + shape2d.getHeight());
        return bottomRight;
    }
    
    public int SetShape(Point2D starting, Point2D dragPoint) {
        double width = dragPoint.getX() - starting.getX();
        double height = dragPoint.getY() - starting.getY();
        if (0 < width) {
          //Fourth quadrant
            if (0 < height) {
                shape2d.setFrame(starting.getX(), starting.getY(),
                        width, height);
            }
          //First quadrant
            else if (0 > height) {
                shape2d.setFrame(starting.getX(), starting.getY() + height,
                        width, -1 * height);
            }
        } else {
          //Third quadrant
            if (0 < height) {
                shape2d.setFrame(starting.getX() + width, starting.getY(),
                        -1 * width, height);
            }
          //Second quadrant
            else if (0 > height) {
                shape2d.setFrame(starting.getX() + width, starting.getY() + height,
                        -1 * width, -1 * height);
            }
        }
        return 0;
    }
    
    public Rectangle2D GetRect2D() {
        return shape2d;
    }
    
    public void Paint() {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(shColor);
        g2d.draw(shape2d);
        if (null != fillColor) {
            FillShape(fillColor);
        }
        if (true == isSelected) {
            ApplySelection();
        }
    }
    
    public boolean CheckSelection(Point p) {
        Rectangle2D rectOuter = new Rectangle2D.Float(0, 0, 0, 0);
        Rectangle2D rectInner = new Rectangle2D.Float(0, 0, 0, 0);
        rectOuter.setFrameFromDiagonal(
                shape2d.getX() - SketchConstants.selDelta,
                shape2d.getY() - SketchConstants.selDelta,
                shape2d.getX() + shape2d.getWidth() + SketchConstants.selDelta,
                shape2d.getY() + shape2d.getHeight() + SketchConstants.selDelta);
        rectInner.setFrameFromDiagonal(
                shape2d.getX() + SketchConstants.selDelta,
                shape2d.getY() + SketchConstants.selDelta,
                shape2d.getX() + shape2d.getWidth() - SketchConstants.selDelta,
                shape2d.getY() + shape2d.getHeight() - SketchConstants.selDelta);
        if ((true == rectOuter.contains(p)) &&
                (false == rectInner.contains(p))) {
            isSelected ^= true;
            return true;
        }
        return false;
    }
    
    public void ApplySelection() {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(Color.black);
        g2d.drawOval((int)shape2d.getX() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
        g2d.drawOval((int)shape2d.getX() + (int)shape2d.getWidth() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
        g2d.drawOval((int)shape2d.getX() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() + (int)shape2d.getHeight() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
        g2d.drawOval((int)shape2d.getX() + (int)shape2d.getWidth() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() + (int)shape2d.getHeight() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
    }
    
    public void Move(Point positiveGradient) {
        shape2d.setFrame(shape2d.getX() + positiveGradient.x,
                shape2d.getY() + positiveGradient.y,
                shape2d.getWidth(),
                shape2d.getHeight());
    }
    
    public void CopyProperties(IShape sh) {
        super.CopyProperties(sh);
        shape2d.setFrame(((Rectangle)sh).GetRect2D());
    }
    
    public void FillShape(Color color) {
        fillColor = color;
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(fillColor);
        g2d.fill(shape2d);
    }
}

class Ellipse extends IShape {
    private static final long serialVersionUID = 1L;
    private Ellipse2D shape2d = new Ellipse2D.Float(0, 0, 0, 0);
    public Ellipse(int id) {
        super(id, ShapeType.SHAPE_TYPE_ELLIPSE);
    }
    
    public Point2D GetTopLeftP() {
        Point2D topLeft = new Point2D.Double(shape2d.getX(), shape2d.getY());
        return topLeft;
    }
    
    public Point2D GetBottomRightP() {
        Point2D bottomRight = new Point2D.Double(shape2d.getX() + (int)shape2d.getWidth(),
                shape2d.getY() + shape2d.getHeight());
        return bottomRight;
    }
    
    public int SetShape(Point2D starting, Point2D dragPoint) {
        double width = dragPoint.getX() - starting.getX();
        double height = dragPoint.getY() - starting.getY();
        if (0 < width) {
          //Fourth quadrant
            if (0 < height) {
                shape2d.setFrame(starting.getX(), starting.getY(),
                        width, height);
            }
          //First quadrant
            else if (0 > height) {
                shape2d.setFrame(starting.getX(), starting.getY() + height,
                        width, -1 * height);
            }
        } else {
          //Third quadrant
            if (0 < height) {
                shape2d.setFrame(starting.getX() + width, starting.getY(),
                        -1 * width, height);
            }
          //Second quadrant
            else if (0 > height) {
                shape2d.setFrame(starting.getX() + width, starting.getY() + height,
                        -1 * width, -1 * height);
            }
        }
        return 0;
    }
    
    public Ellipse2D GetShape() {
        return shape2d;
    }
    
    public void Paint() {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(shColor);
        g2d.draw(shape2d);
        if (null != fillColor) {
            FillShape(fillColor);
        }
        if (true == isSelected) {
            ApplySelection();
        }
    }
    
    public boolean CheckSelection(Point p) {
        Ellipse2D shapeOuter = new Ellipse2D.Float(0, 0, 0, 0);
        Ellipse2D shapeInner = new Ellipse2D.Float(0, 0, 0, 0);
        shapeOuter.setFrameFromDiagonal(
                shape2d.getX() - SketchConstants.selDelta,
                shape2d.getY() - SketchConstants.selDelta,
                shape2d.getX() + shape2d.getWidth() + SketchConstants.selDelta,
                shape2d.getY() + shape2d.getHeight() + SketchConstants.selDelta);
        shapeInner.setFrameFromDiagonal(
                shape2d.getX() + SketchConstants.selDelta,
                shape2d.getY() + SketchConstants.selDelta,
                shape2d.getX() + shape2d.getWidth() - SketchConstants.selDelta,
                shape2d.getY() + shape2d.getHeight() - SketchConstants.selDelta);
        if ((true == shapeOuter.contains(p)) &&
                (false == shapeInner.contains(p))) {
            isSelected ^= true;
            return true;
        }
        return false;
    }
    
    public void ApplySelection() {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(Color.black);
        g2d.drawOval((int)shape2d.getX() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
        g2d.drawOval((int)shape2d.getX() + (int)shape2d.getWidth() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
        g2d.drawOval((int)shape2d.getX() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() + (int)shape2d.getHeight() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
        g2d.drawOval((int)shape2d.getX() + (int)shape2d.getWidth() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() + (int)shape2d.getHeight() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
    }
    
    public void Move(Point positiveGradient) {
        shape2d.setFrame(shape2d.getX() + positiveGradient.x,
                shape2d.getY() + positiveGradient.y,
                shape2d.getWidth(),
                shape2d.getHeight());
    }
    
    public void CopyProperties(IShape sh) {
        super.CopyProperties(sh);
        shape2d.setFrame(((Ellipse)sh).GetShape().getFrame());
    }
    
    public void FillShape(Color color) {
        fillColor = color;
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(fillColor);
        g2d.fill(shape2d);
    }
}

class Square extends IShape {
    private static final long serialVersionUID = 1L;
    private Rectangle2D shape2d = new Rectangle2D.Float(0, 0, 0, 0);
    private double width = 0;
    private double height = 0;
    public Square(int id) {
        super(id, ShapeType.SHAPE_TYPE_SQUARE);
    }
    
    public Point2D GetTopLeftP() {
        Point2D topLeft = new Point2D.Double(shape2d.getX(), shape2d.getY());
        return topLeft;
    }
    
    public Point2D GetBottomRightP() {
        Point2D bottomRight = new Point2D.Double(shape2d.getX() + (int)shape2d.getWidth(),
                shape2d.getY() + shape2d.getHeight());
        return bottomRight;
    }
    
    public int SetShape(Point2D starting, Point2D dragPoint) {
        width = dragPoint.getX() - starting.getX();
        height = dragPoint.getY() - starting.getY();
        SquareUp();
        if (0 < width) {
          //Fourth quadrant
            if (0 < height) {
                shape2d.setFrame(starting.getX(), starting.getY(),
                        width, height);
            }
          //First quadrant
            else if (0 > height) {
                shape2d.setFrame(starting.getX(), starting.getY() + height,
                        width, -1 * height);
            }
        } else {
          //Third quadrant
            if (0 < height) {
                shape2d.setFrame(starting.getX() + width, starting.getY(),
                        -1 * width, height);
            }
          //Second quadrant
            else if (0 > height) {
                shape2d.setFrame(starting.getX() + width, starting.getY() + height,
                        -1 * width, -1 * height);
            }
        }
        return 0;
    }
    
    public Rectangle2D GetShape() {
        return shape2d;
    }
    
    public void Paint() {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(shColor);
        g2d.draw(shape2d);
        if (null != fillColor) {
            FillShape(fillColor);
        }
        if (true == isSelected) {
            ApplySelection();
        }
    }
    
    public boolean CheckSelection(Point p) {
        Rectangle2D shapeOuter = new Rectangle2D.Float(0, 0, 0, 0);
        Rectangle2D shapeInner = new Rectangle2D.Float(0, 0, 0, 0);
        shapeOuter.setFrameFromDiagonal(
                shape2d.getX() - SketchConstants.selDelta,
                shape2d.getY() - SketchConstants.selDelta,
                shape2d.getX() + shape2d.getWidth() + SketchConstants.selDelta,
                shape2d.getY() + shape2d.getHeight() + SketchConstants.selDelta);
        shapeInner.setFrameFromDiagonal(
                shape2d.getX() + SketchConstants.selDelta,
                shape2d.getY() + SketchConstants.selDelta,
                shape2d.getX() + shape2d.getWidth() - SketchConstants.selDelta,
                shape2d.getY() + shape2d.getHeight() - SketchConstants.selDelta);
        if ((true == shapeOuter.contains(p)) &&
                (false == shapeInner.contains(p))) {
            isSelected ^= true;
            return true;
        }
        return false;
    }
    
    public void ApplySelection() {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(Color.black);
        g2d.drawOval((int)shape2d.getX() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
        g2d.drawOval((int)shape2d.getX() + (int)shape2d.getWidth() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
        g2d.drawOval((int)shape2d.getX() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() + (int)shape2d.getHeight() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
        g2d.drawOval((int)shape2d.getX() + (int)shape2d.getWidth() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() + (int)shape2d.getHeight() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
    }
    
    public void Move(Point positiveGradient) {
        shape2d.setFrame(shape2d.getX() + positiveGradient.x,
                shape2d.getY() + positiveGradient.y,
                shape2d.getWidth(),
                shape2d.getHeight());
    }
    
    public void CopyProperties(IShape sh) {
        super.CopyProperties(sh);
        shape2d.setFrame(((Square)sh).GetShape().getFrame());
    }
    
    public void SquareUp() {
        boolean wNeg = (0 < width) ? false: true;
        boolean hNeg = (0 < height) ? false: true;
        if (wNeg) width *= -1;
        if (hNeg) height *= -1;
        
        if (width > height) {
            width = height;
        } else {
            height = width;
        }
        
        if (wNeg) width *= -1;
        if (hNeg) height *= -1;
    }
    
    public void FillShape(Color color) {
        fillColor = color;
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(fillColor);
        g2d.fill(shape2d);
    }
}

class Circle extends IShape {
    private static final long serialVersionUID = 1L;
    private Ellipse2D shape2d = new Ellipse2D.Float(0, 0, 0, 0);
    private double width = 0;
    private double height = 0;
    public Circle(int id) {
        super(id, ShapeType.SHAPE_TYPE_CIRCLE);
    }
    
    public Point2D GetTopLeftP() {
        Point2D topLeft = new Point2D.Double(shape2d.getX(), shape2d.getY());
        return topLeft;
    }
    
    public Point2D GetBottomRightP() {
        Point2D bottomRight = new Point2D.Double(shape2d.getX() + (int)shape2d.getWidth(),
                shape2d.getY() + shape2d.getHeight());
        return bottomRight;
    }
    
    public int SetShape(Point2D starting, Point2D dragPoint) {
        width = dragPoint.getX() - starting.getX();
        height = dragPoint.getY() - starting.getY();
        CircleUp();
        if (0 < width) {
          //Fourth quadrant
            if (0 < height) {
                shape2d.setFrame(starting.getX(), starting.getY(),
                        width, height);
            }
          //First quadrant
            else if (0 > height) {
                shape2d.setFrame(starting.getX(), starting.getY() + height,
                        width, -1 * height);
            }
        } else {
          //Third quadrant
            if (0 < height) {
                shape2d.setFrame(starting.getX() + width, starting.getY(),
                        -1 * width, height);
            }
          //Second quadrant
            else if (0 > height) {
                shape2d.setFrame(starting.getX() + width, starting.getY() + height,
                        -1 * width, -1 * height);
            }
        }
        return 0;
    }
    
    public Ellipse2D GetShape() {
        return shape2d;
    }
    
    public void Paint() {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(shColor);
        g2d.draw(shape2d);
        if (null != fillColor) {
            FillShape(fillColor);
        }
        if (true == isSelected) {
            ApplySelection();
        }
    }
    
    public boolean CheckSelection(Point p) {
        Ellipse2D shapeOuter = new Ellipse2D.Float(0, 0, 0, 0);
        Ellipse2D shapeInner = new Ellipse2D.Float(0, 0, 0, 0);
        shapeOuter.setFrameFromDiagonal(
                shape2d.getX() - SketchConstants.selDelta,
                shape2d.getY() - SketchConstants.selDelta,
                shape2d.getX() + shape2d.getWidth() + SketchConstants.selDelta,
                shape2d.getY() + shape2d.getHeight() + SketchConstants.selDelta);
        shapeInner.setFrameFromDiagonal(
                shape2d.getX() + SketchConstants.selDelta,
                shape2d.getY() + SketchConstants.selDelta,
                shape2d.getX() + shape2d.getWidth() - SketchConstants.selDelta,
                shape2d.getY() + shape2d.getHeight() - SketchConstants.selDelta);
        if ((true == shapeOuter.contains(p)) &&
                (false == shapeInner.contains(p))) {
            isSelected ^= true;
            return true;
        }
        return false;
    }
    
    public void ApplySelection() {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(Color.black);
        g2d.drawOval((int)shape2d.getX() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
        g2d.drawOval((int)shape2d.getX() + (int)shape2d.getWidth() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
        g2d.drawOval((int)shape2d.getX() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() + (int)shape2d.getHeight() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
        g2d.drawOval((int)shape2d.getX() + (int)shape2d.getWidth() - SketchConstants.selCircleRad, 
                (int)shape2d.getY() + (int)shape2d.getHeight() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
    }
    
    public void Move(Point positiveGradient) {
        shape2d.setFrame(shape2d.getX() + positiveGradient.x,
                shape2d.getY() + positiveGradient.y,
                shape2d.getWidth(),
                shape2d.getHeight());
    }
    
    public void CopyProperties(IShape sh) {
        super.CopyProperties(sh);
        shape2d.setFrame(((Circle)sh).GetShape().getFrame());
    }
    
    public void CircleUp() {
        boolean wNeg = (0 < width) ? false: true;
        boolean hNeg = (0 < height) ? false: true;
        if (wNeg) width *= -1;
        if (hNeg) height *= -1;
        
        if (width > height) {
            width = height;
        } else {
            height = width;
        }
        
        if (wNeg) width *= -1;
        if (hNeg) height *= -1;
    }
    
    public void FillShape(Color color) {
        fillColor = color;
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(fillColor);
        g2d.fill(shape2d);
    }
}

class PolygonSh extends IShape {
    private static final long serialVersionUID = 1L;
    private Line2D line2d = new Line2D.Float(0, 0, 0, 0);
    private ArrayList<Line2D> sides = new ArrayList<Line2D>();
    private Polygon poly = new Polygon();
    private boolean isOpen = false;
    private boolean isFinished = false;
    
    public PolygonSh(int id) {
        super(id, ShapeType.SHAPE_TYPE_POLYGON);
    }
    
    public PolygonSh(int id, boolean b) {
        super(id, ShapeType.SHAPE_TYPE_POLYGON);
        isOpen = b;
    }
    
    public boolean GetIsOpen() {
        return isOpen;
    }
    
    public int SetLine(Point2D start, Point2D end) {
        line2d.setLine(start, end);
        return 0;
    }
    
    public void FinishSide() {
        sides.add(line2d);
        line2d = new Line2D.Float(0, 0, 0, 0);
    }
    
    public void FinishShape() {
        if (2 > sides.size()) {
            return;
        }
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        if (false == isOpen) {
            line2d = new Line2D.Double(sides.get(0).getX1(),
                    sides.get(0).getY1(),
                    sides.get(sides.size() - 1).getX2(),
                    sides.get(sides.size() - 1).getY2());
            g2d.draw(line2d);
        }
        for(Line2D line : sides) {
            poly.addPoint((int)line.getX1(), (int)line.getY1());
        }
        isFinished = true;
    }
    
    public ArrayList<Line2D> GetSidesList() {
        return sides;
    }
    
    public void Paint() {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(shColor);
        for(Line2D line : sides) {
            g2d.draw(line);
        }
        if (true == isFinished) {
            FinishShape();
        }
        g2d.draw(line2d);
        if (null != fillColor) {
            FillShape(fillColor);
        }
        if (true == isSelected) {
            ApplySelection();
        }
    }
    
    public boolean CheckSelection(Point p) {
        for(Line2D line : sides) {
            if (true == CheckSelectionForLine(line, p)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean CheckSelectionForLine(Line2D line, Point p) {
        Point2D p1 = line.getP1();
        Point2D p2 = line.getP2();
        double len = p1.distance(p2);
        if ((len < p.distance(p1)) || (len < p.distance(p2))) {
            return false;
        }
        double d = line.ptLineDist(p);
        if (SketchConstants.selDelta > d) {
            isSelected ^= true;
            return true;
        }
        return false;
    }
    
    public void ApplySelection() {
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(Color.black);
        Point2D p = sides.get(0).getP1();
        g2d.drawOval((int)p.getX() - SketchConstants.selCircleRad,
                (int)p.getY() - SketchConstants.selCircleRad,
                SketchConstants.selCircleDia,
                SketchConstants.selCircleDia);
        for (Line2D line : sides) {
            p = line.getP2();
            g2d.drawOval((int)p.getX() - SketchConstants.selCircleRad,
                    (int)p.getY() - SketchConstants.selCircleRad,
                    SketchConstants.selCircleDia,
                    SketchConstants.selCircleDia);
        }
    }
    
    public void Move(Point positiveGradient) {
        for (Line2D line : sides) {
            line.setLine(line.getX1() + positiveGradient.x,
                    line.getY1() + positiveGradient.y,
                    line.getX2() + positiveGradient.x,
                    line.getY2() + positiveGradient.y);
        }
    }
    
    public void CopyProperties(IShape sh) {
        super.CopyProperties(sh);
        isOpen = ((PolygonSh)sh).GetIsOpen();
        ArrayList<Line2D> shSides = ((PolygonSh)sh).GetSidesList();
        for (Line2D line : shSides) {
            line2d = (Line2D) line.clone();
            sides.add(line2d);
        }
    }
    
    public void FillShape(Color color) {
        /*fillColor = color;
        Graphics2D g2d = SketchApp.GetSketchApp().GetGraphicInstance();
        g2d.setColor(fillColor);
        g2d.fill(poly);*/
    }
}

class GroupShape implements Serializable {
    private static final long serialVersionUID = 1L;
    private int grpId;
    private ArrayList<IShape> members = new ArrayList<IShape>();
    
    public GroupShape(int id) {
        grpId = id;
    }
    
    public GroupShape(int id, ArrayList<IShape> mem) {
        grpId = id;
        AddToGroup(mem);
    }
    
    public int GetGrpId() {
        return grpId;
    }
    
    public ArrayList<IShape> GetMembers() {
        return members;
    }
    
    public void AddToGroup(ArrayList<IShape> mem) {
        for(IShape sh : mem) {
            sh.SetGroup(grpId);
            members.add(sh);
        }
    }
    
    public void AddToGroupElem(IShape sh) {
        members.add(sh);
    }
    
    public void UnGroup() {
        for(IShape sh : members) {
            sh.SetGroup(-1);
        }
        members.clear();
    }
}