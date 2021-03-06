package kc;

import java.util.ArrayList;
import java.util.List;

/**
 * A = [
  [ 1, 0, 0],
  [-1, 0, 3]
]
B = [
  [ 7, 0, 0 ],
  [ 0, 0, 0 ],
  [ 0, 0, 1 ]
]
     |  1 0 0 |   | 7 0 0 |   |  7 0 0 |
AB = | -1 0 3 | x | 0 0 0 | = | -7 0 3 |
                  | 0 0 1 |
 */
public class SparseMatrixMultiplication {
    public int[][] multiply(int[][] A, int[][] B) {
    	if(A.length == 0 || A[0].length == 0 || B.length == 0 || B[0].length == 0) return null;
        int[][] res = new int[A.length][B[0].length];
        for(int i = 0 ; i < A.length; i++) {
        	for(int j = 0 ; j < A[0].length; j++) {
        		if(A[i][j] == 0) continue;
        		for(int k = 0 ; k < B[0].length; k++) {
        			if(B[j][k] != 0) {
        				res[i][k] += A[i][j] * B[j][k];
        			}
        		}
        	}
        }
        return res;
    }
    
    public int[][] multiply2(int[][] A, int[][] B) {
        if(A.length == 0 || B.length ==0) return new int[0][0];
        List<List<Integer>> a = new ArrayList<>();
        List<List<Integer>> b = new ArrayList<>();
        
        for(int i = 0 ; i < A.length; i++) {
            List<Integer> list = new ArrayList<>();
            for(int j = 0; j < A[0].length; j++) {
                if(A[i][j] != 0) {
                    list.add(j);
                }
            }
            a.add(list);
        }
        
        for(int i = 0 ; i < B[0].length; i++) {
            List<Integer> list = new ArrayList<>();
            for(int j = 0; j < B.length; j++) {
                if(B[j][i] != 0) {
                    list.add(j);
                }
            }
            b.add(list);
        }
        
        int[][] res = new int[A.length][B[0].length];
        
        for(int i = 0 ; i < a.size(); i++) {
            List<Integer> aa = a.get(i);
            if(aa.isEmpty()) continue;
            for(int j = 0 ; j < b.size(); j++) {
                List<Integer> bb = b.get(j);
                if(bb.isEmpty()) continue;
                
                int aStart = 0;
                int bStart = 0;
                while(aStart < aa.size() && bStart < bb.size()) {
                    if(aa.get(aStart) == bb.get(bStart)) {
                        res[i][j] += A[i][aa.get(aStart)] * B[bb.get(bStart)][j];
                        aStart++;
                        bStart++;
                    } else if(aa.get(aStart) > bb.get(bStart)) {
                        bStart++;
                    } else {
                        aStart++;
                    }
                }
            }
        }
        
        return res;
    }
    
    
    public int[][] multiply3(int[][] A, int[][] B) {
        List<List<Point>> a = new ArrayList<>();
        for(int i = 0 ; i < A.length; i++) {
            List<Point> list = new ArrayList<>();
            for(int j = 0 ; j < A[0].length; j++) {
                if(A[i][j] == 0) continue;
                Point point = new Point(i,j,A[i][j]);
                list.add(point);
            }
            if(list.size() > 0) a.add(list);
        }
        
        List<List<Point>> b = new ArrayList<>();
        for(int i = 0 ; i < B[0].length; i++) {
            List<Point> list = new ArrayList<>();
            for(int j = 0 ; j < B.length; j++) {
                if(B[j][i] == 0) continue;
                Point point = new Point(j, i, B[j][i]);
                list.add(point);
            }
            b.add(list);
        }
        
        int[][] res = new int[A.length][B[0].length];
        
        int aa = 0;
        int bb = 0;
        
        while(aa < a.size() ) {
            
            bb = 0;
            while(bb < b.size()) {
            
                List<Point> aList = a.get(aa);
                List<Point> bList = b.get(bb);
                
                int i = 0;
                int j = 0;
                
                while(i < aList.size() && j < bList.size()) {
                    Point pointA = aList.get(i);
                    Point pointB = bList.get(j);
                    
                    if(pointA.y == pointB.x) {
                        res[pointA.x][pointB.y] += pointA.v * pointB.v;
                        i++;
                        j++;
                    } else if(pointA.y > pointB.x) {
                        j++;
                    } else {
                        i++;
                    }
                }
                
                bb++;
            }
            aa++;
            
        }
        return res;
    }
    
    public class Point {
        int x;
        int y;
        int v;
        public Point(int x, int y, int v) {
            this.x = x;
            this.y = y;
            this.v = v;
        }
    }
    
    
    public static void main(String[] args) {
    	int[][] A = new int[2][3];
    	A[0][0] = 1;
    	A[0][1] = 0;
    	A[0][2] = 0;
    	A[1][0] = -1;
    	A[1][1] = 0;
    	A[1][2] = 3;
    	
    	int[][] B = new int[3][3];
    	B[0][0] = 7;
    	B[0][1] = 0;
    	B[0][2] = 0;
    	B[1][0] = 0;
    	B[1][1] = 0;
    	B[1][2] = 0;
    	B[2][0] = 0;
    	B[2][1] = 0;
    	B[2][2] = 1;
    	
    	SparseMatrixMultiplication x = new SparseMatrixMultiplication();
    	int[][] res = x.multiply(A, B);
    	for(int i = 0 ; i < res.length; i++) {
    		for(int j =0 ; j < res[0].length; j++) {
    			System.out.print(res[i][j] + ",");
    		}
    		System.out.println();
    	}
    	
    }
}
