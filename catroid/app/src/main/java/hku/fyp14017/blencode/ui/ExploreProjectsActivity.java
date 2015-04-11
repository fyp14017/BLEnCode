package hku.fyp14017.blencode.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.ui.adapter.MyArrayAdapter;
import hku.fyp14017.blencode.utils.UtilZip;
import hku.fyp14017.blencode.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

public class ExploreProjectsActivity extends Activity {
    static MyArrayAdapter adapter;
    static List<String> data = new ArrayList<String>();
    static List<Bitmap> screenshots = new ArrayList<Bitmap>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_projects);
        data.clear();
        adapter = new MyArrayAdapter(this, data, screenshots);
        ListView list = (ListView) findViewById(R.id.listView);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final String item = (String)parent.getItemAtPosition(position);
                final String[] temp = item.split("oCISxD");
                final String projectName = temp[0].split("\\(")[0].trim();
                //Toast.makeText(ExploreProjectsActivity.this, item, Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExploreProjectsActivity.this);
                alertDialogBuilder.setMessage("Do you want to download the project \""+ projectName +"\" ?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener (){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(ExploreProjectsActivity.this, "Okay", Toast.LENGTH_SHORT).show();
                        String id = temp[2];
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("test3");
                        query.whereEqualTo("objectId", id);
                        ParseObject zipObject = null;
                        try {
                            zipObject = query.getFirst();
                            ParseFile zipFile = (ParseFile)zipObject.get("zipFileData");
                            zipFile.getDataInBackground(new GetDataCallback() {
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        // data has the bytes for the file
                                        try {
                                            String filePath = Utils.buildPath(Constants.TMP_PATH, "down.catrobat");
                                            File file = new File(filePath);
                                            file.getParentFile().mkdirs();
                                            FileOutputStream fos = new FileOutputStream(file);
                                            fos.write(data);
                                            fos.close();
                                            UtilZip.unZipFile(filePath, Utils.buildProjectPath(projectName));
                                            Toast.makeText(ExploreProjectsActivity.this, "Project "+ projectName +" has been downloaded locally.",Toast.LENGTH_SHORT ).show();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }

                                    } else {
                                        // something went wrong
                                        Toast.makeText(ExploreProjectsActivity.this, "Unable to download "+projectName,Toast.LENGTH_SHORT ).show();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener (){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialogBuilder.create().show();
            }
        });
        list.setAdapter(adapter);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("test3");
        //query.selectKeys(Arrays.asList("objectId", "description","projectName", "username", "createdAt"));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> projectList, ParseException e) {
                if (e == null) {
                    Log.d("dev", "Retrieved " + projectList.size() + " scores");
                    //Toast.makeText(getBaseContext(),"retrieved"+projectList.size(),Toast.LENGTH_SHORT).show();
                    for(ParseObject p : projectList){
                        String[] tempData = new String[3];
                        tempData[0] = p.getString("projectName") + " (" + p.getString("username")+")";
                        tempData[1] = p.getString("description");
                        tempData[2] = p.getObjectId();
                        final Bitmap bmp;

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("test3");
                        query.whereEqualTo("objectId", tempData[2]);
                        ParseObject imageObject = null;
                        try {
                            imageObject = query.getFirst();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        ParseFile image = (ParseFile) imageObject.get("screenshot");
                        byte[] imageData = null;
                        try {
                            imageData = image.getData();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        final String singleTempData = tempData[0] + "oCISxD" +tempData[1] + "oCISxD" +tempData[2];
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addItem(singleTempData,bmp);
                            }
                        });
                    }
                } else {
                    Log.d("dev", "Error: " + e.getMessage());
                }
            }
        });
    }
    public static void addItem(String s, Bitmap bmp){
        //data.clear();
        data.add(s);
        adapter.notifyDataSetChanged();
        screenshots.add(bmp);
        adapter.notifyDataSetChanged();
    }
}
