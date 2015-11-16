package info.aric.android.fishpet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aric on 7/27/2015.
 */
public class LearnActivity extends AppCompatActivity {
    public final static String CURRENT_LESSON = "info.aric.android.fishpet.CURRENT_LESSON";

    int cLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        //Determine which lesson this is
        Intent getIntent = getIntent();
        String previous = getIntent.getStringExtra(LearnActivity.CURRENT_LESSON);

        final int previousLesson = Integer.parseInt(previous);
        final int currentLesson = previousLesson+1;
        cLesson = currentLesson;
        Log.v("LearnActivity", "Loaded lesson " + currentLesson);


        //Setup Next button
        Button nextButton = (Button) findViewById(R.id.nextButton);
        if(cLesson==5)
        {
            nextButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), CongratsActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            nextButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), LearnActivity.class);
                    intent.putExtra(CURRENT_LESSON, Integer.toString(currentLesson));
                    startActivity(intent);
                }
            });
        }


        //Find the Views we need to modify
        ImageView image = (ImageView) this.findViewById(R.id.lesson_image);
        TextView title = (TextView) this.findViewById(R.id.lesson_title);
        TextView description = (TextView) this.findViewById(R.id.lesson_description);
        TextView photoDesc = (TextView) this.findViewById(R.id.lesson_photoDesc);

        //Switch content based on lesson
        switch(currentLesson){
            case 1:
                Log.v("LearnActivity", "Load 1");
                image.setImageResource(R.drawable.species);
                title.setText(getString(R.string.lesson_1_title));
                description.setText(getString(R.string.lesson_1_description));
                photoDesc.setText(getString(R.string.lesson_1_photoDesc));
                break;

            case 2:
                Log.v("LearnActivity", "Load 2");
                image.setImageResource(R.drawable.aquarium);
                title.setText(getString(R.string.lesson_2_title));
                description.setText(getString(R.string.lesson_2_description));
                photoDesc.setText(getString(R.string.lesson_2_photoDesc));
                break;

            case 3:
                Log.v("LearnActivity", "Load 3");
                image.setImageResource(R.drawable.wash);
                title.setText(getString(R.string.lesson_3_title));
                description.setText(getString(R.string.lesson_3_description));
                photoDesc.setText(getString(R.string.lesson_3_photoDesc));
                break;

            case 4:
                Log.v("LearnActivity", "Load 4");
                image.setImageResource(R.drawable.water);
                title.setText(getString(R.string.lesson_4_title));
                description.setText(getString(R.string.lesson_4_description));
                photoDesc.setText(getString(R.string.lesson_4_photoDesc));
                break;

            case 5:
                Log.v("LearnActivity", "Load 5");
                image.setImageResource(R.drawable.earl);
                title.setText(getString(R.string.lesson_5_title));
                description.setText(getString(R.string.lesson_5_description));
                photoDesc.setText(getString(R.string.lesson_5_photoDesc));
                break;
        }

        generateThumb();
    }

    //Capture button action
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    public void capturePhoto(View v)
    {
        dispatchTakePictureIntent();
    }



    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Lesson" + cLesson+"_"+timeStamp + "_";

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + "/FishPet");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.v("LearnActivity",ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }

    // Return after photo taken
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            generateThumb();
        }
    }

    // Create thumbnail
    private void generateThumb()
    {
        // create new filename filter
        FilenameFilter fileNameFilter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if(name.length()>10)
                {

                    String lesson = name.substring(6,7);
                    String date = name.substring(8);

                    Log.v("LearnActivity","Lesson="+lesson);
                    Log.v("LearnActivity","Date="+date);

                    if(Integer.parseInt(lesson)==cLesson)
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
        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles(fileNameFilter);

        if (file!= null) {
            Log.d("Files", "Size: " + file.length);
            for (int i=0; i < file.length; i++)
            {
                Log.d("Files", "FileName:" + file[i].getName());
            }

            if(file.length>0)
            {
                String thumbFilePath = file[(file.length-1)].getAbsolutePath();

                Log.v("LearnAcivity", "Creating thumb (createThumb) for " + thumbFilePath);

                ImageView mImageView = (ImageView) findViewById(R.id.capturedPhotoImageView);
                int THUMBSIZE = 250;

                try {
                    Bitmap bmpThumbImage = ImageManipulation.loadOrientationBitmap(thumbFilePath,
                            THUMBSIZE, THUMBSIZE, true);
                    mImageView.setImageBitmap(bmpThumbImage);

                    final String filePath = thumbFilePath;
                    mImageView.setOnClickListener(new Button.OnClickListener() {
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
                    Log.v("LearnActivity",e.toString());
                }
            }
        }
    }
}
