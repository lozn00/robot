package cn.qssq666.robot.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RegExUtils;

import java.io.File;

import cn.qssq666.robot.app.AppContext;

public class ZxingUtil {
    /**
     * 生成二维码bitmap
     */
    public static Bitmap createBitmap(String str) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, DensityUtil.dip2px(AppContext.getContext(), 300), DensityUtil.dip2px(AppContext.getContext(), 300));
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) {
            return null;
        }
        return bitmap;
    }

    public static String bitmap2qrcodeFile(String text) {
        String pathk = RegexUtils.removePunctuation(text);
        File picRootdir = PicPathUtil.getPicRootdir();
        if (!picRootdir.exists()) {
            picRootdir.mkdirs();
        }
        String path = new File(picRootdir, Md5Utils.encode(pathk) + pathk + ".jpg").getAbsolutePath();
        String s = bitmap2qrcodeFile(text, path);
        return path;
    }

    public static String bitmap2qrcodeFile(String text, String pathk1) throws RuntimeException{
        Bitmap bitmap = createBitmap(text);
        if(bitmap==null){
            throw new RuntimeException("无法生成二维码");
        }

//        String fileName=Md5Utils.encode(pathk)+""+ RegExUtils.;
        ImageUtil.saveBitmap(bitmap, pathk1);
        return pathk1;
    }
}