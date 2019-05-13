package com.example.android.scientific_calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends Activity {

    //constants
    private final static double pie = 3.1415926, e = 2.718281;

    TextView resultBox;
    Button clearButton, AC ,point;

    double prevNum;
    int n =1;
    String prevOp,op;
    boolean opClicked,pointClicked;

    int[] numberButtonIds = { R.id.b0, R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5, R.id.b6, R.id.b7, R.id.b8, R.id.b9 };

    int[] postOpButtonIds = { R.id.b_add, R.id.b_subtract, R.id.b_div, R.id.b_mul, R.id.b_equal ,R.id.sin,R.id.AC,R.id.cos,
            R.id.tan,R.id.tanh,R.id.sinh,R.id.cosh,R.id.power_e,R.id.log,R.id.ln,R.id.power
            ,R.id.sin_inverse,R.id.root_,R.id.root,R.id.inverse,R.id.power_10,R.id.cos_inverse,R.id.tan_inverse};

    int[] preOpButtonId ={R.id.sign,R.id.b_percent,R.id.fact,R.id.square,R.id.cube};

    Button numberButtons[] = new Button[10];
    Button postOpButtons[] = new Button[postOpButtonIds.length];
    Button preOpButton[] = new Button[preOpButtonId.length];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initial conditions
        prevNum = 0;
        prevOp = "=";
        op = null;
        opClicked = false;

        clearButton = findViewById(R.id.clear);
        clearButton.setOnClickListener(clearListener);
        resultBox = findViewById(R.id.result);
        AC = findViewById(R.id.AC);
        point = findViewById(R.id.point);

        //Initializing the number buttons
        for(int i = 0; i < 10; i++){
            numberButtons[i] = findViewById(numberButtonIds[i]);
            numberButtons[i].setOnClickListener(numberListener);
        }

        //Initializing the buttons which operates only after entering next number.
        for(int i = 0; i < postOpButtonIds.length; i++){
            postOpButtons[i] = findViewById(postOpButtonIds[i]);
            postOpButtons[i].setOnClickListener(postOpListener);
        }

        //Initializing the buttons which operates immediately.
        for(int i = 0; i< preOpButtonId.length; i++){
            preOpButton[i] = findViewById(preOpButtonId[i]);
            preOpButton[i].setOnClickListener(preOplistener);
        }


        //Function when AC button is clicked.
        AC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevNum = 0;
                prevOp = "=";
                resultBox.setText("0");
            }
        });

        //Function when point(.) is clicked.
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pointClicked = true;
            }
        });
    }

    //When number is clicked.
    View.OnClickListener numberListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String buttonText = ((Button) v).getText().toString();

            double numberClicked = Double.parseDouble(buttonText);

            if (!opClicked) {
                double numberInBox = Double.parseDouble(resultBox.getText().toString());
                if(!pointClicked)
                    numberInBox = numberInBox * 10 + numberClicked;
                else{
                    numberInBox = numberInBox+numberClicked*Math.pow(10,-n);
                    n++;
                }
                Double bd = numberInBox;
                BigDecimal bd1 = new BigDecimal(bd);
                bd1 = bd1.setScale(6, RoundingMode.HALF_UP).stripTrailingZeros();
                resultBox.setText(Double.toString(numberInBox));
            }
            else{
                Double bd = numberClicked;
                BigDecimal bd1 = new BigDecimal(bd);
                bd1 = bd1.setScale(6, RoundingMode.HALF_UP).stripTrailingZeros();
                resultBox.setText(Double.toString(numberClicked));
            }
            opClicked = false;
        }
    };

    //When a button (which the operation happens immediately) clicks.
    View.OnClickListener preOplistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            double numberInBox = Double.parseDouble(resultBox.getText().toString());
            String message = null ;

            op = ((Button)v).getText().toString();
            opClicked=true;

            pointClicked= false;
            n =1;

            switch (op){
                case "+/-" :
                    prevNum = -numberInBox;
                    message ="";
                    break;
                case "%":
                    prevNum = numberInBox/100;
                    message ="";
                    break;
                case "x^2":
                    prevNum = Math.pow(numberInBox,2);
                    break;
                case "x^3":
                    prevNum = Math.pow(numberInBox,3);
                    break;
                case "x!":
                    {
                    if(numberInBox>=0 && (int)numberInBox == numberInBox)
                    {
                        prevNum = fact((int)numberInBox);
                        break;
                    }
                    else{
                         message = "error";
                    }
                    break;
                    }


            }

            if(message !="error"){
                //Formatting decimal to number having
                String s = String.format("%.6f",prevNum);
                Double bd = Double.parseDouble(s);
                BigDecimal bd1 = new BigDecimal(bd);
                if(message == "")
                    bd1 = bd1.setScale(8, RoundingMode.HALF_UP).stripTrailingZeros();
                else
                    bd1 = bd1.setScale(0, RoundingMode.HALF_UP).stripTrailingZeros();
                resultBox.setText(bd1.toString());
            }
            else{
                resultBox.setText("ERROR");
                message = null;
                prevNum = 0;
                opClicked = true;
            }


        }
    };

    //Function when button (which operates only after entering next number) is clicked.
    View.OnClickListener postOpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            double numberInBox = Double.parseDouble(resultBox.getText().toString());

            switch (prevOp){
                case "+":
                    prevNum += numberInBox;
                    break;
                case "-":
                    prevNum -= numberInBox;
                    break;
                case "X":
                    prevNum *= numberInBox;
                    break;
                case "/":
                    prevNum /= numberInBox;
                    break;
                case "=":
                    prevNum = numberInBox;
                    break;
                case "sin":
                    prevNum = Math.sin(Math.toRadians(numberInBox));
                    break;
                case "cos":
                    prevNum = Math.cos(Math.toRadians(numberInBox));
                    break;
                case "tan":
                    prevNum = Math.tan(Math.toRadians(numberInBox));
                    break;
                case "sinh":
                    prevNum = Math.sinh((numberInBox));
                    break;
                case "cosh":
                    prevNum = Math.cosh((numberInBox));
                    break;
                case "tanh":
                    prevNum = Math.tanh((numberInBox));
                    break;
                case "+/-":
                    prevNum = -numberInBox;
                    break;
                case "e^x":
                    prevNum = Math.exp(numberInBox);
                    break;
                case "log":
                    prevNum = Math.log10(numberInBox);
                    break;
                case "ln":
                    prevNum = Math.log(numberInBox);
                    break;
                case "x^y":
                    prevNum = Math.pow(prevNum,numberInBox);
                    break;
                case "sin-1" :
                    prevNum = Math.asin(numberInBox);
                    prevNum=Math.toDegrees(prevNum);
                    break;

                case "cos-1":
                    prevNum = Math.acos(numberInBox);
                    prevNum=Math.toDegrees(prevNum);
                    break;
                case "tan-1":
                    prevNum = Math.atan(numberInBox);
                    prevNum=Math.toDegrees(prevNum);
                    break;

                case "1/x":
                    prevNum = 1/numberInBox;
                    break;
                case "√":
                    prevNum = Math.sqrt(numberInBox);
                    break;
                case "x√y":
                    prevNum = Math.pow(numberInBox,1/prevNum);
                    break;
                case "10^x":
                    prevNum = Math.pow(10,numberInBox);
                    break;

            }

            opClicked = true;
            pointClicked = false;
            n=1;

            //Formatting decimal to number having 6 decimal place.
            Double bd = prevNum;
            BigDecimal bd1 = new BigDecimal(bd);
            bd1 = bd1.setScale(6, RoundingMode.HALF_UP).stripTrailingZeros();
            resultBox.setText(bd1.toString());
            prevOp = ((Button) v).getText().toString();
        }
    };

    //Function for clear button
    View.OnClickListener clearListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String numberInBox = resultBox.getText().toString();
            if(numberInBox != "")
                numberInBox = numberInBox.substring(0,numberInBox.length()-1);
            if(numberInBox.length()==0)
                numberInBox = "0";
            resultBox.setText(numberInBox);
        }
    };

    //Function for finding Factorial
    private int fact(int num){
        int fact =1;
        for(int i=num;i>1;i--){
            fact *= i;
        }
        return fact;
    }
}