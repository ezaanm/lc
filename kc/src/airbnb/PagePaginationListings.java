package airbnb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
// http://www.1point3acres.com/bbs/thread-154499-1-1.html
// 她要求除了最后一页，每一页都要满。即使有相同id，还有就是比如说有5个id都是1，最后结果有3页，他希望第一页一个1，后面两页都两个1，而不是113
// don't modify input
//http://www.1point3acres.com/bbs/forum.php?mod=viewthread&tid=197945
/*
Host Crowding

You’re given an array of CSV strings representing search results. Results are sorted
by a score initially. A given host may have several listings that show up in these results.
Suppose we want to show 12 results per page, but we don’t want the same host to dominate
the results. Write a function that will reorder the list so that a host shows up at most
once on a page if possible, but otherwise preserves the ordering. Your program should return 
the new array and print out the results in blocks representing the pages.

Input:
*  An array of csv strings, with sort score
*  number of results per page

class Solution {
  public static void main(String [] args) {
    int PER_PAGE = 12;

    ArrayList<String> input = new ArrayList<String>();
    input.add("host_id,listing_id,score,city");
    input.add("1,28,300.1,San Francisco");
    input.add("4,5,209.1,San Francisco");
    input.add("20,7,208.1,San Francisco");
    input.add("23,8,207.1,San Francisco");
    input.add("16,10,206.1,Oakland");
    input.add("1,16,205.1,San Francisco");
    input.add("6,29,204.1,San Francisco");
    input.add("7,20,203.1,San Francisco");
    input.add("8,21,202.1,San Francisco");
    input.add("2,18,201.1,San Francisco");
    input.add("2,30,200.1,San Francisco");
    input.add("15,27,109.1,Oakland");
    input.add("10,13,108.1,Oakland");
    input.add("11,26,107.1,Oakland");
    input.add("12,9,106.1,Oakland");
    input.add("13,1,105.1,Oakland");
    input.add("22,17,104.1,Oakland");
    input.add("1,2,103.1,Oakland");
    input.add("28,24,102.1,Oakland");
    input.add("18,14,11.1,San Jose");
    input.add("6,25,10.1,Oakland");
    input.add("19,15,9.1,San Jose");
    input.add("3,19,8.1,San Jose");
    input.add("3,11,7.1,Oakland");
    input.add("27,12,6.1,Oakland");
    input.add("1,3,5.1,Oakland");
    input.add("25,4,4.1,San Jose");
    input.add("5,6,3.1,San Jose");
    input.add("29,22,2.1,San Jose");
    input.add("30,23,1.1,San Jose");
  }
 *
 * !!!!!!!!!!Important!!!!!!!!!!!!!
 * 比方说input是 1231111222，每页5个
   那么第一页是12311而不是12312
   第二页是12122
 *
 * "她要求除了最后一页，每一页都要满。即使有相同id，还有就是比如说有5个id都是1，最后结果有3页，他希望第一页一个1，后面两页都两个1，而不是113"
 * " 其实他这个是有一个类似于优先级的考量的，也就是越前面的越满足不重复的这样的一个要求。我当时说的是用hash，记录下每个id出现的次数，
 * 同时计算一共需要多少个pages,比如这里是5个1，最后需要3个pages。那第一个里就是5/3 = 1，之后是4/2 = 2，最后是2/1 = 1. follow up不用写代码。"
 *
 * "我的意思是总条目的个数，一共有几十条，除以12，计算出pages的个数。再根据相同host_id除以剩下pages的分布，不过这个只是上限，
 * 具体还得看这些条目在里面如何分布。面试的时候没讨论到这么细，就随便问了下我idea。"
 * 
 * "所以在写code的时候，并没有要求相同hostid的平均分布是么？只是followup要求了呗？"
   "是的，但有要求前面每页要满。我也是按照之前的要求写完了以后，她跟我说，其实要求前面要满，之前要求没说清楚。然后这个我改了一下，就实现了" 
   (http://www.1point3acres.com/bbs/thread-210008-1-1.html)
   
 *http://www.1point3acres.com/bbs/thread-226211-1-1.html
 *
 */
