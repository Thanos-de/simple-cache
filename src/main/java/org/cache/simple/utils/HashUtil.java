package org.cache.simple.utils;

import java.util.zip.CRC32;

public class HashUtil {
	
	
	public static Integer hash(String key, int count) {
		CRC32 crc32 = new CRC32();
		crc32.update(key.getBytes());
		return (int) (Math.abs(crc32.getValue()) % count);
	}

}
