package org.cache.simple.consts;

import java.util.Arrays;

public class NullConst {
	public final static byte[] nullByte = {0,1,2,3};
	
	public static Boolean AssertNull(byte[] by){
		return Arrays.equals(nullByte, by);
	}
}
