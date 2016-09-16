import java.util.*;

public class counter {
	public static List<ArrayList<Double>> cut(List<Double> Start, List<Double> End) {
		//output = List of arrays where each array = <Interval(start), Interval(end)>
		
		//Error Checking
		if (Start.size() != End.size()) {
			System.out.println("Number of starting and ending points don\'t match.");
		}
		
		//Combining starting and ending points and ordering them
		List<Double> Combined = new ArrayList<Double>();
		Combined.addAll(Start);
		Combined.addAll(End);
		Collections.sort(Combined);
		//sortnum(Combined);
		Combined = removerepeats(Combined);
		
		for (int k1 = 0; k1 < Combined.size(); k1++) {
			System.out.println(Combined.get(k1));
		}
		
		List<ArrayList<Double>> results = new ArrayList<ArrayList<Double>>();
		for (int k1 = 0; k1 < Combined.size()-1; k1++) {
			ArrayList<Double> range = new ArrayList<Double>();
			range.add(Combined.get(k1));
			range.add(Combined.get(k1+1));
			results.add(range);
		}
		return results;
	}
	
    public static List<Double> removerepeats(List<Double> a) {
    	ArrayList<Double> output = new ArrayList<Double>();
    	for (int k1 = 0; k1 < a.size(); k1++) {
    		
			if (k1 > 0) {
				if (a.get(k1).equals(a.get(k1-1)) == false) {
					output.add(a.get(k1));
				} 
			} else {
				output.add(a.get(k1));
			}
		}
    	
    	return output;
    }
    
    public static double datetime2value(ArrayList<Double> Time) {
    	//format for Times: <Day#, hour, minutes> (24-hour) 
    	//Day# of the first day is 1
    	double DayNum = Time.get(0);
    	double Hour = Time.get(1);
    	double Minutes = Time.get(2);
    	
    	double output = ((DayNum - 1)*24*60) + (Hour*60) + Minutes;
    	
    	return output;
    }
    
    public static ArrayList<Double> value2datetime(double Value) {
    	double DayNum = Math.floor(Value/(24*60)) + 1;
    	double rem = Value - ((DayNum-1)*24*60);
    	double Hour = Math.floor(rem/60);
    	double Minutes = rem - (Hour*60);
    	
    	ArrayList<Double> output = new ArrayList<Double>();
    	output.add(DayNum);
    	output.add(Hour);
    	output.add(Minutes);
    	
    	return output;
    }
    
