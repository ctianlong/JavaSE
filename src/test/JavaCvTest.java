package test;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.junit.Test;
import sun.font.FontDesignMetrics;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

/**
 * todo 注意：优先在本地安装ffmpeg调用本地命令执行；
 * 参考命令：
 * ./ffmpeg -i /Users/chengtianlong/Desktop/test.mp4 -vf "movie=/Users/chengtianlong/Desktop/water1.png [watermark]; [in][watermark] overlay=(W-w)/2:H-h-15:format=rgb [out]" /Users/chengtianlong/Desktop/test-1.mp4
 * ./ffmpeg -i /Users/chengtianlong/Desktop/test.mp4 -i /Users/chengtianlong/Desktop/water1.png -filter_complex "overlay=(W-w)/2:H-h-15:format=auto,format=yuv420p" -codec:a copy /Users/chengtianlong/Desktop/test-1.mp4
 * @author ctl
 * @createTime 2023/05/09 16:17
 * @description
 */
public class JavaCvTest {


    /**
     * 使用ffmpeg filter处理，输出视频质量有下降
     */
    @Test
    public void test1() throws Exception {
        String inPath = "/Users/chengtianlong/Desktop/test.mp4";
        String outpath = "/Users/chengtianlong/Desktop/test-1.mp4";
        String qrcodefilepath = "/Users/chengtianlong/Desktop/water1.png";

        // Without starting we cannot access any metadata / info
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(inPath);
        frameGrabber.start();

        // The length in time of a video is always more than zero
        if (frameGrabber.getLengthInTime() > 0) {
            // Input details
            int width = frameGrabber.getImageWidth();
            int height = frameGrabber.getImageHeight();
            int channels = frameGrabber.getAudioChannels();

            // Output with same Input details
            FFmpegFrameRecorder frameRecorder = new FFmpegFrameRecorder(outpath, width, height, channels);
            // Round down the FrameRate as passing a floating point does not work
            int frameRate = (int) frameGrabber.getFrameRate();
            frameRecorder.setFrameRate(frameRate);
            // Matching input with output
            frameRecorder.setSampleRate(frameGrabber.getSampleRate());
            frameRecorder.setAudioBitrate(frameGrabber.getAudioBitrate());
            frameRecorder.setVideoBitrate(frameGrabber.getVideoBitrate());
            // Compress with YUV420P / H264 / AAC generating a MP4
//            frameRecorder.setPixelFormat(frameGrabber.getPixelFormat());
//            frameRecorder.setVideoCodec(frameGrabber.getVideoCodec());
//            frameRecorder.setAudioCodec(frameGrabber.getAudioCodec());

            frameRecorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
            frameRecorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            frameRecorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);

            // Filter adds WaterMark
            String watermark = "movie=/Users/chengtianlong/Desktop/water1.png[watermark];[in][watermark]overlay=(W-w)/2:H-h-15:format=rgb[out]";
            FFmpegFrameFilter frameFilter = new FFmpegFrameFilter(watermark, width, height);
            frameFilter.setPixelFormat(avutil.AV_PIX_FMT_BGR24);
            frameFilter.start();

            // Does the read / write
            frameRecorder.start();
            while (true) {
                Frame frame = frameGrabber.grab();
                if (frame != null) {
                    // Adding the WaterMark
                    frameFilter.push(frame);
                    Frame filteredFrame = frameFilter.pull();
                    frameRecorder.record(filteredFrame);
                } else {
                    break;
                }
            }
            frameRecorder.setMetadata(frameGrabber.getMetadata());
            // Stops the recorder
            frameRecorder.stop();
            frameRecorder.release();
            // Stops the filter
            frameFilter.stop();
            frameFilter.release();
            // Stops the reader (grabber)
            frameGrabber.stop();
            frameGrabber.release();

        }
    }


    /**
     * 逐帧图像处理，转成BufferedImage处理，可以加字幕，加水印，输出视频质量有下降
     */
    @Test
    public void test2() throws IOException {
        String inPath = "/Users/chengtianlong/Desktop/test.mp4";
        String outpath = "/Users/chengtianlong/Desktop/test-1.mp4";
        String qrcodefilepath = "/Users/chengtianlong/Desktop/water1.png";
        try {
            Java2DFrameConverter converter = new Java2DFrameConverter();

            FFmpegFrameGrabber qrGrab = new FFmpegFrameGrabber(qrcodefilepath);
            qrGrab.start();
            BufferedImage qrbufimg = converter.getBufferedImage(qrGrab.grabImage());

            // 源视频
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inPath);
            grabber.start();
            // 输出视频
            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outpath, grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());

            // 视频相关配置，取原视频配置
            recorder.setFrameRate(grabber.getFrameRate());
            recorder.setVideoCodec(grabber.getVideoCodec());
            recorder.setVideoBitrate(grabber.getVideoBitrate());
            recorder.setTimestamp(grabber.getTimestamp());

            // 音频相关配置，取原音频配置
            recorder.setSampleRate(grabber.getSampleRate());
            recorder.setAudioCodec(grabber.getAudioCodec());
            recorder.start();

            System.out.println("准备开始推流...");
            Frame frame;
            int frameCount = 0;
            while ((frame = grabber.grabFrame()) != null) {
                frameCount++;
                // 从视频帧中获取图片
                if (frame.image != null) {
                    IplImage iplImage = Java2DFrameUtils.toIplImage(frame);
                    BufferedImage bufferedImage = Java2DFrameUtils.toBufferedImage(iplImage);

//					BufferedImage bufferedImage = converter.getBufferedImage(frame);

                    // 对图片进行文本合入
//					mark1(bufferedImage, qrbufimg);

                    // 视频帧赋值，写入输出流
//					Frame resultFrame = Java2DFrameUtils.toFrame(bufferedImage);

                    IplImage iplImage1 = Java2DFrameUtils.toIplImage(bufferedImage);
                    Frame resultFrame = Java2DFrameUtils.toFrame(iplImage1);

//					Frame resultFrame = converter.getFrame(bufferedImage);

                    recorder.record(resultFrame);
                }
                // 音频帧写入输出流
                if (frame.samples != null) {
                    recorder.recordSamples(frame.sampleRate, frame.audioChannels, frame.samples);
                }
            }
            System.out.println("推流结束...");
            System.out.println("帧数：" + frameCount);

            recorder.close();
            grabber.close();
            qrGrab.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage mark1(BufferedImage bufferedImage, BufferedImage qrbufimg) {
        Graphics2D graphics = bufferedImage.createGraphics();

        graphics.drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);

