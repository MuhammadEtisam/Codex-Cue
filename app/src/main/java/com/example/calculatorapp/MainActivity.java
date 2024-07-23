package com.example.calculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        setNumericOnClickListener();
        setOperatorOnClickListener();
    }

    private void setNumericOnClickListener() {
        findViewById(R.id.button0).setOnClickListener(v -> editText.append("0"));
        findViewById(R.id.button1).setOnClickListener(v -> editText.append("1"));
        findViewById(R.id.button2).setOnClickListener(v -> editText.append("2"));
        findViewById(R.id.button3).setOnClickListener(v -> editText.append("3"));
        findViewById(R.id.button4).setOnClickListener(v -> editText.append("4"));
        findViewById(R.id.button5).setOnClickListener(v -> editText.append("5"));
        findViewById(R.id.button6).setOnClickListener(v -> editText.append("6"));
        findViewById(R.id.button7).setOnClickListener(v -> editText.append("7"));
        findViewById(R.id.button8).setOnClickListener(v -> editText.append("8"));
        findViewById(R.id.button9).setOnClickListener(v -> editText.append("9"));
    }

    private void setOperatorOnClickListener() {
        findViewById(R.id.buttonAdd).setOnClickListener(v -> editText.append("+"));
        findViewById(R.id.buttonSubtract).setOnClickListener(v -> editText.append("-"));
        findViewById(R.id.buttonMultiply).setOnClickListener(v -> editText.append("*"));
        findViewById(R.id.buttonDivide).setOnClickListener(v -> editText.append("/"));
        findViewById(R.id.buttonEquals).setOnClickListener(v -> calculateResult());
        findViewById(R.id.buttonClear).setOnClickListener(v -> editText.setText(""));
    }

    private void calculateResult() {
        String input = editText.getText().toString();
        try {
            double result = evaluateExpression(input);
            editText.setText(String.valueOf(result));
        } catch (Exception e) {
            editText.setText("Error");
        }
    }

    private double evaluateExpression(String expression) {
        String[] tokens = expression.split("(?<=[-+*/])|(?=[-+*/])"); // Split by operators
        double result = 0;
        double current = 0;
        String operator = "+";

        for (String token : tokens) {
            token = token.trim();
            if (token.isEmpty()) continue;

            if (isOperator(token)) {
                operator = token;
            } else {
                current = Double.parseDouble(token);
                switch (operator) {
                    case "+":
                        result += current;
                        break;
                    case "-":
                        result -= current;
                        break;
                    case "*":
                        result *= current;
                        break;
                    case "/":
                        result /= current;
                        break;
                }
            }
        }
        return result;
    }

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }
}