    public static String[][] schedule(List<String> B, List<String> SB, List<ArrayList<Double>> T, double FlightIn, double FlightOut) {
    	// B contains a list of businesses, and T contains a list of arrays, each displaying each business's hours of operation
    	// For instance: <2,3> = 2 to 3, and <2,3,6,7> = 2 to 3 and 6 to 7.
    	// SB = "super" businesses which take precedence over all other businesses (assuming no 2 "super" businesses overlap)
    	
    	//add [FlightIn, FlightOut] as a "super" business
    	ArrayList<Double> FlyingIn = new ArrayList<Double>();
    	FlyingIn.add(-1.0);
    	FlyingIn.add(FlightIn);
    	B.add("flyingin");
    	SB.add("flyingin");
    	T.add(FlyingIn);
    	
    	ArrayList<Double> FlyingOut = new ArrayList<Double>();
    	FlyingOut.add(FlightOut);
    	FlyingOut.add(1e7);
    	B.add("flyingout");
    	SB.add("flyingout");
    	T.add(FlyingOut);
    	    	
    	ArrayList<Double> S = new ArrayList<Double>();
    	ArrayList<Double> E = new ArrayList<Double>();
    	
    	for (int k1 = 0; k1 < B.size(); k1++) {
    		List<Double> Times = T.get(k1);
    		for (int k2 = 0; k2 < ((double) Times.size())/2; k2++) {
    			S.add(Times.get(2*k2));
    			E.add(Times.get(2*k2 + 1));
    		}
    	}
    	
    	
    	
    	List<ArrayList<Double>> IntervalAndCounter = cut(S,E);
    	
    	List<ArrayList<Double>> Intervals =  new ArrayList<ArrayList<Double>>();
    	for (int k1 = 0; k1 < IntervalAndCounter.size(); k1++) {
    		ArrayList<Double> Range = new ArrayList<Double>();
    		Range.add(IntervalAndCounter.get(k1).get(0));
    		Range.add(IntervalAndCounter.get(k1).get(1));
    		Intervals.add(Range);
    	}
       	
    	List<ArrayList<String>> Businesses = new ArrayList<ArrayList<String>>();
    	for (int k1 = 0; k1 < Intervals.size(); k1++) {
    		ArrayList<String> BusinessList = new ArrayList<String>();
    		for (int k2 = 0; k2 < B.size(); k2++) {
    			for (int k3 = 0; k3 < ((double) T.get(k2).size())/2; k3++) {
    				if (T.get(k2).get(2*k3) <= Intervals.get(k1).get(0) &&
    						T.get(k2).get(2*k3+1) >= Intervals.get(k1).get(1)) {
    					BusinessList.add(B.get(k2));
    					break;
    				}    					
    			}
    		}
    		Businesses.add(BusinessList);
    	}
    	
    	//take account of "super" businesses
    	for (int k1 = 0; k1 < Businesses.size(); k1++) {
    		int marker = 0;
    		for (int k4 = 0; k4 < Businesses.get(k1).size(); k4++) {
    			if (Businesses.get(k1).get(k4).equals("flyingin")) {
    				marker = 1;
    				break;
    			} else if (Businesses.get(k1).get(k4).equals("flyingout")) {
    					marker = 2;
    					break;
    			}    				
    		}
    		for (int k2 = 0; k2 < Businesses.get(k1).size(); k2++) {
    			for (int k3 = 0; k3 < SB.size(); k3++) {
    				if (Businesses.get(k1).get(k2).equals(SB.get(k3)) & marker == 0) { 
    					Businesses.get(k1).clear();
    					Businesses.get(k1).add(SB.get(k3));
    					break;
    				} else if (Businesses.get(k1).get(k2).equals(SB.get(k3)) & marker == 1) {
    					Businesses.get(k1).clear();
    					Businesses.get(k1).add("flyingin");
    					break;
    				} else if (Businesses.get(k1).get(k2).equals(SB.get(k3)) & marker == 2) {
    					Businesses.get(k1).clear();
    					Businesses.get(k1).add("flyingout");
    					break;
    				}
    			}
    		}
    	}
    	
    	List<ArrayList<Double>> newIntervals = new ArrayList<ArrayList<Double>>();
    	for (int k1 = 0; k1 < Intervals.size(); k1++) {
    		if (Businesses.get(k1).size() == 1) {
    			newIntervals.add(Intervals.get(k1));
    		} else if (Businesses.get(k1).size() > 1) {
    			int size = Businesses.get(k1).size();
    			double dt = (Intervals.get(k1).get(1) - Intervals.get(k1).get(0))/size;
    			for (int k2 = 0; k2 < size; k2++) {
    				ArrayList<Double> miniInterval = new ArrayList<Double>();
    				if (k2 == 0) {
    					miniInterval.add(Intervals.get(k1).get(0));
    					miniInterval.add(Intervals.get(k1).get(0) + dt);
    					newIntervals.add(miniInterval);
    				} else if (k2 != 0) {
    					double pastnum = newIntervals.get(newIntervals.size()-1).get(1);
    					miniInterval.add(pastnum);
    					if (k2 != size - 1) {
    					miniInterval.add(pastnum + dt);
    					} else if (k2 == size - 1) {
    						miniInterval.add(Intervals.get(k1).get(1));
    					}
    					newIntervals.add(miniInterval);
    				}    					
    			}
    		}
    	}
    	//combine businesses
    	List<String> CombineBusinesses = new ArrayList<String>();    	
    	for (int k1 = 0; k1 < Businesses.size(); k1++) {
    		if (k1 == 0) {
    			CombineBusinesses.addAll(Businesses.get(k1));
    		} else {
    			int except = -1;
    			int size2 = CombineBusinesses.size();
    			for (int k2 = 0; k2 < Businesses.get(k1).size(); k2++) {
    				if (Businesses.get(k1).get(k2).equals(CombineBusinesses.get(size2-1))) {
    					CombineBusinesses.add(Businesses.get(k1).get(k2));
    					except = k2;
    					break;
    				}    			
    			}
    			if (except != -1) {
    				for (int k2 = 0; k2 < Businesses.get(k1).size(); k2++) {
    					if (k2 != except) {
    						CombineBusinesses.add(Businesses.get(k1).get(k2));
    					}
    				}
    			} else {
    				CombineBusinesses.addAll(Businesses.get(k1));
    			}    			
    		}    		
    	}
    	
    	//combine to make a proper schedule
    	List<String> finalBusinesses = new ArrayList<String>();
    	ArrayList<Double> tempIntervals = new ArrayList<Double>();
    	
    	for (int k1 = 0; k1 < CombineBusinesses.size(); k1++) {
    		if (CombineBusinesses.get(k1).equals("flyingin") == false &&
    				CombineBusinesses.get(k1).equals("flyingout") == false) {
    			if (k1 == 0) {
    				finalBusinesses.add(CombineBusinesses.get(0));
    				tempIntervals.add(newIntervals.get(k1).get(0));
    			} 
    			if (k1 != 0 && k1 != CombineBusinesses.size()-1){
    				if (CombineBusinesses.get(k1).equals(CombineBusinesses.get(k1+1)) == false &&
    						CombineBusinesses.get(k1).equals(CombineBusinesses.get(k1-1))) {
    					tempIntervals.add(newIntervals.get(k1).get(1));
    				}
    				if (CombineBusinesses.get(k1).equals(CombineBusinesses.get(k1+1)) &&
    						CombineBusinesses.get(k1).equals(CombineBusinesses.get(k1-1)) == false) {
    					finalBusinesses.add(CombineBusinesses.get(k1));
    					tempIntervals.add(newIntervals.get(k1).get(0));
    				} 
    				if (CombineBusinesses.get(k1).equals(CombineBusinesses.get(k1-1)) == false &&
    						CombineBusinesses.get(k1).equals(CombineBusinesses.get(k1+1)) == false) {
    					finalBusinesses.add(CombineBusinesses.get(k1));
    					tempIntervals.addAll(newIntervals.get(k1));
    				}
    			}
    			if (k1 == CombineBusinesses.size() - 1) {
    				if (CombineBusinesses.get(k1).equals(CombineBusinesses.get(k1-1))) {
    					tempIntervals.add(newIntervals.get(k1).get(1));    				    				
    				} else {
    					tempIntervals.addAll(newIntervals.get(k1));
    					finalBusinesses.add(CombineBusinesses.get(k1));
    				}
    			}
    		}    		
    	}
        String[][] output = new String[finalBusinesses.size()][4];
        for (int k1 = 0; k1 < finalBusinesses.size(); k1++) {
        	ArrayList<Double> x = value2datetime(tempIntervals.get(2*k1));
        	String Date = "";
        	Date = Date + x.get(0);
        	String StartTime = "";
        	StartTime = StartTime + x.get(1) + ":" + x.get(2);
        	ArrayList<Double> y = value2datetime(tempIntervals.get(2*k1));
        	String EndTime = "";
        	EndTime = EndTime + y.get(1) + ":" + y.get(2);
        	
        	output[k1][0] = finalBusinesses.get(k1); 
        	output[k1][1] = Date;
        	output[k1][2] = StartTime;
        	output[k1][3] = EndTime;
        }
    	
        for (int k1 = 0; k1 < output.length; k1++) {
        	for (int k2 = 0; k2 < 4; k2++) {
        		System.out.println(output[k1][k2]);
        	}
        }
    	return output;
    }

