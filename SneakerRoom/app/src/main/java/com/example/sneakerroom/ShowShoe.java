package com.example.sneakerroom;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowShoe extends AppCompatActivity {
    private TextView name;
    private TextView price;
    private TextView colorway;
    private TextView condition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_shoe_layout);

        name = (TextView)findViewById(R.id.sneaker_name);
        price = (TextView)findViewById(R.id.sneaker_price);
        colorway = (TextView)findViewById(R.id.sneaker_colorway);
        condition = (TextView)findViewById(R.id.sneaker_condition);

        Shoes shoe = (Shoes)getIntent().getSerializableExtra("shoe");
        name.setText(shoe.getSneakerName());
        price.setText(shoe.getPrice() + "");
        colorway.setText(shoe.getColorway());
        condition.setText(shoe.getCondition());

    }
}
