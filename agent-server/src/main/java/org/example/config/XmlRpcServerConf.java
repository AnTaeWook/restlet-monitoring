package org.example.config;

import javax.xml.bind.JAXBContext;

import org.apache.xmlrpc.jaxb.JaxbTypeFactory;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;
import org.example.AgentProcedureImpl;
import org.example.PathVariables;
import org.example.dto.RpcDtoList;
import org.example.rpc.AgentProcedure;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlRpcServerConf {
	private final int port = getPort();

	public void start() {
		WebServer webServer = new WebServer(port);

		try {
			XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

			JAXBContext jaxbContext = JAXBContext.newInstance(RpcDtoList.JAXB_DTO_ARRAY);
			xmlRpcServer.setTypeFactory(new JaxbTypeFactory(xmlRpcServer, jaxbContext));

			PropertyHandlerMapping phm = new PropertyHandlerMapping();
			phm.setVoidMethodEnabled(true);
			phm.addHandler(AgentProcedure.class.getName(), AgentProcedureImpl.class);
			xmlRpcServer.setHandlerMapping(phm);

			XmlRpcServerConfigImpl serverConfig = new XmlRpcServerConfigImpl();
			serverConfig.setEnabledForExtensions(true);
			serverConfig.setContentLengthOptional(false);
			xmlRpcServer.setConfig(serverConfig);

			webServer.start();
			log.info("Xml-Rpc Server Start : {}", port);
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}
	}

	private int getPort(){
		return PathVariables.getInstance().getInt("server.port");
	}
}
