package bsu.rfe.java.group7.lab4.Lashkin.varB1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GraphicsDisplay extends JPanel {
    private Double[][] graphicsData;
    private boolean showAxis = true;
    private boolean showMarkers = true;
    private boolean PPP = false;
    private boolean showIntGraphics = false;
    private boolean showGrid = true;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double scale;
    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    private BasicStroke graphicsIntStroke;
    private BasicStroke gridStroke;
    // Различные шрифты отображения надписей
    private Font axisFont;

    public GraphicsDisplay() {
        setBackground(Color.WHITE);
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 10.0f, new float[] {24,6,6,6,6,6,12,6,12,6,0,30}, 0.0f);
        graphicsIntStroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 10.0f, null, 0.0f);
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, null, 0.0f);
        axisFont = new Font("Serif", Font.BOLD, 36);

        gridStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, null, 0.0f);
    }

    public void showGraphics(Double[][] graphicsData) {
        this.graphicsData = graphicsData;
        repaint();
    }


    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }


    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaint();
    }



    public void setShowIntGraphics(boolean showIntGraphics) {
        this.showIntGraphics = showIntGraphics;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graphicsData == null || graphicsData.length == 0) return;
        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length - 1][0];
        minY = graphicsData[0][1];
        maxY = minY;
        for (int i = 1; i < graphicsData.length; i++) {
            if (graphicsData[i][1] < minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1] > maxY) {
                maxY = graphicsData[i][1];
            }
        }
        double scaleX = getSize().getWidth() / (maxX - minX);
        double scaleY = getSize().getHeight() / (maxY - minY);
        scale = Math.min(scaleX, scaleY);
        if (scale == scaleX) {
            double yIncrement = (getSize().getHeight() / scale - (maxY -
                    minY)) / 2;
            maxY += yIncrement;
            minY -= yIncrement;
        }
        if (scale == scaleY) {
            double xIncrement = (getSize().getWidth() / scale - (maxX - minX)) / 2;
            maxX += xIncrement;
            minX -= xIncrement;
        }


        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();
        if (showAxis) paintAxis(canvas);
        paintGraphics(canvas);
        if (showGrid)
            paintGrid(canvas);
        if (showIntGraphics) paintIntUnitOfGraphics(canvas);
        if (showMarkers) paintMarkers(canvas);
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }


    protected void paintGraphics(Graphics2D canvas) {
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.BLACK);
        GeneralPath graphics = new GeneralPath();
        for (int i = 0; i < graphicsData.length; i++) {
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            if (i > 0) {
                graphics.lineTo(point.getX(), point.getY());
            } else {
                graphics.moveTo(point.getX(), point.getY());
            }
        }
        canvas.draw(graphics);


    }

    protected void paintIntUnitOfGraphics(Graphics2D canvas) {
        canvas.setStroke(graphicsIntStroke);
        canvas.setColor(Color.BLACK);
        GeneralPath intGraphics = new GeneralPath();
        for (int i = 0; i < graphicsData.length; i++) {
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1].intValue());
            if (i > 0) {
                if (graphicsData[i][1].intValue() == graphicsData[i - 1][1].intValue())
                    intGraphics.lineTo(point.getX(), point.getY());
                else {
                    intGraphics.lineTo(point.getX(),
                            xyToPoint(graphicsData[i][0], graphicsData[i - 1][1].intValue()).getY());
                    intGraphics.moveTo(point.getX(), point.getY());
                }
            } else {
                intGraphics.moveTo(point.getX(), point.getY());
            }
        }
        canvas.draw(intGraphics);

    }

    protected boolean isSumLessThanTen(Double[] point) {
        int valueFuncInt = point[1].intValue();
        System.out.println(valueFuncInt);
        System.out.println("");
        String xstr = String.valueOf(valueFuncInt);
        return ("1234567890".contains(xstr));

    }




    protected void paintGrid(Graphics2D canvas) {
        GeneralPath graphics = new GeneralPath();
        double MAX = Math.max(Math.abs(maxX - minX), Math.abs(maxY - minY));
        double MAX20 = MAX / 10;
        double step = 0.0f;
            step = MAX20;
        if (PPP) {
            int YY = Math.min(getWidth(), getHeight());
            if (YY < 200)
                step *= 3;
            else if (YY < 400)
                step *= 2;
        }
        Color oldColor = canvas.getColor();
        Stroke oldStroke = canvas.getStroke();
        canvas.setStroke(gridStroke);
        canvas.setColor(Color.BLUE);
        int xp = 0;
        double x = 0.0d;
        int gH = getHeight();
        int gW = getWidth();
        xp = (int) xyToPoint(0, 0).x;
        while (xp > 0) {
            graphics.moveTo(xp, 0);
            graphics.lineTo(xp, gH);
            xp = (int) xyToPoint(x, 0).x;
            x -= step;
        }
        xp = (int) xyToPoint(0, 0).x;

        while (xp < gW) {
            graphics.moveTo(xp, 0);
            graphics.lineTo(xp, gH);
            xp = (int) xyToPoint(x, 0).x;
            x += step;
        }
        int yp = (int) xyToPoint(0, 0).y;
        double y = 0.0f;
        while (yp < gH) {
            yp = (int) xyToPoint(0, y).y;
            graphics.moveTo(0, yp);
            graphics.lineTo(gW, yp);
            y -= step;
        }
        yp = (int) xyToPoint(0, 0).y;
        while (yp > 0) {
            yp = (int) xyToPoint(0, y).y;
            graphics.moveTo(0, yp);
            graphics.lineTo(gW, yp);
            y += step;
        }
        canvas.draw(graphics);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }



    protected void paintMarkers(Graphics2D canvas) {


        canvas.setStroke(markerStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);

        for (Double[] point : graphicsData) {
            Point2D.Double center = xyToPoint(point[0], point[1]);
            GeneralPath path = new GeneralPath();
            path.moveTo(center.x - 5, center.y + 0);
            path.lineTo(center.x + 5, center.y + 0);
            path.moveTo(center.x + 0, center.y + 5);
            path.lineTo(center.x + 0, center.y - 5);
            path.moveTo(center.x + 5, center.y + 5);
            path.lineTo(center.x - 5, center.y - 5);
            path.moveTo(center.x - 5, center.y + 5);
            path.lineTo(center.x + 5, center.y - 5);
            canvas.draw(path);
        }
        for (Double[] point : graphicsData) {
            canvas.setPaint(Color.GREEN);

            if(isSumLessThanTen(point)) {
                Point2D.Double center = xyToPoint(point[0], point[1]);
                GeneralPath path = new GeneralPath();
                path.moveTo(center.x - 5, center.y + 0);
                path.lineTo(center.x + 5, center.y + 0);
                path.moveTo(center.x + 0, center.y + 5);
                path.lineTo(center.x + 0, center.y - 5);
                path.moveTo(center.x + 5, center.y + 5);
                path.lineTo(center.x - 5, center.y - 5);
                path.moveTo(center.x - 5, center.y + 5);
                path.lineTo(center.x + 5, center.y - 5);
                canvas.draw(path);
            }
        }
    }


    protected void paintAxis(Graphics2D canvas){
        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.setFont(axisFont);
        FontRenderContext context = canvas.getFontRenderContext();



        if (minX <= 0.0 && maxX >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(0.1, maxY-0.1), xyToPoint(0.1, minY-0.1)));

            GeneralPath arrow = new GeneralPath();

            Point2D.Double lineEnd = xyToPoint(0.1, maxY-0.1);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());

            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);

            arrow.lineTo(arrow.getCurrentPoint().getX() - 10, arrow.getCurrentPoint().getY());


            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);

            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0.1, maxY-0.1);

            canvas.drawString("y", (float) labelPos.getX() + 10, (float) (labelPos.getY() - bounds.getY()));

        }

        canvas.draw(new Line2D.Double(xyToPoint(0, minY+0.1), xyToPoint(maxX, minY+0.1)));

        GeneralPath arrow = new GeneralPath();

        Point2D.Double lineEnd = xyToPoint(maxX, minY+0.1);
        arrow.moveTo(lineEnd.getX(), lineEnd.getY());

        arrow.lineTo(arrow.getCurrentPoint().getX() - 20, arrow.getCurrentPoint().getY() - 5);

        arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 10);


        arrow.closePath();
        canvas.draw(arrow);
        canvas.fill(arrow);

        Rectangle2D bounds = axisFont.getStringBounds("x", context);
        Point2D.Double labelPos = xyToPoint(maxX, minY+0.1);

        canvas.drawString("x", (float) (labelPos.getX() - bounds.getWidth() - 10), (float) (labelPos.getY() + bounds.getY()));


         bounds = axisFont.getStringBounds("0", context);
         labelPos = xyToPoint(-3, 5);
        canvas.drawString("0", (float) labelPos.getX() + 10,
                (float) (labelPos.getY() - bounds.getY()));

    }


    protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX = x - minX;
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX * scale, deltaY * scale);
    }

    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX,
                                        double deltaY) {
        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }
}