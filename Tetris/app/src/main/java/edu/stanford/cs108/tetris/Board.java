// Board.java
package edu.stanford.cs108.tetris;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	private int[] widths;
	private int[] xWidths;
	private int[] heights;
	private int[] xHeights;
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean[][] xGrid;
	private boolean DEBUG = true;
	boolean committed;
	private int maxHeight;
	
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		xGrid = new boolean[width][height];
		committed = true;
		
		// YOUR CODE HERE
		heights = new int[width];
		widths = new int[height];
		xHeights = new int[width];
		xWidths = new int[height];
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight; 
	}
	
	public int setMaxHeight() {	 
		int max = 0;
		for(int i=0; i<heights.length; i++) {
			if(max < heights[i]) max = heights[i];
		}
		return max; 
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			for(int i =0 ; i<grid.length; i++) {
				int num = 0;
				for(int j = 0; j<grid[0].length; j++) {
					if(grid[i][j] == true) {
						if(num < j+1) num = j+1; 
					}
				}
				if(num != heights[i]) throw new RuntimeException("incorrect heights");
			}
			for(int j = 0; j<grid[0].length; j++) {
				int num = 0;
				for(int i = 0; i<grid.length; i++) {
					if(grid[i][j] == true) num++;
				}
				if(num != widths[j]) throw new RuntimeException("wrong widths");
			}
			// check maxHeight
			int max = 0;
			for(int i = 0; i<heights.length; i++) {
				if(max < heights[i]) max = heights[i];
			}
			if(max != maxHeight) throw new RuntimeException("maxHeight wrong");
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int y = 0;
        for(int i =0 ;i < piece.getWidth();i++)  {
            int possibleY = getColumnHeight(x + i)- piece.getSkirt()[i];
            if (possibleY > 0 && y < possibleY)
                    y = possibleY;
        }
		return y;  
	}
		
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x]; 
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y]; 
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if(x>=grid.length || x < 0 || y >= grid[0].length || y < 0) return true;
		return grid[x][y];
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) {
			throw new RuntimeException("place commit problem");		
		}
		committed = false;
		// set backup
		for(int i = 0; i<grid.length; i++) {
		   System.arraycopy(grid[i], 0, xGrid[i], 0, grid[i].length);
		}   
		System.arraycopy(widths, 0, xWidths, 0, widths.length);
		System.arraycopy(heights, 0, xHeights, 0, heights.length);
		
		int result = PLACE_OK;		
		TPoint[] body = piece.getBody();
		int deltaX, deltaY = 0;
		for(int i =0 ; i<body.length; i++) {
			deltaX = body[i].x;
			deltaY = body[i].y;
			if(x+deltaX >= this.width || y+deltaY >= this.height || x+deltaX < 0 || y+deltaY < 0) {
				return PLACE_OUT_BOUNDS;
			}
			if(grid[x+deltaX][y+deltaY] == true) {
				return PLACE_BAD;
			}
			grid[x+deltaX][y+deltaY] = true;
			if(heights[x+deltaX] < y+deltaY+1) heights[x+deltaX] = y+deltaY+1;
			widths[y+deltaY]++;
			if(heights[x+deltaX] > maxHeight) maxHeight = heights[x+deltaX];
			if(widths[y+deltaY] == this.width) result = PLACE_ROW_FILLED;
		}
		if(result != PLACE_OUT_BOUNDS && result != PLACE_BAD) sanityCheck();
		return result;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		//set undo
		if(committed == true) {
			for(int i = 0; i<grid.length; i++) {
				   System.arraycopy(grid[i], 0, xGrid[i], 0, grid[i].length);
				}
			System.arraycopy(widths, 0, xWidths, 0, widths.length);
			System.arraycopy(heights, 0, xHeights, 0, heights.length);
		}
		maxHeight = getMaxHeight();
		int start = 0;
		int startFrom = 0;
		int numClear = 0;
		//get how many rows will be cleared to update the heights
		for(int i = 0; i<widths.length; i++) {
			if(widths[i] == width) numClear++;
		}
		if(numClear == 0) {
			sanityCheck();
			committed = false;
			return 0;
		}
		// get the start position of filled
		for(int i = 0; i<widths.length; i++) {
			if(widths[i] == width) {
				start = i;
				startFrom = start;
				break;
			}
		}
		int[] to = new int[maxHeight-start];
		int[] from = new int[maxHeight-start];
		// set to and from array
		for(int i =0; i<maxHeight-start; i++) {
			to[i] = start+i;
			if(startFrom >= height) {
				from[i] = startFrom;
				startFrom++;
			}
			else {
			  for(int j = startFrom; j < height; j++) {
				  if(widths[j] != width) {
			 		  from[i] = j;
					  startFrom = j+1;
					  break;
				  }
			  }
		    }  
		}
	    // copy down	
		for(int i = 0; i<to.length; i++) {
			copy(from[i], to[i]);
		}
		// update heights
		for(int i = 0; i<heights.length; i++) {
			int max = 0;
			for(int j = 0; j<height; j++) {
				if(grid[i][j] == true) {
					if(max < j+1) max = j+1;
				}
			}
			heights[i] = max;
		}
		maxHeight = setMaxHeight();
    	sanityCheck();
    	committed = false;
		return numClear;
	}
	
	
	// copy the values of r1 to r2, also updates the widths
	private void copy(int r1, int r2) {
		if(r1 >= height) {
			for(int i = 0; i < width; i++) {
				grid[i][r2] = false;
				widths[r2] = 0;
			}
		}
		else {
		  for(int i = 0; i<width; i++) {
			  grid[i][r2] = grid[i][r1];
			  widths[r2] = widths[r1];
		  }
	    }	  
	}



	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(committed == true) return;
		for(int i = 0; i<grid.length; i++) {
		   System.arraycopy(xGrid[i], 0, grid[i], 0, xGrid[i].length);
		}   
		System.arraycopy(xWidths, 0, widths, 0, xWidths.length);
		System.arraycopy(xHeights, 0, heights, 0, xHeights.length);
		maxHeight = setMaxHeight();
		committed = true;
		sanityCheck();
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


