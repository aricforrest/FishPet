package info.aric.android.fishpet;

import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Aric on 11/14/2015.
 */
public class ImageManipulation {

    public static int getCameraPhotoOrientation(String imagePath){
        int rotate = 0;
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            //Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    // Several of these methods sourced from http://stackoverflow.com/questions/13226907/bitmap-to-large
    public static int getScale(int originalWidth,int originalHeight,
                               final int requiredWidth,final int requiredHeight)
    {
        int scale=1;

        if((originalWidth>requiredWidth) || (originalHeight>requiredHeight))
        {
            if(originalWidth<originalHeight)
                scale=Math.round((float)originalWidth/requiredWidth);
            else
                scale=Math.round((float)originalHeight/requiredHeight);

        }
        return scale;
    }

    public static BitmapFactory.Options getOptions(String filePath,
                                                   int requiredWidth,int requiredHeight)
    {

        BitmapFactory.Options options=new BitmapFactory.Options();
        //setting inJustDecodeBounds to true
        //ensures that we are able to measure
        //the dimensions of the image,without
        //actually allocating it memory
        options.inJustDecodeBounds=true;

        //decode the file for measurement
        BitmapFactory.decodeFile(filePath,options);

        //obtain the inSampleSize for loading a
        //scaled down version of the image.
        //options.outWidth and options.outHeight
        //are the measured dimensions of the
        //original image
        options.inSampleSize=getScale(options.outWidth,
                options.outHeight, requiredWidth, requiredHeight);

        //set inJustDecodeBounds to false again
        //so that we can now actually allocate the
        //bitmap some memory
        options.inJustDecodeBounds=false;

        return options;

    }

    public static Bitmap loadBitmap(String filePath,
                                    int requiredWidth,int requiredHeight){

        BitmapFactory.Options options = getOptions(filePath,
                requiredWidth, requiredHeight);
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap loadOrientationBitmap(String filePath,
                                    int requiredWidth,int requiredHeight, boolean square){

        Bitmap bitmapImage = ImageManipulation.loadBitmap(filePath, requiredWidth, requiredHeight);

        //Get correct rotation of image and rotate matrix
        int rotateImage = ImageManipulation.getCameraPhotoOrientation(filePath);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateImage);

        if (square)
        {
            if (bitmapImage.getWidth() >= bitmapImage.getHeight()){
                bitmapImage = Bitmap.createBitmap(bitmapImage,
                        bitmapImage.getWidth()/2 - bitmapImage.getHeight()/2,
                        0,
                        bitmapImage.getHeight(),
                        bitmapImage.getHeight(),
                        matrix,
                        true);

            }else{
                bitmapImage = Bitmap.createBitmap(bitmapImage,
                        0,
                        bitmapImage.getHeight()/2 - bitmapImage.getWidth()/2,
                        bitmapImage.getWidth(),
                        bitmapImage.getWidth(),
                        matrix,
                        true);
            }

        } else {
            bitmapImage = Bitmap.createBitmap(bitmapImage, 0, 0, bitmapImage.getWidth(),
                    bitmapImage.getHeight(), matrix, true);
        }

        return bitmapImage;

    }
}
