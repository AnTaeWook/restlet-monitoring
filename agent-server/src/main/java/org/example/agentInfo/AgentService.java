package org.example.agentInfo;

import org.example.config.DependencyManager;

public class AgentService {
	AgentInfoDao agentInfoDao = new AgentInfoDao(DependencyManager.databaseManager.getDataSource());

	public AgentInfo findLastAgentInfo(){
		return agentInfoDao.findLastAgentInfo();
	}

}