	public static void main(String[] args) {
		List<String> Bedit = new ArrayList<String>();
		Bedit.add("lol");
		Bedit.add("lolol");
		Bedit.add("lololol");
		Bedit.add("lolololol");
		Bedit.add("lololololol");
		Bedit.add("lololololololmao");
		
		List<String> SBedit = new ArrayList<String>();

		SBedit.add("lolololol");
		SBedit.add("lololololololmao");

		List<ArrayList<Double>> T = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> test1 = new ArrayList<Double>();
		test1.add(1.0);
		test1.add(2.5);
		test1.add(3.0);
		test1.add(4.0);
		ArrayList<Double> test2 = new ArrayList<Double>();
		test2.add(2.0);
		test2.add(3.0);
		test2.add(5.0);
		test2.add(7.0);
		ArrayList<Double> test3 = new ArrayList<Double>();
		test3.add(1.0);
		test3.add(2.5);
		test3.add(5.5);
		test3.add(7.5);
		ArrayList<Double> test4 = new ArrayList<Double>();
		test4.add(6.0);
		test4.add(8.0);
		test4.add(3.0);
		test4.add(4.0);
		ArrayList<Double> test5 = new ArrayList<Double>();
		test5.add(3.0);
		test5.add(5.0);
		ArrayList<Double> test6 = new ArrayList<Double>();
		test6.add(2.0);
		test6.add(2.5);
		T.add(test1);
		T.add(test2);
		T.add(test3);
		T.add(test4);
		T.add(test5);
		T.add(test6);
		//System.out.println(T.get(0).size());
		String[][] x = schedule(Bedit,SBedit,T,1.3,7.1);
		
//    	for (int k1 = 0; k1 < x.length; k1++) {
//    		System.out.println(x[k1]);
//    	}
	}
}
