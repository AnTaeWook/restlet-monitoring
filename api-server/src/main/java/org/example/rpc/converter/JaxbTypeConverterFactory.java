package org.example.rpc.converter;

import javax.xml.bind.Element;

import org.apache.xmlrpc.common.TypeConverter;
import org.apache.xmlrpc.common.TypeConverterFactoryImpl;

public class JaxbTypeConverterFactory extends TypeConverterFactoryImpl {

	private final TypeConverter jaxbTypeConverter = new JaxbTypeConverter();

	@Override
	public TypeConverter getTypeConverter(Class pClass) {
		if (Element.class.isAssignableFrom(pClass)) {
			return jaxbTypeConverter;
		}
		return super.getTypeConverter(pClass);
	}

}
