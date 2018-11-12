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

        int idColumnIndex = cursor.getColumnIndex ( GroceryContract.GroceryEntry._ID );
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

        final  int groceryId = cursor.getInt ( idColumnIndex );
        final Button saleBtn = view.findViewById ( R.id.saleBtn );
        saleBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt ( qtyTextView.getText ().toString () );
                if (quantity > 0) {
                    --quantity;
                    String qtyString = Integer.toString ( quantity );
                    ContentValues values = new ContentValues ();
                    values.put ( GroceryContract.GroceryEntry.COLUMN_GROCERY_QUANTITY, qtyString );
                    Uri currentGroceryUri = ContentUris.withAppendedId ( GroceryContract.GroceryEntry.CONTENT_URI, groceryId );
                    int rowsAffected = context.getContentResolver ().update ( currentGroceryUri, values, null, null );
                    if (rowsAffected != 0) {
                        qtyTextView.setText ( qtyString );
                    } else {
                        Toast.makeText ( context, context.getString( R.string.saleToastMsg), Toast.LENGTH_SHORT ).show ();
                    }
                } if (quantity == 0){
                    Toast.makeText ( context, "The quantity can not be lower than 0.", Toast.LENGTH_SHORT ).show ();
                }
            }
        } );

    }
}
