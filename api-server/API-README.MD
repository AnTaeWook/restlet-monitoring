# 관제 서버

---

## 소개

각각의 VM에 설치된 에이전트를 통하여 VM의 매트릭 정보를 수집하고, 시스템을 조작할 수 있는 API를 제공한다.

### 아키텍처

![](https://gitlab.gabia.com/cloud/system/onboarding/2023/restlet-team-a/uploads/46f5e955c9517dd98a1e47f24d88c4bc/restlet_agent.drawio__1_.png)



### ERD

![](https://gitlab.gabia.com/cloud/system/onboarding/2023/restlet-team-a/uploads/5a7daf36f0cd9feae17169dd8f1853da/image.png)

### 디렉토리 구조

```shell
├───● auth # 인증 관련 모듈
├───● common # 모든 패키지에서 사용되는 모듈
├───● dto
├───● entity
├───● error # 예외 관련 모듈
├───● guice # di 모듈
├───● mapper
├───● repository
├───● resource # Restlet ServerResource (Controller)
├───● rpc # Xml-Rpc Client 관련 모듈
├───● service
├───● zookeeper # Zookeeper Client 모듈
├─── RestletApplication.java
├─── DtoConverter.java
└─── ApiServer.java
```

에이전트와 달리 API 서버로의 확장 가능성을 염두하여 각각의 의존성, 역할에 따른 계층 구조를 나누었다. 