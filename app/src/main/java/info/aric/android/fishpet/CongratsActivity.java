package info.aric.android.fishpet;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Aric on 7/29/2015.
 */
public class CongratsActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrats);

        // Hide the status bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //Get image filenames
        String[] fileNames = getFileNames();

        for(int i=0; i<5; i++)
        {
            Log.d("CongratsActivity", "Image "+i+" : "+fileNames[i]);
        }

        //Get linear layout
        LinearLayout theLayout = (LinearLayout) this.findViewById(R.id.congratsLinearLayout);

        //Add images to layout
        for(int i=0; i<5; i++)
        {
            if (fileNames[i] != "")
            {
                Log.v("CongratsActivity","Begin adding image "+(i+1));
                Log.v("CongratsActivity","Image: " + fileNames[i]);

                //Create ImageView and add to layout
                ImageView newImageView = new ImageView(this);
                ((LinearLayout) theLayout).addView(newImageView);

                //Add bottom margin to ImageView
                LinearLayout.LayoutParams marginlayout = new LinearLayout.LayoutParams(newImageView.getLayoutParams());
                marginlayout.setMargins(0, 0, 0, 40);
                newImageView.setLayoutParams(marginlayout);

                //Set fileName and create bitmap
                int THUMBSIZE = 500;
                String thumbFilePath = fileNames[i];

                try {
                    //Replace bitmap with rotated
                    Bitmap newImageBitmap = ImageManipulation.loadOrientationBitmap(thumbFilePath,
                            THUMBSIZE, THUMBSIZE, false);
                    newImageView.setImageBitmap(newImageBitmap);

                    // Create open gallery action
                    final String filePath = thumbFilePath;
                    newImageView.setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse("file://" + filePath), "image/*");
                            startActivity(intent);
                        }
                    });

                } catch
                        (NullPointerException e)
                {
                    Log.v("CongratsActivity",e.toString());
                }
            } else {
                Log.v("CongratsActivity","No image for lesson "+(i+1));
            }
        }
    }


    private String[] getFileNames()
    {

        String[] fileNames = new String[5];

        for(int i = 0; i<5; i++)
        {
            fileNames[i]=getLessonFile(i+1);
        }
        return fileNames;
    }

    private String getLessonFile(final int i)
    {
        String filePath="";
        // create new filename filter
        FilenameFilter fileNameFilter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if(name.length()>10)
                {
                    String lesson = name.substring(6,7);
                    if(Integer.parseInt(lesson)==i)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                return false;
            }
        };

        String path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + "/FishPet";
        File f = new File(path);
        File file[] = f.listFiles(fileNameFilter);

        if (file != null)
        {
            Log.d("Files", "Size: " + file.length);
            for (int z=0; z < file.length; z++)
            {
                Log.d("Files", "FileName:" + file[z].getName());
            }

            if(file.length>0) {
                filePath = file[(file.length - 1)].getAbsolutePath();
            }
        }

        return filePath;
    }
}
