package org.catrobat.catroid.web;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.*;

import org.catrobat.catroid.R;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.utils.StatusBarNotificationManager;
import org.catrobat.catroid.utils.UtilZip;
import org.catrobat.catroid.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by User HP on 9/3/2015.
 */
public class ParseProjectUploadManager {
    private final Context context;

    public ParseProjectUploadManager(Context c) {
        context = c;
    }

    public void uploadProject(){
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user

            AlertDialog.Builder uploadCurrentProject = new AlertDialog.Builder(context);
            LayoutInflater li = LayoutInflater.from(context);
            View uploadView = li.inflate(R.layout.parse_upload, null);
            final EditText name = (EditText) uploadView.findViewById(R.id.editText4);
            final EditText description = (EditText) uploadView.findViewById(R.id.editText5);
            uploadCurrentProject.setTitle("Upload")
                    .setView(uploadView)
                    .setPositiveButton("Upload" , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int notificationId = StatusBarNotificationManager.getInstance().createUploadNotification(context,
                                    name.getText().toString());
                            String projectName = Utils.getCurrentProjectName(context);
                            String projectPath = Constants.DEFAULT_ROOT + "/" + projectName;
                            if (projectPath == null) {
                                Log.d("dev", "project path is null");
                                return;
                            }

                            File directoryPath = new File(projectPath);
                            String[] paths = directoryPath.list();

                            if (paths == null) {
                                Log.d("dev", "project path is not valid");
                                return;
                            }

                            for (int j = 0; j < paths.length; j++) {
                                paths[j] = Utils.buildPath(directoryPath.getAbsolutePath(), paths[j]);
                            }

                            String zipFileString = Utils.buildPath(Constants.TMP_PATH, "upload.catrobat");
                            File zipFile = new File(zipFileString);
                            if (!zipFile.exists()) {
                                zipFile.getParentFile().mkdirs();
                                try {
                                    zipFile.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!UtilZip.writeToZipFile(paths, zipFileString)) {
                                Log.d("dev", "error writing to zip file");
                                zipFile.delete();
                            }
                            File file = new File(zipFileString);
                            byte[] fileData = new byte[(int) file.length()];
                            FileInputStream fis = null;
                            try {
                                fis = new FileInputStream(file);
                                fis.read(fileData);
                                fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            ParseFile pf = new ParseFile(name.getText().toString() + ".zip", fileData);
                            try {
                                pf.save();
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            ParseObject parseZipFile = new ParseObject("test3");
                            parseZipFile.put("username", currentUser.getUsername());
                            parseZipFile.put("projectName", name.getText().toString());
                            parseZipFile.put("description", description.getText().toString());
                            parseZipFile.put("zipFileData", pf);
                            try {
                                parseZipFile.save();
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            StatusBarNotificationManager.getInstance().showOrUpdateNotification(notificationId, 100);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
        } else {
            AlertDialog.Builder loginDialog = new AlertDialog.Builder(context);
            LayoutInflater li = LayoutInflater.from(context);
            View signInView = li.inflate(R.layout.signin_fields, null);
            final EditText userName = (EditText) signInView.findViewById(R.id.editText);
            final EditText password = (EditText) signInView.findViewById(R.id.editText2);
            loginDialog.setTitle("Sign In")
                    .setView(signInView)
                    .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {
                            ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString(), new LogInCallback() {
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null) {
                                        // Hooray! The user is logged in.
                                        dialogInterface.dismiss();
                                    } else {
                                        // Signup failed. Look at the ParseException to see what happened.
                                        Toast.makeText(context, "Wrong username and/or password",Toast.LENGTH_SHORT);
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton("Sign Up", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            AlertDialog.Builder registerDialog = new AlertDialog.Builder(context);
                            LayoutInflater li = LayoutInflater.from(context);
                            View signUpView = li.inflate(R.layout.signup_fields, null);
                            final EditText userName = (EditText) signUpView.findViewById(R.id.editText);
                            final EditText password = (EditText) signUpView.findViewById(R.id.editText2);
                            final EditText email = (EditText) signUpView.findViewById(R.id.editText3);
                            registerDialog.setTitle("Sign Up")
                                    .setView(signUpView)
                                    .setNeutralButton("Register", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialogInterface, int i) {
                                            ParseUser user = new ParseUser();
                                            user.setUsername(userName.getText().toString());
                                            user.setPassword(password.getText().toString());
                                            user.setEmail(email.getText().toString());

                                            user.signUpInBackground(new SignUpCallback() {
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        // Hooray! Let them use the app now.
                                                        dialogInterface.dismiss();
                                                    } else {
                                                        Toast.makeText(context, "Username/email already exists. Please use unique credentials",Toast.LENGTH_SHORT);
                                                    }
                                                }
                                            });
                                        }
                                    });
                            dialogInterface.dismiss();
                            registerDialog.create().show();


                        }
                    })
                    .setNeutralButton("Reset Password", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();


        }
    }
}
