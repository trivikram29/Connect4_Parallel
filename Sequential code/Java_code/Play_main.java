import java.io.*;
import java.util.*;

class Play_main {
	static int rows = 7, columns = 7, mdepth = 7;
	int posrow = -1, poscol = -1;
	Utility util = new Utility();
	public static void main(String[] args) {
		Play_main mainObj = new Play_main();
		Scanner sc = new Scanner(System.in);
		int mat[][] = new int[rows][columns];
		int arr[] = new int[columns];
		int flag=1;
		mainObj.util.initialize(mat, arr);
		mainObj.util.printMatrix(mat);
		Boolean player_turn = (Math.random() > 0.5) ? true : false;
		if(player_turn){
			System.out.println("It's your turn");
		} else{
			System.out.println("It's my turn");	
		}
		
		while(true){
			if(player_turn)
			{
				System.out.println("Enter a valid column number between 0-6");
				int col = sc.nextInt();
				//System.out.println("row value:"+arr[col]);
				if((col>=columns || col<0) || (arr[col]==(rows)-1))				
					continue;
				arr[col]++;
				//System.out.println("row value:"+arr[col]);
				mat[arr[col]][col] = 1;
				mainObj.util.printMatrix(mat);
				if(mainObj.util.hasWin(mat, arr[col], col, 1))
				{
					System.out.println("You won the game");
					System.exit(0);
				}
			}
			else
			{				
				if(flag==1)
				{
					mat[++arr[(columns/2)]][(columns/2)] = 2;
					player_turn = !player_turn;
					mainObj.util.printMatrix(mat);
					flag=0;					
					continue;
				}
				long time = System.currentTimeMillis();
				mainObj.makeMove(mat, arr, mdepth, true, -1, -1);
				System.out.print("time " + (System.currentTimeMillis()-time));
				mat[mainObj.posrow][mainObj.poscol] = 2;
				arr[mainObj.poscol]++;
				
				mainObj.util.printMatrix(mat);
				if(mainObj.util.hasWin(mat, mainObj.posrow, mainObj.poscol, 2))
				{
					System.out.println("I won the Game! What say?");
					System.exit(0);
				}
				System.out.println("PLAYED AT " + mainObj.poscol);
			}
			player_turn = !player_turn;
		}
	}
	
	double makeMove(int mat[][], int pos[], int depth, Boolean isMaximize, int currRow, int currCol)
	{
		if(currRow >=0 && currRow < rows && currCol >= 0 && currCol < columns)
		{
			if(util.hasWin(mat, currRow, currCol, 2))
			{
				return Integer.MAX_VALUE/(mdepth - depth+1);
			}
			else if(util.hasWin(mat, currRow, currCol, 1))
			{
				return Integer.MIN_VALUE/(mdepth - depth+1);
			}
		}
		double score = 0;
		
		if(isMaximize)
		{
			score = Integer.MIN_VALUE;
		} 
		else
		{
			score = Integer.MAX_VALUE;
		}

		for(int i = 0; i < columns ; i++)
		{
			pos[i]++;
			if(pos[i] >= rows)
			{
				pos[i]--;
				continue;
			}
			if(isMaximize)
			{
				mat[pos[i]][i] = 2; 
			} else{
				mat[pos[i]][i] = 1;
			}
			if(depth == 1){
					
				double tempscore=util.getScore(mat, pos[i], i, 2);
			//	System.out.println("score for depth " + depth + " and col  " + i + " row " + pos[i] + " score " + tempscore);
					
					if(isMaximize)
					{
						if(score <= tempscore)
						{
							score = tempscore;
						}
						if(depth == mdepth)
						{
							if(score <= tempscore)
							{
								posrow = pos[i];
								poscol = i;
							}
							System.out.println("Score for location "+i+" = "+tempscore + " final : " + score);
						}
					} 
					else
					{
						if(tempscore <= score)
						{
							score = tempscore;
						}
					}
			}
			if(depth > 1)
			{
				
				/*int tempMat[][] = new int[rows][columns];
				int temparr[] = new int[columns];
				util.initialize(tempMat, temparr);*/
				
				
				double tempscore = makeMove(mat, pos, depth-1, !isMaximize, pos[i], i);
			//	System.out.println("score for depth " + depth + " and col  " + i + " row " + pos[i] + " score " + tempscore);
				if(isMaximize)
				{
					if(score <= tempscore)
					{
						score = tempscore;
					}
					if(depth == mdepth)
					{
						if(score <= tempscore)
						{
							posrow = pos[i];
							poscol = i;
						}
						System.out.println("Score for location "+i+" = "+tempscore + " final : " + score);
					}
				} 
				else
				{
					if(tempscore <= score)
					{
						score = tempscore;
					}
				}
				
			}
			mat[pos[i]][i] = 0;
			pos[i]--;
		}
		
		return score;
	}
	
	
}
