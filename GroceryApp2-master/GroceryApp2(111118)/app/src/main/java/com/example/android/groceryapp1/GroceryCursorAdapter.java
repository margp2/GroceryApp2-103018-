package com.example.android.groceryapp1;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.groceryapp1.data.GroceryContract;

class GroceryCursorAdapter extends CursorAdapter {
    int quantity = 0;

    public GroceryCursorAdapter(Context context, Cursor cursor) {
        super ( context, cursor, 0/* flags*/ );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from ( context ).inflate ( R.layout.list_item, parent, false );
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView nameTextView = view.findViewById ( R.id.grocery_name );
        TextView priceTextView = view.findViewById ( R.id.groceryPrice );
        final TextView qtyTextView = view.findViewById ( R.id.grocery_qty );
        TextView supplierNameTextView = view.findViewById ( R.id.grocSuppName );
        TextView supplierPhoneTextView = view.findViewById ( R.id.grocSuppPhone );

        final int nameColumnIndex = cursor.getColumnIndex ( GroceryContract.GroceryEntry.COLUMN_GROCERY_NAME );
        int priceColumnIndex = cursor.getColumnIndex ( GroceryContract.GroceryEntry.COLUMN_GROCERY_PRICE );
        final int qtyColumnIndex = cursor.getColumnIndex ( GroceryContract.GroceryEntry.COLUMN_GROCERY_QUANTITY );
        int suppNameColumnIndex = cursor.getColumnIndex ( GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLIER_NAME );
        int suppPhoneColumnIndex = cursor.getColumnIndex ( GroceryContract.GroceryEntry.COLUMN_GROCERY_SUPPLER_PHONE );

        String groceryName = cursor.getString ( nameColumnIndex );
        final String groceryPrice = cursor.getString ( priceColumnIndex );
        final String groceryQty = cursor.getString ( qtyColumnIndex );
        String grocerySuppName = cursor.getString ( suppNameColumnIndex );
        String grocerySuppPhone = cursor.getString ( suppPhoneColumnIndex );

        nameTextView.setText ( groceryName );
        priceTextView.setText ( groceryPrice );
        qtyTextView.setText ( String.valueOf ( groceryQty ) );
        supplierNameTextView.setText ( grocerySuppName );
        supplierPhoneTextView.setText ( grocerySuppPhone );

        final int cursorPosition = cursor.getPosition ();

        final Button saleBtn = view.findViewById ( R.id.saleBtn );
        saleBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                ContentValues groceryValues = new ContentValues ();
                cursor.moveToPosition ( cursorPosition );
                int previousQty = 0;
                int groceryId = cursor.getInt ( cursor.getColumnIndex ( GroceryContract.GroceryEntry._ID ) );
                previousQty = Integer.parseInt ( String.valueOf ( quantity ) );
                previousQty -= 1;
                if (previousQty < 0) {
                    Toast.makeText ( context, "The quantity has decreased", Toast.LENGTH_SHORT ).show ();
                }
                groceryValues.put ( GroceryContract.GroceryEntry.COLUMN_GROCERY_QUANTITY, previousQty );
                Uri grocerySelectedUri = ContentUris.withAppendedId ( GroceryContract.GroceryEntry.CONTENT_URI, groceryId );
                int rowsAffected = context.getContentResolver ().update (grocerySelectedUri, groceryValues, null, null );
                if (rowsAffected > 0){
                    qtyTextView.setText ( Integer.toString ( previousQty ) );
                }
            }
        } );

    }
}
