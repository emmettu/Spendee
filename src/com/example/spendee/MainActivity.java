package com.example.spendee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	PurchaseHistory purchaseHistory = new PurchaseHistory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView dollarTextView = (TextView) findViewById(R.id.dollarTextView);
        final EditText amountText = (EditText) findViewById(R.id.amountEditText);
        Button undoButton = (Button) findViewById(R.id.undobutton);
        
        //initialize purchase history from file
        
        final IOManager io = new IOManager(getApplicationContext());
        purchaseHistory = io.load();
        
        //set initial dollar value
		dollarTextView.setText(String.format("$%.2f" ,purchaseHistory.getCurrentSum().getAmount()));
        
        amountText.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				try {
					String amount = amountText.getText().toString();
					float floatAmount = Float.parseFloat(amount);
					
					purchaseHistory.add(floatAmount);
					
					dollarTextView.setText(String.format("$%.2f" ,purchaseHistory.getCurrentSum().getAmount()));
					amountText.setText("");
					io.save(purchaseHistory);	
				}
				catch (NumberFormatException e) {
					Toast.makeText(getBaseContext(), "Invalid Entry", Toast.LENGTH_SHORT).show();
					amountText.setText("");
				}
				return false;
			}
		});
        
        undoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				float amount = purchaseHistory.undo();
				if(amount == 0) {
					Toast.makeText(getBaseContext(), "Nothing to undo", Toast.LENGTH_SHORT).show();
				}
				else {
					dollarTextView.setText(String.format("$%.2f" ,purchaseHistory.getCurrentSum().getAmount()));
					Toast.makeText(getBaseContext(), String.format("Undid $%.2f", amount*-1), Toast.LENGTH_SHORT).show();
					io.save(purchaseHistory);
				}
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.History) {
    		
    		Intent intent = new Intent(MainActivity.this, History.class);
    		startActivity(intent);
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

}
