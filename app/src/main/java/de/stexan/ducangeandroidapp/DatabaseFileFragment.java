package de.stexan.ducangeandroidapp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/** testing fragments
 *
 */
public class DatabaseFileFragment extends DialogFragment {


    public DatabaseFileFragment() {}

    public static DatabaseFileFragment newInstance() {
        return new DatabaseFileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_database_file, container, false);
    }


}
