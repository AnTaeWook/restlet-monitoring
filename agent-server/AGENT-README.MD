# 에이전트

---

## 소개

VM의 매트릭 정보를 수집하여 관제 서버에 전달한다.


### 아키텍처

![](https://gitlab.gabia.com/cloud/system/onboarding/2023/restlet-team-a/uploads/cbb666893626db025fd320e9151706f0/restlet_agent.drawio.png)



### ERD

![](https://gitlab.gabia.com/cloud/system/onboarding/2023/restlet-team-a/uploads/aeef4d4ec54b1e23b94119783390b0e1/image.png)

### 디렉토리 구조

```shell
├───●agentInfo # Agent Info 엔티티 관련 기능들
│     ├───AgentInfo
│     ├───AgentInfoDao
│     └───AgentService
├───●config # 의존성 설정파일
│     ├───DatabaseManager
│     ├───DependencyManager
│     ├───MetricScheduler
│     ├───XmlRpcServerConf
│     └───ZookeeperConnection
├───●metric # Metric 엔티티 관련 기능들
│     └───metricInfo # 매트릭 객체
│         ├───NetworkMetricInfo
│         └───SystemMetricInfo
│     └───utils
│         └───systemCommand # 매트릭 수집
│              ├───CentosCommandUtilImpl
│              ├───OsCommandUtil
│              ├───OsCommandUtilFactory
│              └───OsTemplateCommandUtilImpl
│         └───SystemdNotify
│     ├───Metric
│     ├───MetricDao
│     └───MetricJob
├───AgentProcedureImpl # xml-rpc 명령어 구현체
└───Main
```

디렉토리는 엔티티와 설정파일로 구분하여 관리한다. 별도의 계층구조로 디렉터리를 나누지 않은 이유는 하나의 엔티티 안에서만 로직이 수행되고 변경의 여지가 크지가 않다. 

따라서, 각각의 엔티티 디렉토리에서 역할을 담당하고 `AgentProcedureImpl`에서 `xml-rpc`명령을 수행한다.

