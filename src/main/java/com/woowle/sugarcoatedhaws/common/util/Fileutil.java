package com.woowle.sugarcoatedhaws.common.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.woowle.sugarcoatedhaws.common.util.CommonUtil.FloatUtil;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.ImageIcon;

/**
 * Create By xiaoyin.lu.o on 2018/12/11
 **/
public class Fileutil {
  /**
   * 缩放图片(压缩图片质量，改变图片尺寸)
   * 若原图宽度小于新宽度，则宽度不变！
   * @param newWidth 新的宽度
   * @param quality 图片质量参数 0.7f 相当于70%质量
   */
  public static void imageResize(File originalFile, File resizedFile,
      int maxWidth,int maxHeight, float quality) throws IOException {
    quality = FloatUtil.setDeNum(quality,2);

    if (!FloatUtil.in(quality,0,1)) {
      throw new IllegalArgumentException(
          "图片质量需设置在0.1-1范围");
    }

    ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
    Image i = ii.getImage();
    Image resizedImage = null;

    int iWidth = i.getWidth(null);
    int iHeight = i.getHeight(null);

    int newWidth = maxWidth;
    if(iWidth < maxWidth){
      newWidth = iWidth;
    }


    if (iWidth >= iHeight) {
      resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight)
          / iWidth, Image.SCALE_SMOOTH);
    }



    int newHeight = maxHeight;
    if(iHeight < maxHeight){
      newHeight = iHeight;
    }

    if(resizedImage==null && iHeight >= iWidth){
      resizedImage = i.getScaledInstance((newHeight * iWidth) / iHeight,
          newHeight, Image.SCALE_SMOOTH);
    }

    // This code ensures that all the pixels in the image are loaded.
    Image temp = new ImageIcon(resizedImage).getImage();

    // Create the buffered image.
    BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),
        temp.getHeight(null), BufferedImage.TYPE_INT_RGB);

    // Copy image to buffered image.
    Graphics g = bufferedImage.createGraphics();

    // Clear background and paint the image.
    g.setColor(Color.white);
    g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
    g.drawImage(temp, 0, 0, null);
    g.dispose();

    // Soften.
    float softenFactor = 0.05f;
    float[] softenArray = { 0, softenFactor, 0, softenFactor,
        1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0 };
    Kernel kernel = new Kernel(3, 3, softenArray);
    ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    bufferedImage = cOp.filter(bufferedImage, null);

    // Write the jpeg to a file.
    FileOutputStream out = new FileOutputStream(resizedFile);

    // Encodes image as a JPEG data stream
    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

    JPEGEncodeParam param = encoder
        .getDefaultJPEGEncodeParam(bufferedImage);

    param.setQuality(quality, true);

    encoder.setJPEGEncodeParam(param);
    encoder.encode(bufferedImage);
  } // Example usage




}
