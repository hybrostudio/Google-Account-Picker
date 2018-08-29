/**
This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 Unported License. To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

Authors:Vishwas Adiga
        NMD
Version 3
**/

package com.vishwas;

import com.google.appinventor.components.runtime.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.*;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.R.drawable;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.DialogInterface;
import android.app.Dialog;
import android.view.Window;
import java.util.ArrayList;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.SdkLevel;
import com.google.appinventor.components.runtime.util.ElementsUtil;
import com.google.appinventor.components.runtime.util.YailList;

/** NMD **/
import com.google.appinventor.components.runtime.util.MediaUtil;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.content.res.Resources;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.AssetManager;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import android.os.Environment;
import java.io.ByteArrayOutputStream;


@DesignerComponent(version = 3,
    category = ComponentCategory.EXTENSION,
    description = "An extension to show dialogs and pickers. Jointly created by Vishwas Adiga and NMD (www.nmd-apps.jimdo.com/) and licensed under the Creative Commons Attribution-ShareAlike 4.0 Unported License.",
    nonVisible = true,
    iconName = "https://firebasestorage.googleapis.com/v0/b/extensions-3caad.appspot.com/o/Dialog.png?alt=media&token=caf8ec0d-a1bd-4615-90f1-3521a30c9be1")

@SimpleObject(external=true)
public final class Dialogs extends AndroidNonvisibleComponent implements Component {
    
    //NMD UPDATE >>>
	private ComponentContainer container;
    private Context context;
    //NMD UPDATE <<<<
    private static final String LOG_TAG = "Dialogs";
    private final Activity activity;
    private final Handler handler;
    private ProgressDialog progress;
    private ProgressDialog progressl;
    public int currentSelection = -1;
    final ArrayList seletedItems = null;
    public int currentTheme;

    
    
