
class Utility {
	int rows = Play_main.rows, columns = Play_main.columns;
	
	void initialize(int mat[][], int arr[]){
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < columns; j++)
			{
				mat[i][j] = 0;
			}
		}
		for(int j = 0; j < columns; j++)
		{
			arr[j] = -1;
		}
	}
	
	void printMatrix(int mat[][])
	{
		System.out.println("  column ids: ");
		System.out.print("  "+0 + " ");
		for(int i = 1; i < columns; i++)
		{
			System.out.print("  "+i + " ");
		}
		System.out.println();
		for(int i = 0; i < columns; i++)
		{
			System.out.print("-----");
		}
		System.out.println();
		
		for(int i = rows - 1; i >=0; i--)
		{
			System.out.print("| ");
			
			for(int j = 0; j < columns; j++)
			{
				System.out.print(mat[i][j] + " | ");
			}
			System.out.println();
		}
	}
	
	Boolean hasWin(int mat[][], int recentrowmove, int recentcolumnmove, int arg)
	{
		return checkHorizontal(mat, recentrowmove, recentcolumnmove, arg)
				|| checkVertical(mat, recentrowmove, recentcolumnmove, arg)
				|| checkDiagonalRi(mat, recentrowmove, recentcolumnmove, arg)
				|| checkDiagonalLi(mat, recentrowmove, recentcolumnmove, arg);
	}
	
	Boolean checkHorizontal(int mat[][], int row, int column, int arg)
	{
		int tempr = row, tempc = column;
		int count = 0;
		while(tempc >= 0)
		{
			if(mat[row][tempc] != arg)
			{
				break;
			}
			count++;
			tempc--;
		}
		tempc = column + 1;
		while(tempc < columns)
		{
			if(mat[row][tempc] != arg)
			{
				break;
			}
			count++;
			tempc++;
		}
		if(count >= 4)
		{
			return true;
		}
		return false;
	}
	
	Boolean checkVertical(int mat[][], int row, int column, int arg)
	{
		int tempr = row, tempc = column;
		int count = 0;
		while(tempr >= 0)
		{
			if(mat[tempr][column] != arg)
			{
				break;
			}
			count++;
			tempr--;
		}
		tempr = row + 1;
		while(tempr < rows)
		{
			if(mat[tempr][column] != arg)
			{
				break;
			}
			count++;
			tempr++;
		}
		if(count >= 4)
		{
			return true;
		}
		return false;
	}
	
	Boolean checkDiagonalRi(int mat[][], int row, int column, int arg)
	{
		int tempr = row, tempc = column;
		int count = 0;
		while(tempr >= 0 && tempc >=0)
		{
			if(mat[tempr][tempc] != arg)
			{
				break;
			}
			count++;
			tempr--;
			tempc--;
		}
		tempr = row + 1;
		tempc = column +1;
		while(tempr < rows && tempc < columns)
		{
			if(mat[tempr][tempc] != arg)
			{
				break;
			}
			count++;
			tempr++;
			tempc++;
		}
		if(count >= 4)
		{
			return true;
		}
		return false;
	}
	
	Boolean checkDiagonalLi(int mat[][], int row, int column, int arg)
	{
		int tempr = row, tempc = column;
		int count = 0;
		while(tempr < rows && tempc >=0)
		{
			if(mat[tempr][tempc] != arg)
			{
				break;
			}
			count++;
			tempr++;
			tempc--;
		}
		tempr = row - 1;
		tempc = column +1;
		while(tempr >= 0 && tempc < columns)
		{
			if(mat[tempr][tempc] != arg)
			{
				break;
			}
			count++;
			tempr--;
			tempc++;
		}
		if(count >= 4)
		{
			return true;
		}
		return false;
	}
	
	
	
	int getScore(int mat[][], int row, int column, int arg)
	{
		int moreMoves=0;
		int machinescore = 0,finalScore=0;
		int tempr = row, tempc = column;
		while(tempc >= 0)
		{

			if (mat[row][tempc] == 0) {
				moreMoves++;
				
			}
			else if (mat[row][tempc] == arg) {
				machinescore++;
			} else
				break;
			tempc--;
		}
		tempc = column + 1;
		while(tempc < columns)
		{
			if (mat[row][tempc] == 0) {
				moreMoves++;
				
			}
			else if (mat[row][tempc] == arg) {
				machinescore++;
			} else
				break;
			tempc++;
		}
		
		
		if(moreMoves!=0)
		{
			finalScore+=calculateScore(machinescore, moreMoves);
		}

		//System.out.println("Final score " + finalScore + " moremoves " + moreMoves + " machine score " + machinescore);
		///////////////////////////////////////////
		machinescore=moreMoves=0;		
		tempr = row;
		tempc = column;
		
		while(tempr >= 0)
		{		
		    if (mat[tempr][column] == arg) 
		    {
				machinescore++;
			} 
		    else
				break;
			tempr--;
		}
		moreMoves=rows-row-1;
		if(moreMoves!=0)
		{
			finalScore+=calculateScore(machinescore, moreMoves);
		}
		////////////////////////////////////
		
		machinescore=moreMoves=0;
		tempr = row;
		tempc = column;
		
		while(tempr >= 0 && tempc >=0)
		{
			if (mat[tempr][tempc] == 0)
			{
				moreMoves++;				
			}
			else if (mat[tempr][tempc] == arg) 
			{
				machinescore++;
			} 
			else
			{
				break;
			}
			tempr--;
			tempc--;
		}
		tempr = row + 1;
		tempc = column +1;
		while(tempr < rows && tempc < columns)
		{
			if (mat[tempr][tempc] == 0)
			{
				moreMoves++;				
			}
			else if (mat[tempr][tempc] == arg) 
			{
				machinescore++;
			} 
			else
			{
				break;
			}
			tempr++;
			tempc++;
		}
		if(moreMoves!=0)
		{
			finalScore+=calculateScore(machinescore, moreMoves);
		}
		
		/////////////////////////////////////////////////////////////
		
		
		tempr = row;
		tempc = column;
		machinescore=moreMoves=0;
		while(tempr < rows && tempc >=0)
		{
			if (mat[tempr][tempc] == 0)
			{
				moreMoves++;				
			}
			else if (mat[tempr][tempc] == arg) 
			{
				machinescore++;
			} 
			else
			{
				break;
			}
			
			tempr++;
			tempc--;
		}
		tempr = row - 1;
		tempc = column +1;
		while(tempr >= 0 && tempc < columns)
		{
			if (mat[tempr][tempc] == 0)
			{
				moreMoves++;				
			}
			else if (mat[tempr][tempc] == arg) 
			{
				machinescore++;
			} 
			else
			{
				break;
			}			
			tempr--;
			tempc++;
		}
		
		
		if(moreMoves!=0)
		{
			finalScore+=calculateScore(machinescore, moreMoves);
		}		
		return finalScore;
		
	}
	
	
	
	/*
	
    public int evaluateBoard(int[][] b){
        
        int aiScore=1;
        int score=0;
        int blanks = 0;
        int k=0, moreMoves=0;
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;++j){
                
                if(b[i][j]==0 || b[i][j]==2) continue; 
                
                if(j<=3){ 
                    for(k=1;k<4;++k){
                        if(b[i][j+k]==1)aiScore++;
                        else if(b[i][j+k]==2){aiScore=0;blanks = 0;break;}
                        else blanks++;
                    }
                     
                    moreMoves = 0; 
                    if(blanks>0) 
                        for(int c=1;c<4;++c){
                            int column = j+c;
                            for(int m=i; m<= 5;m++){
                             if(b[m][column]==0)moreMoves++;
                                else break;
                            } 
                        } 
                    
                    if(moreMoves!=0) score += calculateScore(aiScore, moreMoves);
                    aiScore=1;   
                    blanks = 0;
                } 
                
                if(i>=3){
                    for(k=1;k<4;++k){
                        if(b[i-k][j]==1)aiScore++;
                        else if(b[i-k][j]==2){aiScore=0;break;} 
                    } 
                    moreMoves = 0; 
                    
                    if(aiScore>0){
                        int column = j;
                        for(int m=i-k+1; m<=i-1;m++){
                         if(b[m][column]==0)moreMoves++;
                            else break;
                        }  
                    }
                    if(moreMoves!=0) score += calculateScore(aiScore, moreMoves);
                    aiScore=1;  
                    blanks = 0;
                }
                 
                if(j>=3){
                    for(k=1;k<4;++k){
                        if(b[i][j-k]==1)aiScore++;
                        else if(b[i][j-k]==2){aiScore=0; blanks=0;break;}
                        else blanks++;
                    }
                    moreMoves=0;
                    if(blanks>0) 
                        for(int c=1;c<4;++c){
                            int column = j- c;
                            for(int m=i; m<= 5;m++){
                             if(b[m][column]==0)moreMoves++;
                                else break;
                            } 
                        } 
                    
                    if(moreMoves!=0) score += calculateScore(aiScore, moreMoves);
                    aiScore=1; 
                    blanks = 0;
                }
                 
                if(j<=3 && i>=3){
                    for(k=1;k<4;++k){
                        if(b[i-k][j+k]==1)aiScore++;
                        else if(b[i-k][j+k]==2){aiScore=0;blanks=0;break;}
                        else blanks++;                        
                    }
                    moreMoves=0;
                    if(blanks>0){
                        for(int c=1;c<4;++c){
                            int column = j+c, row = i-c;
                            for(int m=row;m<=5;++m){
                                if(b[m][column]==0)moreMoves++;
                                else if(b[m][column]==1);
                                else break;
                            }
                        } 
                        if(moreMoves!=0) score += calculateScore(aiScore, moreMoves);
                        aiScore=1;
                        blanks = 0;
                    }
                }
                 
                if(i>=3 && j>=3){
                    for(k=1;k<4;++k){
                        if(b[i-k][j-k]==1)aiScore++;
                        else if(b[i-k][j-k]==2){aiScore=0;blanks=0;break;}
                        else blanks++;                        
                    }
                    moreMoves=0;
                    if(blanks>0){
                        for(int c=1;c<4;++c){
                            int column = j-c, row = i-c;
                            for(int m=row;m<=5;++m){
                                if(b[m][column]==0)moreMoves++;
                                else if(b[m][column]==1);
                                else break;
                            }
                        } 
                        if(moreMoves!=0) score += calculateScore(aiScore, moreMoves);
                        aiScore=1;
                        blanks = 0;
                    }
                } 
            }
        }
        return score;
    } 
	
	*/
	
	
	
	
    int calculateScore(int machineScore, int moreMoves)
    {       	
        int moveScore = (moreMoves>3)? 3:moreMoves;
        if(machineScore==0)return 0;
        else if(machineScore==1)return (moveScore < 3 )? 0 :1*moveScore;
        else if(machineScore==2)return (moveScore < 2 )? 0 :10*moveScore;
        else if(machineScore==3)return (moveScore < 1 )? 0 :100*moveScore;
        else return 1000;
    }
	
	
	
	
	
}
