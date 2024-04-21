package test;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;

public class TextComponent extends JComponent {
    private String text;
    private Font font;
    private Color textColor;
    private Color strokeColor;

    public TextComponent(String text, Font font, Color textColor, Color strokeColor) {
        this.text = text;
        this.font = font;
        this.textColor = textColor;
        this.strokeColor = strokeColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //消除文字锯齿
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 方式1，效果稍差
//        g2d.setFont(font);
//        FontMetrics fontMetrics = g2d.getFontMetrics();
//        int x = (getWidth() - fontMetrics.stringWidth(text)) / 2;
//        int y = (getHeight() - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();
//
//        // 创建文字形状
//        Shape textShape = font.createGlyphVector(g2d.getFontRenderContext(), text).getOutline();
//
//        // 创建描边形状
//        g2d.setStroke(new BasicStroke(2)); // 设置描边宽度
//        Shape strokeShape = g2d.getStroke().createStrokedShape(textShape);
//
//        g2d.translate(x, y);
//
//        // 绘制描边效果的文字
//        g2d.setColor(strokeColor);
//        g2d.draw(strokeShape);
//        // 绘制正常颜色的文字
//        g2d.setColor(textColor);
//        g2d.fill(textShape);



        // 方式2
        g2d.setFont(font);
        Shape shape = font.createGlyphVector(g2d.getFontRenderContext(), text).getOutline();

        Rectangle2D bounds = shape.getBounds2D();
        double x = (getWidth() - bounds.getWidth()) / 2 - bounds.getX();
        double y = (getHeight() - bounds.getHeight()) / 2 - bounds.getY();
        AffineTransform translate = AffineTransform.getTranslateInstance(x, y);
        Shape transformedShape = translate.createTransformedShape(shape);

        g2d.setColor(textColor);
        g2d.fill(transformedShape);

        g2d.setColor(strokeColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(transformedShape);

    }
}
