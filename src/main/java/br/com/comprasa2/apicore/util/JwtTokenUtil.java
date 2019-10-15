package br.com.comprasa2.apicore.util;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Date;

import java.util.HashMap;

import java.util.Map;

import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;

import br.com.comprasa2.apicore.model.Usuario;
import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;



@Component

public class JwtTokenUtil implements Serializable {



	private static final long serialVersionUID = -2550185165626007488L;



	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;



	@Value("${jwt.secret}")

	private String secret;



	//retrieve username from jwt token

	public String getUsernameFromToken(String token) {

		return getClaimFromToken(token, Claims::getSubject);

	}



	//retrieve expiration date from jwt token

	public Date getExpirationDateFromToken(String token) {

		return getClaimFromToken(token, Claims::getExpiration);

	}



	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {

		final Claims claims = getAllClaimsFromToken(token);

		return claimsResolver.apply(claims);

	}

	//for retrieveing any information from token we will need the secret key

	private Claims getAllClaimsFromToken(String token) {

		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

	}



	//check if the token has expired

	private Boolean isTokenExpired(String token) {

		final Date expiration = getExpirationDateFromToken(token);

		return expiration.before(new Date());

	}



	//generate token for user

	public String generateToken(Usuario userDetails) {

		Map<String, Object> claims = new HashMap<>();
		claims.put("username", userDetails.getUsername());
		claims.put("name", userDetails.getName());
		claims.put("status", userDetails.getStatus());
		
		return doGenerateToken(claims, userDetails.getId());

	}



	//while creating the token -

	//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID

	//2. Sign the JWT using the HS512 algorithm and secret key.

	//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)

	//   compaction of the JWT to a URL-safe string 

	private String doGenerateToken(Map<String, Object> claims, String subject) {



		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))

				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))

				.signWith(SignatureAlgorithm.HS512, secret).compact();

	}



	//validate token

	public Boolean validateToken(String token, UserDetails userDetails) {
		return (!isTokenExpired(token));
	}

	
	public Usuario getBasicInfo(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.startsWith("Basic")) {
			Usuario basicInfo = new Usuario();
	        // Authorization: Basic base64credentials
	        String base64Credentials = authorization.substring("Basic".length()).trim();
	        String credentials = new String(Base64.getDecoder().decode(base64Credentials),
	                Charset.forName("UTF-8"));
	        // credentials = username:password
	        String[] values = credentials.split(":",2);
	        basicInfo.setUserName(values[0]);
	        basicInfo.setPassword(values[1]);
	        return basicInfo;
		}
		else {
			return null;
		}
	}

	
	
}