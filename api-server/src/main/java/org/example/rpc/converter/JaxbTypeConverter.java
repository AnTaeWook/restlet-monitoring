package org.example.rpc.converter;

import javax.xml.bind.Element;

import org.apache.xmlrpc.common.TypeConverter;

public class JaxbTypeConverter implements TypeConverter {

	@Override
	public boolean isConvertable(Object pObject) {
		return pObject == null || pObject instanceof Element;
	}

	@Override
	public Object convert(Object pObject) {
		return pObject;
	}

	@Override
	public Object backConvert(Object result) {
		return result;
	}

}
