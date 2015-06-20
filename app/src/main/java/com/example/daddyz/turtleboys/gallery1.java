package com.example.daddyz.turtleboys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.daddyz.turtleboys.subclasses.Camera;
import com.example.daddyz.turtleboys.subclasses.GigUser;
import com.parse.LogOutCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gregory Hooks Jr on 6/16/2015.
 */
public class gallery1 extends AppCompatActivity {
    private Toolbar toolbar;
    private Button b1;
    private Uri fileUri;
    private GridView gridview;
    private AsyncTaskLoadFiles myAsyncTaskLoadFiles;
    private ImageAdapter myImageAdapter;
    private Pattern pattern = Pattern.compile("(IMG.*)");
    private HashMap<Integer, Bitmap> finishedPictures;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event Pictures");

        //Set up back arrow icon on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        b1 = (Button) findViewById(R.id.camerabutton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create Intent to take a picture and return control to the calling application
                Camera camera = new Camera(gallery1.this);
                fileUri = camera.startCamera();
            }
        });

        //Prepare Gallery View
        gridview = (GridView) findViewById(R.id.gridview);
        myImageAdapter = new ImageAdapter(this);
        gridview.setAdapter(myImageAdapter);
        finishedPictures = new HashMap<>();

  /*
   * Move to asyncTaskLoadFiles String ExternalStorageDirectoryPath =
   * Environment .getExternalStorageDirectory() .getAbsolutePath();
   *
   * String targetPath = ExternalStorageDirectoryPath + "/test/";
   *
   * Toast.makeText(getApplicationContext(), targetPath,
   * Toast.LENGTH_LONG).show(); File targetDirector = new
   * File(targetPath);
   *
   * File[] files = targetDirector.listFiles(); for (File file : files){
   * myImageAdapter.add(file.getAbsolutePath()); }
   */
        myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
        myAsyncTaskLoadFiles.execute();

        gridview.setOnItemClickListener(myOnItemClickListener);

        /*Button buttonReload = (Button)findViewById(R.id.reload);
        buttonReload.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {

                //Cancel the previous running task, if exist.
                myAsyncTaskLoadFiles.cancel(true);

                //new another ImageAdapter, to prevent the adapter have
                //mixed files
                myImageAdapter = new ImageAdapter(MainActivity.this);
                gridview.setAdapter(myImageAdapter);
                myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
                myAsyncTaskLoadFiles.execute();
            }});*/
    }

    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position,
                                long id) {
            final ImageView enlargedPicture = new ImageView(gallery1.this);
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(gallery1.this);
            final String title = (String) parent.getItemAtPosition(position);
            Matcher matcher = pattern.matcher(title);
            if(matcher.find() == false){
                return;
            }
            alertDialog.setTitle(matcher.group(1));
            alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, int which) {
                    final AlertDialog.Builder confirmDialog = new AlertDialog.Builder(gallery1.this);
                    confirmDialog.setTitle("Confirm");
                    confirmDialog.setMessage("Are you sure you want to delete this picture?");
                    confirmDialog.setCancelable(false);
                    confirmDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog2, int which) {
                            dialog2.dismiss();
                        }
                    });
                    confirmDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog2, int which) {
                            File picture = new File(title);
                            picture.delete();
                            myImageAdapter.remove(position);
                            myImageAdapter.notifyDataSetChanged();
                            finishedPictures.clear();
                            myAsyncTaskLoadFiles.cancel(true);

                            //new another ImageAdapter, to prevent the adapter have
                            //mixed files
                            myImageAdapter = new ImageAdapter(gallery1.this);
                            gridview.setAdapter(myImageAdapter);
                            myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
                            myAsyncTaskLoadFiles.execute();
                            Toast.makeText(getApplicationContext(), "Picture deleted!", Toast.LENGTH_SHORT);
                            return;
                        }
                    });
                    AlertDialog alert2 = confirmDialog.create();
                    alert2.show();
                }
            });
            alertDialog.setNegativeButton("Share", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Toast.makeText(getApplicationContext(), "Picture share feature not yet implemented", Toast.LENGTH_LONG).show();
                }
            });

            //Prepare Picture for Larger View
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap picture = BitmapFactory.decodeFile(title, options);

            try {
                ExifInterface exif = new ExifInterface(title);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                }
                else if (orientation == 3) {
                    matrix.postRotate(180);
                }
                else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                picture = Bitmap.createBitmap(picture, 0, 0, picture.getWidth(), picture.getHeight(), matrix, true); // rotating bitmap
            }
            catch (Exception e) {

            }

            enlargedPicture.setImageBitmap(picture);
            alertDialog.setView(enlargedPicture);

            AlertDialog alert = alertDialog.create();
            alert.show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //Update MediaStore with new file (ex:  Picture shows up in Gallery)
        Intent broadcast = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        broadcast.setData(fileUri);
        sendBroadcast(broadcast);

        //Update Gallery List
        //Cancel the previous running task, if exist.
        myAsyncTaskLoadFiles.cancel(true);

        //new another ImageAdapter, to prevent the adapter have
        //mixed files
        myImageAdapter = new ImageAdapter(this);
        gridview.setAdapter(myImageAdapter);
        myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
        myAsyncTaskLoadFiles.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class AsyncTaskLoadFiles extends AsyncTask<Void, String, Void> {

        File targetDirector;
        ImageAdapter myTaskAdapter;

        public AsyncTaskLoadFiles(ImageAdapter adapter) {
            myTaskAdapter = adapter;
        }

        @Override
        protected void onPreExecute() {
            String ExternalStorageDirectoryPath = Environment
                    .getExternalStorageDirectory().getAbsolutePath();

            //String targetPath = ExternalStorageDirectoryPath + "/test/";
            String targetPath = ExternalStorageDirectoryPath + "/Pictures/GigIT";
            targetDirector = new File(targetPath);
            myTaskAdapter.clear();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            File[] files = targetDirector.listFiles();
            Arrays.sort(files, Collections.reverseOrder());
            for (File file : files) {
                publishProgress(file.getAbsolutePath());
                if (isCancelled()) break;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            myTaskAdapter.add(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            myTaskAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }

    public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        ArrayList<String> itemList = new ArrayList<String>();

        public ImageAdapter(Context c) {
            mContext = c;
        }

        void add(String path) {
            itemList.add(path);
        }

        void clear() {
            itemList.clear();
        }

        void remove(int index){
            itemList.remove(index);
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        //getView load bitmap in AsyncTask
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            ImageView imageView;
            if (convertView == null) { // if it's not recycled, initialize some
                // attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(512, 512));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);

                convertView = imageView;

                holder = new ViewHolder();
                holder.image = imageView;
                holder.position = position;
                convertView.setTag(holder);
            } else {
               //imageView = (ImageView) convertView;
                holder = (ViewHolder) convertView.getTag();
                holder.position = position;
                ((ImageView)convertView).setImageBitmap(finishedPictures.get(position));
            }

            //Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220, 220);
            // Using an AsyncTask to load the slow images in a background thread
            new AsyncTask<ViewHolder, Void, Bitmap>() {
                private ViewHolder v;

                @Override
                protected Bitmap doInBackground(ViewHolder... params) {
                    v = params[0];
                    if(itemList.size() <= position){
                        return null;
                    }
                    Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 512, 512);
                    //Rotate images to correct orientation
                    try {
                        ExifInterface exif = new ExifInterface(itemList.get(position));
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        }
                        else if (orientation == 3) {
                            matrix.postRotate(180);
                        }
                        else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true); // rotating bitmap
                    }
                    catch (Exception e) {
                        return bm;
                    }
                    return bm;
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);

                    if (v.position == position) {
                        finishedPictures.put(position, result);
                        v.image.setImageBitmap(result);
                    }

                }
            }.execute(holder);

            //imageView.setImageBitmap(bm);
            //return imageView;
            return convertView;
        }

        public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
                                                 int reqHeight) {

            Bitmap bm = null;
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(path, options);

            return bm;
        }

        public int calculateInSampleSize(

                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round((float) height
                            / (float) reqHeight);
                } else {
                    inSampleSize = Math.round((float) width / (float) reqWidth);
                }
            }

            return inSampleSize;
        }

        class ViewHolder {
            ImageView image;
            int position;
        }

    }

        public int calculateInSampleSize(

                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round((float) height
                            / (float) reqHeight);
                } else {
                    inSampleSize = Math.round((float) width / (float) reqWidth);
                }
            }

            return inSampleSize;
        }
    }
