package kc;

public class ShortestPalindrome {
    public String shortestPalindrome(String s) {
    	if(s.length() <= 1) return s;
        int res = getPivotPoint(s);
        StringBuilder sb = new StringBuilder();
        for(int i = s.length()-1; i > res; i--) {
        	sb.append(s.charAt(i));
        }
        sb.append(s);
        return sb.toString();
    }
    
    private int getPivotPoint(String s) {
    	for(int i = s.length() - 1; i >= 1; i--) {
    		if(isPalin(s, 0, i)) {
    			return i;
    		}
    	}
    	return 0;
    }
    
    private boolean isPalin(String s, int i, int j) {
    	while( i < j) {
    		if(s.charAt(i) != s.charAt(j)) return false;
    		i++;
    		j--;
    	}
    	return true;
    }
    
    // key is to find the longest suffix that's also a prefix since they are in reverse positions
    //, it means they both are palindromes
    // it did not use kmp to search for a string within a string (or needle in a haystack, which is what kmp 
    // intends to solve. However, the prefix suffix array that's required in the kmp pre-processing is 
    // used to identify the reversed prefix is also the match of an actual prefix starting from the beginning.
    
    public String shortestPalindrome2(String s) {
        if(s.length() == 0) return "";
        if(s.length() == 1) return s;
        StringBuilder sb = new StringBuilder(s);
        String res = sb.toString() + " " + sb.reverse().toString();
        int[] kmp = new int[res.length()];
        int i = 0;
        int j = 1;
        while(j < res.length()) {
            if(res.charAt(j) == res.charAt(i)) {
                kmp[j] = i + 1;
                j++;
                i++;
            } else {
                if(i == 0) {
                    kmp[j] = 0;
                    j++;
                    continue;
                }
                
                while(i > 0) {
                    i = kmp[i-1];
                    if(res.charAt(j) == res.charAt(i)) {
                        kmp[j] = i + 1;
                        j++;
                        i++;
                        break;
                    }
                    
                    if (i == 0) {
                        kmp[j] = 0;
                        j++;
                        break;
                    }            
                }
            }
            
        }
        int len = kmp[kmp.length-1];
        StringBuilder newSb = new StringBuilder(s);
        return newSb.reverse().substring(0, s.length() - len) + s;
    }
    
    
    public String getShortest(String s) {
    	if(s.length() <= 1) return s;
    	StringBuilder sb = new StringBuilder(s);
    	String needle = sb.toString() + "&" + sb.reverse().toString();
    	int[] longestProperSuffix = new int[needle.length()];
    	int currentPointer = 0;
    	for(int i = 1; i < needle.length(); i++) {
    		if(needle.charAt(currentPointer) == needle.charAt(i)) {
    			longestProperSuffix[i] = currentPointer+1;
    			currentPointer++;
    		} else {
    			if(currentPointer==0) {
    				continue;
    			} else {
    				currentPointer = longestProperSuffix[currentPointer - 1];
    				i--;
    			}
    		}
    	}
    	
    	
    	int finalLenOfPrefixThatsAPalindrome = longestProperSuffix[needle.length()-1];
    	String toAppend = s.substring(finalLenOfPrefixThatsAPalindrome);
    	sb = new StringBuilder(toAppend);
    	return sb.reverse().toString() + s;

    }
    
    
    
    public static void main(String[] args) {
    	ShortestPalindrome x = new ShortestPalindrome();
    	String str = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab";
    	System.out.println(x.shortestPalindrome2(str));
    	System.out.println(x.getShortest(str));
    	
    }
}
