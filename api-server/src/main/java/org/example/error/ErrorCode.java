package org.example.error;

import org.restlet.data.Status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	SAMPLE_CODE(Status.CLIENT_ERROR_BAD_REQUEST, "Sample Error Message."),
	UNAUTHORIZED_USER_ERROR(Status.CLIENT_ERROR_UNAUTHORIZED, "인증받지 못한 회원입니다."),
	XML_RPC_CLIENT_ERROR(Status.SERVER_ERROR_INTERNAL, "Xml-Rpc 통신 에러입니다."),
	ZOOKEEPER_ERROR(Status.SERVER_ERROR_INTERNAL, "Zookeeper 통신 에러입니다."),
	SQL_SESSION_ERROR(Status.SERVER_ERROR_INTERNAL, "Sql 생성 에러입니다."),
	SCHEDULER_START_ERROR(Status.SERVER_ERROR_INTERNAL, "Scheduler 실행 에러입니다."),
	INTERNAL_RESOURCE_ERROR(Status.SERVER_ERROR_INTERNAL, "Server Resource 에러입니다."),
	AGENT_NOT_EXISTS_ERROR(Status.CLIENT_ERROR_NOT_FOUND, "존재하지 않는 에이전트 입니다."),
	AGENT_REGISTER_ERROR(Status.CLIENT_ERROR_NOT_FOUND, "에이전트 등록 에러입니다."),
	BAD_ACTION_REQUEST_ERROR(Status.CLIENT_ERROR_BAD_REQUEST, "잘못된 시스템 제어 명령입니다."),
	;

	private final Status status;
	private final String message;

}