  public Dialogs (ComponentContainer container) {
    super(container.$form());
    //NMD UPDATE >>>
    this.container = container;
    context = (Context) container.$context();
    //NMD UPDATE <<<<
    activity = container.$context();
    handler = new Handler();
    progress = null;
    progressl = null;

  }

/** Display a spinning progress dialog **/
@SimpleFunction(
    description = "Shows a spinning progress dialog which can be dismissed by the user if 'dismissable' is set to true."
    )
public void ShowSpinningProgress(String message, String title, boolean dismissable) {
    progressDialogSpinning(message, title, dismissable);
}

/** Dismisses progress dialog **/
@SimpleFunction(
    description = "Dismiss a previously displayed dialog."
    )
public void DismissProgressDialog() {
    if (progress != null) {
    progress.dismiss();
    progress = null;
    }
    if (progressl != null) {
    progressl.dismiss();
    progressl = null;
    }
}

/** Creates progress dialog
* @param message the message for the dialog
* @param title the title for the dialog
**/
public void progressDialogSpinning(String message, String title, boolean dismissable) {
    if (progress != null) {
        DismissProgressDialog();
    }
    if (progressl != null) {
        DismissProgressDialog();
    }
    progress = new ProgressDialog(activity,currentTheme);
    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progress.setIndeterminate(true);
    progress.setCancelable(dismissable);
    if (currentTheme == 16974393){
        progress.setMessage(message + "                                                                                                                                                                                                                   ");
    }else{
        progress.setMessage(message);
    }
    //Extra spaces to ensure than the spinner stays at the correct width when using the light theme. //
    progress.setTitle(title);
    progress.show();
  }
    
/** Display a linear progress bar **/
@SimpleFunction(
    description = "Shows a progress dialog with a horizontal progress bar. Can be dismissed by user if 'dismissable' is set to true. If indeterminate is true, maxValue and the 'UpdateProgres' method will have no effect."
    )
public void ShowLinearProgress(String message, String title, boolean dismissable, boolean indeterminate, int maxValue){
    progressDialogLinear(message,title,dismissable,indeterminate,maxValue);
}
    
public void progressDialogLinear(String message, String title, boolean dismissable, boolean indeterminate, int max){
    if (progress != null) {
        DismissProgressDialog();
    }
    if (progressl != null) {
        DismissProgressDialog();
    }
    progressl = new ProgressDialog(activity,currentTheme);
    progressl.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressl.setIndeterminate(indeterminate);
    progressl.setCancelable(dismissable);
    progressl.setMessage(message);
    progressl.setTitle(title);
    progressl.setProgress(0);
    progressl.setMax(max);
    progressl.setProgress(0);
    if (indeterminate){
    progressl.setProgressNumberFormat(null);
    progressl.setProgressPercentFormat(null);
    }
    progressl.show();
    
}

@SimpleFunction(
    description = "Sets the current value of the linear progress dialog. Has no effect if 'indeterminate' is set to true."
    )
public void UpdateProgress(int value){
    progressl.setProgress(value);
}
    
/** Displays a single choice picker dialog **/
@SimpleFunction(
    description = "Creates a picker dialog with a list of options of which only one can be chosen."
    )
public void RadioListPicker(String title, YailList options, final String chooseButtonText, boolean cancelable,String cancelButtonText, int selection, String icon){
    Dialog dialog = onCreateDialogSingleChoice(title,options,chooseButtonText,cancelable,cancelButtonText,selection,icon);
    dialog.show();
}
/** Source: https://abhik1987.wordpress.com/2013/02/06/android-alert-dialog-with-single-choice-item-selection-implementation/ **/
    
/** Certain methods and functions are the works of NMD. /NMD [c]2017 www.nmd-apps.jimdo.com/ **/

public Dialog onCreateDialogSingleChoice(String title, YailList options, String buttonText, boolean cancelable,String cancelButtonText, int selection, String icon) {

    ///////NMD Updates+++	
//Custom canceable text
//Custom html title|You can use html tags in the title
//Custom drawable/ user icon
icon = icon == null ? "" : icon;
Bitmap bitmapA;
bitmapA = null;
        try { // load the input bitmaps from Assets
            bitmapA = MediaUtil.getBitmapDrawable(container.$form(), icon).getBitmap();
        } catch (IOException ioe) {
            Log.e("Image", "Unable to load ");
            Toast.makeText(activity,"Error",Toast.LENGTH_SHORT).show();
        }
        
ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        bitmapA.compress(CompressFormat.PNG, 0 , ostream);
        
        File image = new File(Environment.getExternalStorageDirectory()+"/CheckDialogIcon.png");
        try {
            FileOutputStream fostream = new FileOutputStream(image);
            fostream.write(ostream.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return image.getAbsolutePath();
    
BitmapDrawable drawable = new BitmapDrawable(image.getAbsolutePath());
//NMD UPDATE <<<<
selection = selection - 1;
//Initialize the Alert Dialog
AlertDialog.Builder builder = new AlertDialog.Builder(activity,currentTheme);
//Source of the data in the DIalog
String[] array = options.toStringArray();
builder.setCancelable(cancelable);
// Set the dialog title
builder.setTitle(title)
// Specify the list array, the items to be selected by default (null for none),
// and the listener through which to receive callbacks when items are selected
.setSingleChoiceItems(array, selection, new DialogInterface.OnClickListener() {

@Override
public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub

currentSelection = which + 1;

}
});

// Set the action buttons
builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int id) {
// User clicked OK, so save the result somewhere
// or return them to the component that opened the dialog
AfterRadioSelection(currentSelection);
}
});

if (cancelable){
builder.setNegativeButton(Html.fromHtml(cancelButtonText), new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int id) {
AfterRadioSelection(-1);
}
});
}
builder.setIcon(drawable);
return builder.create();
}

@SimpleEvent(
    description = "Event invoked when user has selected an option from the radio button picker. Outputs the index of the selected item. Returns -1 if cancel was pressed."
    )
public void AfterRadioSelection(int listIndex){
    EventDispatcher.dispatchEvent(this, "AfterRadioSelection", listIndex);
}
    
/** Toasts a message on the screen **/
@SimpleFunction(
    description = "Toasts a message on the screen."
    )
public void ShowToast(String message){
    Toast.makeText(activity,message,Toast.LENGTH_SHORT).show(); 
}

@SimpleFunction(
    description = "Creates a picker dialog with a list of options of which more than one can be chosen. Invokes the 'AfterMultiSelection' event."
    )
