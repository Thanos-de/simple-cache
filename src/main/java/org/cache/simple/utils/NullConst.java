package org.cache.simple.utils;

import java.util.Arrays;

public class NullConst {
	public final static byte[] nullByte = {1,2,3,2,1,0};
	
	public static Boolean AssertNull(byte[] by){
		return Arrays.equals(nullByte, by);
	}
}
