package com.ads.lib.util;

import java.util.Random;

public class PossibilityCalculator {

	private static Random sRandom = new Random();
	public static boolean satisfyPossibility(float possibility){
		if(possibility <= 0f){
			possibility = 0f;
		    return false;
		}
		
		if(possibility >= 1f){
			possibility = 1f;
			return true;
		}
		/*long time = SystemClock.elapsedRealtime();
		long rem = time%100;
		int res = (int) (100*possibility);
		if(rem <= res)
			return true;*/
		float random = sRandom.nextFloat();
		if(random <= possibility)
			return true;
		return false;
	}

}
