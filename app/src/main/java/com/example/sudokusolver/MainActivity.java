package com.example.sudokusolver;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button, clear_button;
    private int[][] matrix = new int[9][9]; // this array wil store the sudoku grid

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        clear_button = findViewById(R.id.clear_button);

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {            // setting onClickListener for solve button

                for(int row=1; row<=9; row++){

                    for (int col=1; col<=9; col++){
                        String a = Integer.valueOf(row*10+col).toString();
                        String id = "t"+a;
                        int editText_id = getResources().getIdentifier(id,"id",getPackageName());
                        EditText editText = findViewById(editText_id);
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        editText.setGravity(Gravity.CENTER_HORIZONTAL);

                        try {
                            matrix[row - 1][col - 1] = Integer.parseInt(editText.getText().toString());
                        }
                        catch (Exception e){     // if the box is empty, put 0
                            matrix[row-1][col-1] = 0;
                        }

                    }
                }

                boolean valid=true;
                outer:
                    for (int i=0;i<9;i++){          // this nested for loop checks if game has a valid solution
                        for (int j=0;j<9;j++){
                            if (matrix[i][j]!=0){
                                int[] pos = {i,j};
                                if (!valid(matrix,matrix[i][j],pos)){
                                    Toast.makeText(MainActivity.this, "No solution found!", Toast.LENGTH_SHORT).show();
                                    valid=false;
                                    break outer;
                                }
                            }
                        }
                    }


                if (valid && solve(matrix)){    // if there is a solution, solve the game

                    for(int row=1; row<=9; row++){  // this nested for loop sets the text in editText

                        for (int col=1; col<=9; col++){
                            String a = Integer.valueOf(row*10+col).toString();
                            String id = "t"+a;
                            int editText_id = getResources().getIdentifier(id,"id",getPackageName());
                            EditText editText = findViewById(editText_id);
                            editText.setText(Integer.valueOf(matrix[row-1][col-1]).toString());

                        }
                    }

                    Toast.makeText(MainActivity.this, "Solved!", Toast.LENGTH_SHORT).show(); // showing toast message
                }

            }
        });

        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {   // setting onClickListener for clear button
                for(int row=1; row<=9; row++){

                    for (int col=1; col<=9; col++){
                        String a = Integer.valueOf(row*10+col).toString();
                        String id = "t"+a;
                        int editText_id = getResources().getIdentifier(id,"id",getPackageName());
                        EditText editText = findViewById(editText_id);
                        editText.getText().clear();

                    }
                }
            }
        });

    }

    public boolean solve(int[][] board){   // this is the function where I applied recursive backtracking to solve sudoku
        int[] find = find_empty(board);

        if(find[0]==1000){ // this indicates no more empty spots are left
            return true;
        }

        int row = find[0], col = find[1];

        for(int i=1;i<10;i++){  // trying each number from 1 to 9

            if (valid(board, i, find)){  // if the number is valid at that position, place that number

                board[row][col]=i;

                if (solve(board)){ // if board is solved, return true, here we are applying recursion to solve the game
                    return true;
                }

                board[row][col]=0;
            }

        }

        return false;
    }

    public int[] find_empty(int[][] board){  // this functions helps finding next empty spot in Sudoku grid
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                if (board[i][j]==0){
                    int[] answer = new int[2];
                    answer[0]=i;
                    answer[1]=j;
                    return answer;
                }
            }
        }
        int[] answer = new int[1];
        answer[0]=1000; // this will indicate that no empty spot was found
        return answer;
    }

    public boolean valid(int[][] board, int num, int[] pos){  // this method checks if the given number is valid at given position in the grid
        int a = pos[0];
        int b = pos[1];

        for(int i=0;i<9;i++){  // checking row first
            if (i==b){
                continue;
            }
            else{
                if (board[a][i]==num){ // if same number found in the row, return false
                    return false;
                }
            }
        }

        for(int i=0;i<9;i++){  // checking whole column for duplicate number
            if (i==a){
                continue;
            }
            else{
                if (board[i][b]==num){
                    return false;
                }
            }
        }

        int x = pos[0]/3;
        int y = pos[1]/3;

        for (int i=3*x;i<3*x+3;i++){   // checking the corresponding smaller grid for duplicates
            for (int j=3*y;j<3*y+3;j++){
                if (pos[0]==i && pos[1]==j){
                    continue;
                }
                else{
                    if (board[i][j]==num){
                        return false;
                    }
                }
            }
        }

        return true;  // if our program reaches here this means the number is valid and returning true
    }

}