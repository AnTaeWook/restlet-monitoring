# RESTLET_TEAM A

---

## 소개

본 프로젝트는 관제 서버에서 에이전트의 메트릭을 수집하고, 이를 모니터링 및 조작 할 수 있도록 API를 제공하는 역할을 할 수 있도록 한다.

시스템 명령어를 통한 os 이해도와 다음과 같은 기술스택을 사용하여 업무 적응력을 높인다.

- JAVA 8
- Restlet-2.4.3
- Mybatis-3.5.13
- MySQL-8.0.31
- XML-RPC-3.1.3
- Zookeeper-3.8.1


### 아키텍처

![](https://gitlab.gabia.com/cloud/system/onboarding/2023/restlet-team-a/uploads/8490c1b4edb912860021a1544ff1bc8c/image.png)


### 디렉토리 구조

```shell
Restlet TEAM A
├── README.md
├── agent-server # 에이전트 프로젝트
│   └── ...
├── api-server # 관제 서버 프로젝트 
│   └── ...
├── module-common # 프로젝트 간의 공통 모듈
│   └── ...
└── setting.gradle # 멀티 프로젝트 설정
```

### 프로젝트 문서

[관제서버](api-server/API-README.MD)

[에이전트](agent-server/AGENT-README.MD)