package com.learn.weatherapplication.data;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class AssetsDatabaseHelper {
    private Context context;
    private String dbName = "db_city";

    public AssetsDatabaseHelper(Context context){
        this(context,"db_city");
    }
    public AssetsDatabaseHelper(Context context,String dbName){
        this.context = context;
        this.dbName = dbName;
    }
    public void checkDb(){
        File dbfile = context.getDatabasePath(dbName);
        if(!dbfile.exists()){
            try {
                copyDatabase(dbfile);
                Log.i("AssetsDatabaseHelper","database copied");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyDatabase(File dbfile) throws IOException {
        InputStream in = context.getAssets().open(dbName);

        Objects.requireNonNull(dbfile.getParentFile()).mkdirs();
        OutputStream out = new FileOutputStream(dbfile);
        int len = 0;
        byte[] buffer = new byte[1024];

        while((len = in.read(buffer)) > 0){
            out.write(buffer, 0, len);
        }

        out.flush();
        out.close();
        in.close();

    }
}
