package org.example.auth;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Verifier;

import com.mysql.cj.util.StringUtils;

public class TokenVerifier implements Verifier {
	private String token;

	public TokenVerifier(String psk) {
		this.token = psk;
	}

	@Override
	public int verify(Request request, Response response) {
		String clientToken = request.getHeaders().getValues("Authorization");
		if (!StringUtils.isNullOrEmpty(clientToken)){
			String parsedToken = parseToken(clientToken);
			if (parsedToken.equals(token)){
				return RESULT_VALID;
			}
		}
		return RESULT_INVALID;
	}

	private String parseToken(String token){
		return token.split("Bearer")[1].trim();
	}
}
