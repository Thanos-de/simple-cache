package org.cache.simple.serializer;

public interface Serializer {
	
	byte[] serialize(Object paramObject) throws Exception;

	Object deserialize(byte[] by, Class<?> paramClass) throws Exception;

}