/** Source: http://stackoverflow.com/questions/16954196/alertdialog-with-checkbox-in-android **/
public void CheckboxListPicker(String title, YailList options, String buttonText, String cancelButtonText, boolean cancelable, String icon){

///////NMD Updates+++	
//Custom canceable text
//Custom html title|You can use html tags in the title
//Custom drawable/ user icon
icon = icon == null ? "" : icon;
Bitmap bitmapA;
bitmapA = null;
        try { // load the input bitmaps from Assets
            bitmapA = MediaUtil.getBitmapDrawable(container.$form(), icon).getBitmap();
        } catch (IOException ioe) {
            Log.e("Image", "Unable to load ");
            Toast.makeText(activity,"Error",Toast.LENGTH_SHORT).show();
        }
        
ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        bitmapA.compress(CompressFormat.PNG, 0 , ostream);
        
        File image = new File(Environment.getExternalStorageDirectory()+"/CheckDialogIcon.png");
        try {
            FileOutputStream fostream = new FileOutputStream(image);
            fostream.write(ostream.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return image.getAbsolutePath();
    
BitmapDrawable drawable = new BitmapDrawable(image.getAbsolutePath());
//NMD UPDATE <<<<
final String[] items = options.toStringArray();
// arraylist to keep the selected items
final ArrayList seletedItems = new ArrayList();

AlertDialog.Builder dialog = new AlertDialog.Builder(activity,currentTheme)
.setTitle(title)
.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
        if (isChecked) {
            // If the user checked the item, add it to the selected items
            seletedItems.add(indexSelected + 1);
        } else if (seletedItems.contains(indexSelected + 1)) {
            // Else, if the item is already in the array, remove it
            seletedItems.remove(Integer.valueOf(indexSelected + 1));
        }
    }
}).setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int id) {
        //  Your code when user clicked on OK
        //  You can write the code  to save the selected item here
        YailList itemsYail = YailList.makeList(seletedItems);
        AfterCheckboxSelection(itemsYail);
    }
});
    if (cancelable){
    dialog.setNegativeButton(cancelButtonText, new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int id) {
        //  Your code when user clicked on Cancel
        
        AfterCheckboxSelection(ElementsUtil.elementsFromString("-1"));
    }
});
    };
dialog.create();
dialog.setCancelable(cancelable);
dialog.setIcon(drawable);
dialog.show();
}

@SimpleEvent(
    description = "Invoked after user has finished selecting items from the Checkbox picker. Returns a list of indices of the selected items in the order of selection. Returns a list having -1 if cancel was pressed."
    )
public void AfterCheckboxSelection(YailList selection){
    EventDispatcher.dispatchEvent(this, "AfterCheckboxSelection", selection);
}

/** Displays a message dialog **/
@SimpleFunction(description = "Displays a simple message on the screen.")
public void MessageDialog(String icon,String title,String message,final String chooseButtonText){

	Dialog dialog = onCreateDialog(icon,title,message,chooseButtonText);
    dialog.show();

}

public Dialog onCreateDialog(String icon,String title,String message,String buttonText) {

///////NMD Updates+++	
//Custom canceable text
//Custom html title|You can use html tags in the title
//Custom drawable/ user icon
   
//Initialize the Alert Dialog

//NMD UPDATE >>>
//Source of the data in the DIalog
icon = icon == null ? "" : icon;
Bitmap bitmapA;
bitmapA = null;
        try { // load the input bitmaps from Assets
            bitmapA = MediaUtil.getBitmapDrawable(container.$form(), icon).getBitmap();
        } catch (IOException ioe) {
            Log.e("Image", "Unable to load ");
            Toast.makeText(activity,"Error",Toast.LENGTH_SHORT).show();
        }
        
ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        bitmapA.compress(CompressFormat.PNG, 0 , ostream);
        
        File image = new File(Environment.getExternalStorageDirectory()+"/DialogIcon.png");
        try {
            FileOutputStream fostream = new FileOutputStream(image);
            fostream.write(ostream.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return image.getAbsolutePath();
    
BitmapDrawable drawable = new BitmapDrawable(image.getAbsolutePath());
//NMD UPDATE <<<<
        
AlertDialog.Builder builder = new AlertDialog.Builder(activity, currentTheme);
builder.setCancelable(true);
builder.setTitle(Html.fromHtml(title));
builder.setMessage(Html.fromHtml(message));
builder.setIcon(drawable);
builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int id) {
// User clicked OK, so save the result somewhere
// or return them to the component that opened the dialog
}
});
// NMD UPDATE
//Set the negative button with no function.. Only to hide the message without a click on the positive button.
builder.setNegativeButton("", new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int id) {
// User clicked OK, so save the result somewhere
// or return them to the component that opened the dialog
}
});

return builder.create();
}
@DesignerProperty(
    editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN
    )  
@SimpleProperty(
    description = "Gets the current theme",
    userVisible = false
    )
public boolean LightTheme(boolean light){
if (light) {currentTheme = 16974393;}
else {currentTheme = 16974545;}

return light;
}
}


    

