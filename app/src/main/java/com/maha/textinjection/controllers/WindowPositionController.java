package com.maha.textinjection.controllers;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.maha.textinjection.R;


public class WindowPositionController {
   private WindowManager windowManager;
   private ImageView iconizedWindowView;
   private LinearLayout mainContainer;
   private View searchedItemView;

   private String searchString;
   private Context context;

   private String TAG = "WindowController";
   private String currntApplicationPackage;

   private boolean isKeyboardOpnend = false;


   private WindowTouchController windowTouchController;


   public WindowPositionController( WindowManager windowManager, Context context ) {
      this.windowManager = windowManager;
      this.context = context;
      onCreate();
   }

   public void onCreate() {
      //  fillDummyData();

      if( mainContainer == null ) {
         iconizedWindowView = new ImageView( context );
         mainContainer = new LinearLayout( context );
           /* LinearLayout.LayoutParams linearLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayoutParam.setLayoutDirection(Linear.HORIZONTAL);
            mainContainer.setLayoutParams(linearLayoutParam);*/
         iconizedWindowView.setImageResource( R.mipmap.ic_launcher );

         WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                 WindowManager.LayoutParams.WRAP_CONTENT,
                 WindowManager.LayoutParams.WRAP_CONTENT,
                 WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                 PixelFormat.TRANSLUCENT );

         params.gravity = Gravity.TOP | Gravity.LEFT;
         params.x = 0;
         params.y = 10;

         searchedItemView = LayoutInflater.from( context ).inflate( R.layout.content_floatingview, null );
         //  RecyclerView rvSounds = (RecyclerView) searchedItemView.findViewById(R.id.rvItems);
         //   SearchItemsListAdapter searchSoundListAdapter = new SearchItemsListAdapter(context, sounds, this);

         //   rvSounds.setAdapter(searchSoundListAdapter);
         //    rvSounds.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));


         View close = searchedItemView.findViewById( R.id.ivClose );
         close.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
               searchedItemView.setVisibility( searchedItemView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE );
            }
         } );
         searchedItemView.setVisibility( View.GONE );
         mainContainer.addView( searchedItemView );
         try {
            mainContainer.addView( iconizedWindowView );
            windowManager.addView( mainContainer, params );
         } catch( Exception e ) {
            e.printStackTrace();
         }

         initWindowTouchListener( params );

         mainContainer.setOnTouchListener( windowTouchController );


         mainContainer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
               Log.d( TAG, "setOnClickListener" );
               searchedItemView.setVisibility( searchedItemView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE );
            }
         } );


      }

   }

   private void initWindowTouchListener( WindowManager.LayoutParams params ) {
      windowTouchController = new WindowTouchController( params, mainContainer, windowManager );
   }


   public void onDestroy() {
      if( mainContainer != null )
         windowManager.removeView( mainContainer );
      mainContainer = null;
   }

   public void onPause() {
      if( mainContainer != null )
         windowManager.removeView( mainContainer );
      mainContainer = null;
   }


   public void onResume() {

   }

   public void notifyDatasetChanged( String searchString, String currntApplicationPackage ) {
      if( ( searchString.length() == 0 || searchString.equalsIgnoreCase( "[]" ) ) && mainContainer != null ) {
         if( mainContainer != null )
            windowManager.removeView( mainContainer );
         mainContainer = null;
      }
      if( mainContainer == null ) {
         onCreate();
      }
      this.searchString = searchString;
      this.currntApplicationPackage = currntApplicationPackage;
   }

   public void setIconized() {
      if( searchedItemView != null )
         searchedItemView.setVisibility( View.GONE );
   }


}
