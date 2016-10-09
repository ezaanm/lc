package kc;

import java.util.HashMap;
import java.util.Map;

public class MaxPointsOnAPlane {
    public int maxPoints(Point[] points) {
        if(points.length < 2) return points.length;
        int max = 2;

        for(int i = 0; i < points.length; i++) {
            Map<Double, Map<Double, Integer>> map = new HashMap<Double, Map<Double, Integer>>();
            Map<Integer, Integer> xx = new HashMap<Integer, Integer>();
            int samep = 0;

            for(int j = 0; j < points.length; j++) {
            	if(i == j) continue;
            	
                Point a = points[i];
                Point b = points[j];
                
                double slope = 0;
                double yIntersection = 0;
                if((a.x == b.x) && (a.y == b.y)) {
                	samep = samep+1;
                }
                                
                if(a.x == b.x) {
                    if(xx.containsKey(a.x)) {
                        xx.put(a.x, xx.get(a.x) + 1);
                    } else {
                        xx.put(a.x, 2);
                    }
                    max = Math.max(xx.get(a.x), max);
                } else {
                    slope = ((double) a.y - (double) b.y) / ((double) a.x - (double) b.x);
                    yIntersection = (double) a.y - slope * (double) a.x;
                    if(map.containsKey(slope)) {
                        Map<Double, Integer> interVSTimes = map.get(slope);
                        if(interVSTimes.containsKey(yIntersection)) {
                            interVSTimes.put(yIntersection, interVSTimes.get(yIntersection) + 1);
                        } else {
                            interVSTimes.put(yIntersection, 2);
                        }
                        
                        max = Math.max(max, interVSTimes.get(yIntersection) + samep);
                    } else {
                        Map<Double, Integer> in = new HashMap<Double, Integer>();
                        in.put(yIntersection, 2);
                        map.put(slope, in);
                    }
                }
                
            }
        }
        
        return max;
    }
    
    public static void main(String[] args) {
		Point a = new Point();
		Point b = new Point();
		Point c = new Point();
		Point d = new Point();
		
		a.x = 1; a.y = 1;
		b.x = 1; b.y = 1;
		
		c.x = 2; c.y = 2;

		d.x = 2; d.y = 2;
		Point[] p = new Point[4];
		p[0] = a;
		p[1] = b;
		p[2] = c;
		p[3] = d;
		
		MaxPointsOnAPlane x = new MaxPointsOnAPlane();
		System.out.println(x.maxPoints(p));
	}
}
