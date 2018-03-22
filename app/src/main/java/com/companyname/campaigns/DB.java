package com.companyname.campaigns;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    final static String name="androidProje";
    final static int version=1;


    public DB(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE `liste` (\n" +
                "\t`favorid`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`fuserid`\tINTEGER,\n" +
                "\t`fproductid`\tINTEGER,\n" +
                "\t`fProductTitle`\tTEXT,\n" +
                "\t`fProductMoney`\tTEXT\n" +
                ");");

      //  sqLiteDatabase.execSQL("insert into liste values(null,null,null)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table if exists liste");
        onCreate(sqLiteDatabase);

    }
}
