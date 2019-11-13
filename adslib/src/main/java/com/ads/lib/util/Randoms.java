package com.ads.lib.util;


import java.util.Random;

public class Randoms {

    public static int getRandom(int start, int end) {
        Random random = new Random();
        return random.nextInt(end) + start;
    }

}
