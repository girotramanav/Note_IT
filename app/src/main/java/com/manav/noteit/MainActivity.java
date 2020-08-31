package com.manav.noteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    static ArrayAdapter<String> arrayAdapter;
    static ArrayList<String> notes;

    public void addNote()
    {
        Intent intent = new Intent( getApplicationContext(),notesContent.class );
        startActivity( intent );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.main_menu,menu );

        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected( item );
        switch(item.getItemId())
        {
            case R.id.addNote:
                addNote();return true;
            default:
                Toast.makeText( this, "Not Clicked", Toast.LENGTH_SHORT ).show();return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        listView = (ListView)findViewById( R.id.listView );
        notes = new ArrayList<String>(  );
        SharedPreferences sharedPreferences = this.getSharedPreferences( "com.manav.noteit", Context.MODE_PRIVATE );
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet( "notes",null );
        if(set == null)
        { notes.add( "Example Note" ); }
        else
        {
            notes = new ArrayList( set );
        }
        arrayAdapter = new ArrayAdapter( this,android.R.layout.simple_list_item_1,notes);
        listView.setAdapter( arrayAdapter );

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent( getApplicationContext(),notesContent.class );
                intent.putExtra( "noteId",position);
                startActivity( intent );
            }
        } );
        listView.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon( android.R.drawable.ic_delete )
                        .setTitle( "Delete Note" )
                        .setMessage( "This NOTE will be deleted permanently" )
                        .setPositiveButton( "Cancel", null )
                        .setNegativeButton( "Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove( position );
                                arrayAdapter.notifyDataSetChanged();
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences( "com.manav.noteit", Context.MODE_PRIVATE );

                                HashSet<String> set = new HashSet( MainActivity.notes );

                                sharedPreferences.edit().putStringSet( "notes",set ).apply();
                            }
                        } )
                        .show();
                return true;
            }
        } );

    }


}