//		graphics.setColor(Color.WHITE);
//		graphics.setFont(new Font("微软雅黑", Font.BOLD, 30));
//		graphics.drawString("《狂飙狂飙》",(bufferedImage.getWidth() - 180) / 2,bufferedImage.getHeight() - 60);

//		graphics.drawImage(qrbufimg, (bufferedImage.getWidth() - qrbufimg.getWidth()) / 2, bufferedImage.getHeight() - qrbufimg.getHeight() - 10,
//				qrbufimg.getWidth(), qrbufimg.getHeight(), null);

        graphics.dispose();
        return bufferedImage;
    }

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 图片添加文本
     *
     * @param bufImg
     * @param subTitleContent
     * @return
     */
    private static BufferedImage addSubtitle(BufferedImage bufImg, String subTitleContent) {

        // 添加字幕时的时间
        Font font = new Font("微软雅黑", Font.BOLD, 32);
        String timeContent = sdf.format(new Date());
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        Graphics2D graphics = bufImg.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        //设置图片背景
        graphics.drawImage(bufImg, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null);

        //设置左上方时间显示
        graphics.setColor(Color.orange);
        graphics.setFont(font);
        graphics.drawString(timeContent, 0, metrics.getAscent());

        // 计算文字长度，计算居中的x点坐标
        int textWidth = metrics.stringWidth(subTitleContent);
        int widthX = (bufImg.getWidth() - textWidth) / 2;
        graphics.setColor(Color.red);
        graphics.setFont(font);
        graphics.drawString(subTitleContent, widthX, bufImg.getHeight() - 100);
        graphics.dispose();
        return bufImg;
    }


    /**
     * 生成背景透明的 文字水印，文字位于透明区域正中央，可设置旋转角度
     *
     * @param width 生成图片宽度
     * @param height 生成图片高度
     * @param text 水印文字
     * @param color 颜色对象
     * @param font awt字体
     * @param degree 水印文字旋转角度
     * @param alpha 水印不透明度0f-1.0f
     */
    public static BufferedImage waterMarkByText(int width, int height, String text, Color color,
                                                Font font, Double degree, float alpha) {
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = buffImg.createGraphics();

        g2d.setBackground(Color.WHITE);//设置背景色
        g2d.clearRect(0, 0, width, height);//通过使用当前绘图表面的背景色进行填充来清除指定的矩形。

        // ----------  增加下面的代码使得背景透明  -----------------
//        buffImg = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
//        g2d.dispose();
//        g2d = buffImg.createGraphics();
        // ----------  背景透明代码结束  -----------------

        // 设置对线段的锯齿状边缘处理，使用双线性插值，可以在绘制过程中产生更平滑的效果，减少锯齿状边缘和像素化的外观。
//        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        //消除文字锯齿
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //消除画图锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //把源图片写入
//            g2d.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0,null);
        // 设置水印旋转
        if (null != degree) {
            //注意rotate函数参数theta，为弧度制，故需用Math.toRadians转换一下
            //以矩形区域中央为圆心旋转
            g2d.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
        }
        // 设置 Font
        g2d.setFont(font);
        //设置透明度:1.0f为透明度 ，值从0-1.0，依次变得不透明
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        // 计算文字长度，计算居中的x点坐标 ，即字符串左边位置
        FontMetrics fm = g2d.getFontMetrics(font);
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int x = (width - textWidth) / 2;
        // y坐标位置为：指字体所在矩形的左上角y坐标+ascent(基线-升部线的距离)
        // 原本字体基线位置对准画布的y坐标导致字体偏上ascent距离，加上ascent后下移刚好顶边吻合
        int y = (height - textHeight) / 2 + fm.getAscent();

        // 创建文字形状
        Shape textShape = font.createGlyphVector(g2d.getFontRenderContext(), text).getOutline();
        g2d.translate(x, y);
        g2d.setColor(color);
        g2d.fill(textShape);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(textShape);


        //取绘制的字串宽度、高度中间点进行偏移，使得文字在图片坐标中居中
//        g2d.drawString(text, x, y);
        //释放资源
        g2d.dispose();
        return buffImg;
    }

    @Test
    public void watermarkStroke() throws IOException, FontFormatException {
        int width = 600;
        int height = 200;
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffImg.createGraphics();

        g2d.setBackground(Color.WHITE);//设置背景色
        g2d.clearRect(0, 0, width, height);//通过使用当前绘图表面的背景色进行填充来清除指定的矩形。

        //消除文字锯齿
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //消除画图锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);


        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/chengtianlong/Desktop/字体/Alimama_ShuHeiTi_Bold.ttf"));
        font = font.deriveFont(Font.BOLD | Font.ITALIC, 60);

        g2d.setFont(font);
        Shape shape = font.createGlyphVector(g2d.getFontRenderContext(), "《精彩立即观看》").getOutline();

        Rectangle2D bounds = shape.getBounds2D();
        double x = (width - bounds.getWidth()) / 2 - bounds.getX();
        double y = (height - bounds.getHeight()) / 2 - bounds.getY();
        AffineTransform translate = AffineTransform.getTranslateInstance(x, y);
        Shape transformedShape = translate.createTransformedShape(shape);

        g2d.setColor(new Color(246,228,59));
        g2d.fill(transformedShape);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(transformedShape);

        g2d.dispose();

        ImageIO.write(buffImg, "png", new File("/Users/chengtianlong/Desktop/test-1.png"));//写入文件
    }

    private static BufferedImage scaleImage(BufferedImage originalImage, int newWidth, int newHeight) {
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D graphics = scaledImage.createGraphics();
        //消除文字锯齿
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //消除画图锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        graphics.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        graphics.dispose();
        return scaledImage;
    }

    @Test
    public void watermark() throws IOException, FontFormatException {
        int width = 600;
        int heigth = 200;
//        Font font = new Font("微软雅黑", Font.ROMAN_BASELINE, 20);//字体
//        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/chengtianlong/Desktop/字体/字魂扁桃体.ttf"));
//        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/chengtianlong/Desktop/字体/云峰寒蝉体.ttf"));
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/chengtianlong/Desktop/字体/Alimama_ShuHeiTi_Bold.ttf"));
        font = font.deriveFont(Font.BOLD | Font.ITALIC, 60);
        BufferedImage bi1 = waterMarkByText(width, heigth, "《精彩立即观看》", new Color(246,228,59), font, null, 1.0f);//给图片添加文字水印
        try {
            ImageIO.write(bi1, "png", new File("/Users/chengtianlong/Desktop/test-1.png"));//写入文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void allFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment ();
        String [] fontList = ge.getAvailableFontFamilyNames ();
        System.out.println(Arrays.toString(fontList));
    }

    public static void main(String[] args) throws IOException, FontFormatException {

        JFrame frame = new JFrame();
        frame.setSize(600, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        Font font = new Font("微软雅黑", Font.BOLD | Font.ITALIC, 60);
//        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/chengtianlong/Desktop/字体/字魂扁桃体.ttf"));
//        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/chengtianlong/Desktop/字体/云峰寒蝉体.ttf"));
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/chengtianlong/Desktop/字体/Alimama_ShuHeiTi_Bold.ttf"));
//        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/chengtianlong/Desktop/字体/yahei.ttc"));
        font = font.deriveFont(Font.BOLD | Font.ITALIC, 60);
        Color textColor = new Color(246,228,59);
        Color strokeColor = Color.BLACK;

        TextComponent textComponent = new TextComponent("《精彩立即观看》", font, textColor, strokeColor);
        frame.add(textComponent);

        frame.setVisible(true);



        //得到窗口内容面板
        Container content=frame.getContentPane();
        //创建缓冲图片对象
        BufferedImage img=new BufferedImage(
                frame.getWidth(),frame.getHeight(),BufferedImage.TYPE_INT_RGB);
        //得到图形对象
        Graphics2D g2d = img.createGraphics();
        //将窗口内容面板输出到图形对象中
        content.printAll(g2d);

        Iterator<ImageWriter> iter = ImageIO
                .getImageWritersByFormatName("png");

        ImageWriter imageWriter = iter.next();
        ImageWriteParam wp = imageWriter.getDefaultWriteParam();

        //保存为图片
        File f=new File("/Users/chengtianlong/Desktop/saveScreen.jpg");
        try {
            ImageIO.write(img, "jpg", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //释放图形对象
        g2d.dispose();

    }


}
