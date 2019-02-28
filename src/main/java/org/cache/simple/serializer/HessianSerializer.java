package org.cache.simple.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

public class HessianSerializer implements Serializer {

	@Override
	public byte[] serialize(Object obj) throws Exception {
		if(obj==null) throw new NullPointerException();  
	      
	    ByteArrayOutputStream os = new ByteArrayOutputStream();  
	    HessianOutput ho = new HessianOutput(os);  
	    ho.writeObject(obj);  
	    return os.toByteArray(); 
	}

	@Override
	public Object deserialize(byte[] by, Class<?> paramClass) throws Exception {
		if(by==null) throw new NullPointerException();  
	      
	    ByteArrayInputStream is = new ByteArrayInputStream(by);  
	    HessianInput hi = new HessianInput(is);  
	    return hi.readObject();  
	}

}
