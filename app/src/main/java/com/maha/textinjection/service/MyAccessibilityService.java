package com.maha.textinjection.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;

import com.maha.textinjection.controllers.WindowPositionController;


public class MyAccessibilityService extends AccessibilityService {
   private final AccessibilityServiceInfo info = new AccessibilityServiceInfo();
   private static final String TAG = "MyAccessibilityService";
   private static final String TAGEVENTS = "TAGEVENTS";
   private String currntApplicationPackage = "";

   private WindowPositionController windowController;
   private WindowManager windowManager;
   private boolean showWindow = false;

   @Override
   public void onAccessibilityEvent( AccessibilityEvent accessibilityEvent ) {
      Log.d( TAG, "onAccessibilityEvent" );
      final String sourcePackageName = ( String ) accessibilityEvent.getPackageName();
      currntApplicationPackage = sourcePackageName;
      Log.d( TAG, "sourcePackageName:" + sourcePackageName );
      Log.d( TAG, "parcelable:" + accessibilityEvent.getText().toString() );


      if( accessibilityEvent.getSource() == null ) return;

      if( accessibilityEvent.getText().toString().equals( "[android]" ) ) {
         Log.e( "test", "Success" );

         if( accessibilityEvent.getSource().getClassName().equals( EditText.class.getName() ) ) {

            Bundle arguments = new Bundle();
            arguments.putCharSequence( AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                    "hacked" );
            accessibilityEvent.getSource().performAction( AccessibilityNodeInfo.ACTION_SET_TEXT, arguments );
         }
      }


      //accessibilityEvent.

      windowManager = ( WindowManager ) getSystemService( WINDOW_SERVICE );

      /*  if (accessibilityEvent.getEventType() == AccessibilityEvent.CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION) {
            Log.d(TAGEVENTS, "CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Log.d(TAGEVENTS, "TYPE_WINDOW_STATE_CHANGED");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.CONTENT_CHANGE_TYPE_SUBTREE) {
            Log.d(TAGEVENTS, "CONTENT_CHANGE_TYPE_SUBTREE");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.CONTENT_CHANGE_TYPE_TEXT) {
            Log.d(TAGEVENTS, "CONTENT_CHANGE_TYPE_TEXT");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.INVALID_POSITION) {
            Log.d(TAGEVENTS, "INVALID_POSITION");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED) {
            Log.d(TAGEVENTS, "CONTENT_CHANGE_TYPE_UNDEFINED");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_ANNOUNCEMENT) {
            Log.d(TAGEVENTS, "TYPE_ANNOUNCEMENT");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_ASSIST_READING_CONTEXT) {
            Log.d(TAGEVENTS, "TYPE_ASSIST_READING_CONTEXT");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_GESTURE_DETECTION_END) {
            Log.d(TAGEVENTS, "TYPE_GESTURE_DETECTION_END");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            Log.d(TAGEVENTS, "TYPE_VIEW_CLICKED");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START) {
            Log.d(TAGEVENTS, "TYPE_TOUCH_EXPLORATION_GESTURE_START");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_GESTURE_DETECTION_START) {
            Log.d(TAGEVENTS, "TYPE_GESTURE_DETECTION_START");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED) {
            Log.d(TAGEVENTS, "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
            Log.d(TAGEVENTS, "TYPE_VIEW_ACCESSIBILITY_FOCUSED");
        }
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOWS_CHANGED) {
            Log.d(TAGEVENTS, "TYPE_WINDOWS_CHANGED");
        }*/

      if( accessibilityEvent.getPackageName() == null || !( accessibilityEvent.getPackageName().equals( "com.bsb.hike" ) || !( accessibilityEvent.getPackageName().equals( "com.whatsapp" ) || accessibilityEvent.getPackageName().equals( "com.facebook.orca" ) || accessibilityEvent.getPackageName().equals( "com.twitter.android" ) || accessibilityEvent.getPackageName().equals( "com.facebook.katana" ) || accessibilityEvent.getPackageName().equals( "com.facebook.lite" ) ) ) )
         showWindow = false;

      if( accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED ) {
         Log.d( TAGEVENTS, "TYPE_VIEW_TEXT_CHANGED" );
         if( windowController == null )
            windowController = new WindowPositionController( windowManager, getApplicationContext() );
         showWindow = true;
         windowController.notifyDatasetChanged( accessibilityEvent.getText().toString(), currntApplicationPackage );
      } else if( accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ) {
         Log.d( TAGEVENTS, "TYPE_WINDOW_STATE_CHANGED:" + accessibilityEvent.getContentDescription() );


         //remove window when keyboard closed or user moved from chatting to other things
         if( windowController != null && !showWindow )
            windowController.onDestroy();
      }
   }

   @Override
   public void onInterrupt() {

   }

   @Override
   public void onServiceConnected() {
      // Set the type of events that this service wants to listen to.
      //Others won't be passed to this service.
      info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
      info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
      info.notificationTimeout = 100;

      this.setServiceInfo( info );
   }

   /**
    * Check if Accessibility Service is enabled.
    *
    * @param mContext
    * @return <code>true</code> if Accessibility Service is ON, otherwise <code>false</code>
    */
   public static boolean isAccessibilitySettingsOn( Context mContext ) {
      int accessibilityEnabled = 0;
      //your package /   accesibility service path/class
      final String service = "com.maha.textinjection/com.maha.textinjection.Service.MyAccessibilityService";

      boolean accessibilityFound = false;
      try {
         accessibilityEnabled = Settings.Secure.getInt(
                 mContext.getApplicationContext().getContentResolver(),
                 android.provider.Settings.Secure.ACCESSIBILITY_ENABLED );
         Log.v( TAG, "accessibilityEnabled = " + accessibilityEnabled );
      } catch( Settings.SettingNotFoundException e ) {
         Log.e( TAG, "Error finding setting, default accessibility to not found: "
                 + e.getMessage() );
      }
      TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter( ':' );

      if( accessibilityEnabled == 1 ) {
         Log.v( TAG, "***ACCESSIBILIY IS ENABLED*** -----------------" );
         String settingValue = Settings.Secure.getString(
                 mContext.getApplicationContext().getContentResolver(),
                 Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES );
         if( settingValue != null ) {
            TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
            splitter.setString( settingValue );
            while( splitter.hasNext() ) {
               String accessabilityService = splitter.next();

               Log.v( TAG, "-------------- > accessabilityService :: " + accessabilityService );
               if( accessabilityService.equalsIgnoreCase( service ) ) {
                  Log.v( TAG, "We've found the correct setting - accessibility is switched on!" );
                  return true;
               }
            }
         }
      } else {
         Log.v( TAG, "***ACCESSIBILIY IS DISABLED***" );
      }

      return accessibilityFound;
   }
}