public class PagePaginationListings {
	public List<List<String>> getPage(List<String> input, int PAGE_COUNT) {
		// this question has different solutions
		// my first try is very simple: iterate through until max page limit, use a temp list to store the listing from same hosts for future iteration
		// use doubly-linked list to extract a given indexed element
		// this may not be the right answer but should be fairly close.
		// a follow up or possible variant is that the hosts should be evenly distributed, i.e when page is not full and we need to put duplicates in the same page
		// we should evenly distribute the ids. This can be very tricky though, but i don't think this is the actual question.
		List<List<String>> res = new ArrayList<>();
		List<String> cur = new ArrayList<>();
		LinkedList<String> temp = new LinkedList<>();
		Set<String> visitedHost = new HashSet<>();
		
		int tempIndex = 0;
		int overallIndex = 0;
		
		while(overallIndex < input.size()) {
			// first we take a look at previous skipped ones
			while(tempIndex < temp.size()) {
				String prevListing = temp.get(tempIndex);
				if(!visitedHost.contains(prevListing.split(",")[0])) {
					cur.add(prevListing);
					temp.remove(tempIndex);
					if(temp.isEmpty()) tempIndex = 0;
					if(cur.size() == PAGE_COUNT) {
						tempIndex = 0;
						break;
					}
				}
				tempIndex++;
			}
			
			if(cur.size() == PAGE_COUNT) {
				res.add(new ArrayList<>(cur));
				visitedHost = new HashSet<>();
				cur = new ArrayList<>();
			} else {
				String listing = input.get(overallIndex);
				if(!visitedHost.contains(listing.split(",")[0])) {
					cur.add(listing);
					visitedHost.add(listing.split(",")[0]);
				} else {
					temp.add(listing);
				}
				overallIndex++;
			}
			
		}

		// the 'duplicates' have lower order than the first timers even if duplicates might have higher scores and need to be displayed again.
		while(!temp.isEmpty() && cur.size() < PAGE_COUNT) {
			cur.add(temp.poll());
		}
		res.add(new ArrayList<>(cur));

		// if there are still listings that are not printed out and the current page is filled up, we don't just output the rest, but attempt 
		// reorder based on the rules again. 
		if(temp.size() > 0) {
			res.addAll(getPage(temp, PAGE_COUNT));
		}
		
		return res;	
	}
	
	// second solution (WRONG!!)
//    public static Map<Integer, List<String>> displayPages(List<String> input, int size) {
//        // Map Solution
//        Map<Integer, List<String>> map = new HashMap<>();
//        Map<String, Integer> pageIndex = new HashMap<>();
//        for (int i = 0; i < input.size(); i++) {
//            String[] logItems = input.get(i).split(",");
//            // Update the index
//            int hostIdPageNum = pageIndex.getOrDefault(logItems[0], 0) + 1;
//            while (map.containsKey(hostIdPageNum) && map.get(hostIdPageNum).size() == size) hostIdPageNum++;
//            map.putIfAbsent(hostIdPageNum, new ArrayList<>());
//            map.get(hostIdPageNum).add(input.get(i));
//            pageIndex.put(logItems[0], hostIdPageNum);
//        }
//        return map;
//    }
	
	
	public static void main(String[] args) {

	    ArrayList<String> input = new ArrayList<String>();
	    //input.add("host_id,listing_id,score,city");
	    input.add("1,28,300.1,San Francisco");
	    input.add("4,5,209.1,San Francisco");
	    input.add("20,7,208.1,San Francisco");
	    input.add("23,8,207.1,San Francisco");
	    input.add("16,10,206.1,Oakland");
	    input.add("1,16,205.1,San Francisco");
	    input.add("6,29,204.1,San Francisco");
	    input.add("7,20,203.1,San Francisco");
	    input.add("8,21,202.1,San Francisco");
	    input.add("2,18,201.1,San Francisco");
	    input.add("2,30,200.1,San Francisco");
	    input.add("15,27,109.1,Oakland");
	    input.add("10,13,108.1,Oakland");
	    input.add("11,26,107.1,Oakland");
	    input.add("12,9,106.1,Oakland");
	    input.add("13,1,105.1,Oakland");
	    input.add("22,17,104.1,Oakland");
	    input.add("1,2,103.1,Oakland");
	    input.add("28,24,102.1,Oakland");
	    input.add("18,14,11.1,San Jose");
	    input.add("6,25,10.1,Oakland");
	    input.add("19,15,9.1,San Jose");
	    input.add("3,19,8.1,San Jose");
	    input.add("3,11,7.1,Oakland");
	    input.add("27,12,6.1,Oakland");
	    input.add("1,3,5.1,Oakland");
	    input.add("25,4,4.1,San Jose");
	    input.add("5,6,3.1,San Jose");
	    input.add("29,22,2.1,San Jose");
	    input.add("30,23,1.1,San Jose");

	    ArrayList<String> input2 = new ArrayList<String>();
	    input2.add("1,25,10.1,Oakland");
	    input2.add("2,15,9.1,San Jose");
	    input2.add("3,19,8.1,San Jose");
	    input2.add("1,11,7.1,Oakland");
	    input2.add("1,12,6.1,Oakland");
	    input2.add("1,3,5.1,Oakland");
	    input2.add("1,4,4.1,San Jose");
	    input2.add("2,6,3.1,San Jose");
	    input2.add("2,22,2.1,San Jose");
	    input2.add("2,23,1.1,San Jose");
	    input2.add("2,23,1.1,San Jose");
	    input2.add("2,23,1.1,San Jose");
	    
	    
	    PagePaginationListings x = new PagePaginationListings();
	    List<List<String>> res = x.getPage(input, 12);
	    for(List<String> list : res) {
	    	for(String a : list) {
	    		System.out.print(a.split(",")[0] + ",");
	    	}
	    	System.out.println();
	    }
	    System.out.println();
	    List<List<String>> res2 = x.getPage(input2, 5);
	    for(List<String> list : res2) {
	    	for(String a : list) {
	    		System.out.print(a.split(",")[0] + ",");
	    	}
	    	System.out.println();
	    }
	}
}
