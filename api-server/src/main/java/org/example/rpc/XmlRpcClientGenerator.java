package org.example.rpc;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.jaxb.JaxbTypeFactory;
import org.example.PathVariables;
import org.example.dto.RpcDtoList;
import org.example.error.ErrorCode;
import org.example.error.RestApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlRpcClientGenerator {

	private static final int XML_SERVER_PORT = PathVariables.getInstance().getInt("rpc.port");
	private static final String URL_PREFIX = "http://";
	private static final String URL_POSTFIX = "/xml-rpc";

	public static XmlRpcClient generate(String serverIp) {
		XmlRpcClient xmlRpcClient;
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(getAgentXmlRpcUrl(serverIp));
			config.setEnabledForExtensions(true);
			config.setConnectionTimeout(10000);

			JAXBContext jaxbContext = JAXBContext.newInstance(RpcDtoList.JAXB_DTO_ARRAY);

			xmlRpcClient = new XmlRpcClient();
			xmlRpcClient.setConfig(config);

			xmlRpcClient.setTypeFactory(new JaxbTypeFactory(xmlRpcClient, jaxbContext));
		} catch (JAXBException | MalformedURLException e) {
			log.error(e.getMessage(), e);
			throw new RestApiException(ErrorCode.XML_RPC_CLIENT_ERROR, e);
		}
		return xmlRpcClient;
	}

	private static URL getAgentXmlRpcUrl(String ip) throws MalformedURLException {
		return new URL(URL_PREFIX + ip + ":" + XML_SERVER_PORT + URL_POSTFIX);
	}

}
