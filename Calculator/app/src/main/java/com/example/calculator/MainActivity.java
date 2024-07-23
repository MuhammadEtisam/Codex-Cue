package com.example.calculator;

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
        String expression = editText.getText().toString();
        // Simple calculation logic using eval() method or similar
        try {
            double result = eval(expression);
            editText.setText(String.valueOf(result));
        } catch (Exception e) {
            editText.setText("Error");
        }
    }

    // Simple eval function to handle basic arithmetic operations
    public double eval(String expression) {
        // Basic implementation of eval function
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                return x;
            }
        }.parse();
    }
}

