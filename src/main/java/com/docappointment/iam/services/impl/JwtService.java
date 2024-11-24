package com.docappointment.iam.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
	
	private String JWT_SECRET = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";

	/**
	 * Extracts the username from the JWT token.
	 *
	 * This method extracts the subject claim ("sub") from the JWT token, which represents the username.
	 *
	 * @param jwt the JWT token
	 * @return the username extracted from the JWT token
	 */
	public String extractUsername(String jwt) {
		return extractClaim(jwt, Claims::getSubject); // the {sub:""} field
	}
	
	/**
	 * Checks if the JWT token is valid for the given UserDetails.
	 *
	 * This method verifies if the token is valid by comparing the username extracted from the token with the username
	 * from the UserDetails. It also checks if the token has expired.
	 *
	 * @param token the JWT token to validate
	 * @param userDetails the UserDetails of the user
	 * @return true if the token is valid for the user, false otherwise
	 */
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
	
	/**
	 * Checks if the JWT token has expired.
	 *
	 * This method checks if the expiration date of the token is before the current date and time, indicating that
	 * the token has expired.
	 *
	 * @param token the JWT token to check for expiration
	 * @return true if the token has expired, false otherwise
	 */
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * Extracts the expiration date from the JWT token.
	 *
	 * This method extracts the expiration claim ("exp") from the JWT token, which represents the expiration date
	 * of the token.
	 *
	 * @param token the JWT token
	 * @return the expiration date extracted from the JWT token
	 */
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * Generates a JWT token for the given UserDetails.
	 *
	 * This method generates a JWT token for the specified UserDetails. It includes any extra claims provided
	 * in the extraClaims map. The token includes the subject claim  set to the username of the UserDetails,
	 * the issued at claim set to the current date and time, and the expiration claim set to a date
	 * and time in the future.
	 *
	 * @param userDetails the UserDetails object representing the user
	 * @return the generated JWT token
	 */
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<String, Object>(), userDetails);
	}
	
	/**
	 * Generates a JWT token for the given UserDetails with extra claims.
	 *
	 * This method generates a JWT token for the specified UserDetails with additional custom claims provided in the
	 * extraClaims map. The token includes the subject claim ("sub") set to the username of the UserDetails,
	 * the issued at claim ("iat") set to the current date and time, and the expiration claim ("exp") set to a date
	 * and time in the future. The extraClaims map allows adding any additional claims to the token payload.
	 *
	 * @param extraClaims a map containing extra claims to be included in the token payload
	 * @param userDetails the UserDetails object representing the user
	 * @return the generated JWT token
	 */
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *24))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	/**
	 * Extracts a specific claim value from the JWT token.
	 *
	 * This method extracts a specific claim value from the JWT token by providing a claimsResolver function that
	 * determines the desired claim to extract. The claimsResolver function takes a Claims object and returns the
	 * desired claim value.
	 *
	 * @param token the JWT token
	 * @param claimsResolver a function to resolve the desired claim from the Claims object
	 * @return the extracted claim value
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	
	/**
	 * This method extracts all claims value from the JWT token
	 *
	 * @param token the JWT token
	 */
	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	public String getJWT_SECRET() {
		return JWT_SECRET;
	}

	public void setJWT_SECRET(String jWT_SECRET) {
		JWT_SECRET = jWT_SECRET;
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
}
