package com.example.android.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {
    static final String quantityreset = "A";
    static final String counterreset = "B";
    int quantity = 0;
    int flag = 0;
    int counter = 0;
    TextView quantityTextView;
    TextView orderSummaryTextView;
    TextView counterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            quantity = savedInstanceState.getInt(quantityreset);
            counter = savedInstanceState.getInt(counterreset);
        }
        setContentView(R.layout.activity_main);
        display(quantity);
        displayPrice(0);
        displayMessage(NumberFormat.getCurrencyInstance().format(0), counter);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(quantityreset, quantity);
        savedInstanceState.putInt(counterreset, counter);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * This method is called upon clicking the order button.
     */
    public void sendOrder(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(getResources().getString(R.string.address))); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.subject) + counter);
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.sendOrder, NameText(), calculatePrice(), quantity, checkStateWhipped(), checkStateChocolate()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method is for whipped cream status
     */
    private boolean checkStateWhipped() {
        boolean hasWhippedCream = ((CheckBox) findViewById(R.id.checkWhipped)).isChecked();
        return hasWhippedCream;
    }

    /**
     * This method is for chocolate status
     */
    private boolean checkStateChocolate() {
        boolean hasChocolate = ((CheckBox) findViewById(R.id.checkChocolate)).isChecked();
        return hasChocolate;
    }

    /**
     * This method checks the name.
     */
    private String NameText() {
        EditText Name = findViewById(R.id.plain_text_input);
        return Name.getText().toString();
    }

    /**
     * This method is called upon clicking the button
     */
    public void increment(View view) {
//        if (flag == 1) {
//            flag = 0;
//            Resetorder(view);
//        }
        if (quantity > 99) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.moreThan100), Toast.LENGTH_SHORT).show();
        } else
            quantity = quantity + 1;
        display(quantity);
    }

    /**
     * This method is called when user clicks the button
     */
    public void decrement(View view) {
//        if (flag == 1) {
//            flag = 0;
//            Resetorder(view);
//        }
        if (quantity == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.decrementlessThan0), Toast.LENGTH_SHORT).show();
            displayMessage(NumberFormat.getCurrencyInstance().format(0), counter);
        } else
            quantity = quantity - 1;

        display(quantity);
    }

    /**
     * This method is called upon new order
     */
    public void Resetorder(View view) {
        quantity = 0;
        CheckBox hasWhippedCream = findViewById(R.id.checkWhipped);
        hasWhippedCream.setChecked(false);
        CheckBox hasChocolate = findViewById(R.id.checkChocolate);
        hasChocolate.setChecked(false);
        display(quantity);
        displayPrice(0);
        EditText Name = findViewById(R.id.plain_text_input);
        Name.setText("");
    }

    /**
     * This method calculates price
     */
    private String calculatePrice() {
        int basePrice = 5;
        if (checkStateWhipped()) {
            basePrice += 1;
        }
        if (checkStateChocolate()) {
            basePrice += 2;
        }
        int price = quantity * basePrice;
        String cost = NumberFormat.getCurrencyInstance().format(price);
        return cost;
    }

    /**
     * This method summarizes the order
     */
    private String orderSummary() {
        return getResources().getString(R.string.priceMessage, NameText(), calculatePrice(), quantity, checkStateWhipped(), checkStateChocolate());
    }

    /**
     * This is the method for displaying the quantity
     */
    private void display(int number) {
        quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * And this one for the price
     */
    private void displayPrice(int number) {
        orderSummaryTextView = findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    /**
     * Method for the text
     */
    private void displayMessage(String message, int count) {
        orderSummaryTextView = findViewById(R.id.order_summary_text_view);
        counterTextView.setText("" + count);
        orderSummaryTextView.setText(message);
    }

    /**
     * Method for bill button click event
     */
    public void submitOrder(View view) {
        if (quantity == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.lessThan0), Toast.LENGTH_SHORT).show();
            Resetorder(view);
        } else {
            counter += 1;
            flag = 1;
            displayMessage(orderSummary(), counter);
        }
    }


}
