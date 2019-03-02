package com.maha.textinjection;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.maha.textinjection.service.MyAccessibilityService;

public class MainActivity extends AppCompatActivity {

   @Override
   protected void onCreate( Bundle savedInstanceState ) {
      super.onCreate( savedInstanceState );
      setContentView( R.layout.activity_main );

      boolean abool=MyAccessibilityService.isAccessibilitySettingsOn(this);

      if(abool){
         Log.e( "Enable" ,"success");
      }else{

         showAlertDialog(this,"Enable GOTO setting -> Accessibility->TextInjection");
      }
   }


   /**
    * @param aMessage aMessage
    */
   public  void showAlertDialog( final AppCompatActivity aContext, String aMessage ) {
      try {
         AlertDialog.Builder builder = new AlertDialog.Builder( aContext );
         builder.setMessage( aMessage )
                 .setTitle( aContext.getString( R.string.app_name ) )
                 .setCancelable( false )
                 .setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int id ) {
                       dialog.dismiss();
                    }
                 } );

         AlertDialog alert = builder.create();
         alert.show();
         // Change the buttons color in dialog
         Button pbutton = alert.getButton( DialogInterface.BUTTON_POSITIVE );
         pbutton.setTextColor( ContextCompat.getColor( aContext, R.color.colorAccent ) );
      } catch( Exception e ) {
         e.printStackTrace();
      }
   }
}
