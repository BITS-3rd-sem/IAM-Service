package com.docappointment.iam.config;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint{

	
	/**
	 * The commence() method is called by Spring Security when an AuthenticationException occurs during the authentication process. 
	 * It is responsible for setting the response status, content type, and error message to be sent back to the client.
	 * Note: This class assumes that the authentication exception is caused by an invalid or missing authorization header. 
	 * The response is set to have an "application/json" content type, an "Unauthorized" status (HTTP 401), and a JSON body containing an error message
	 * indicating the cause of the authentication failure.
	 * @param request, the HttpServletRequest object representing the incoming request
	 * 
	 * @param response, the HttpServletResponse object representing the outgoing response
	 * 
	 * @param Authentication Exception
	 * 
	 * @throws ServletException if any servlet-related exception occurs during the processing of the request
	 * 
	 * @throws IOException if an I/O error occurs during the processing of the request
	 */
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException, java.io.IOException {
    
    	System.out.println(response.getStatus());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println("{ \"status\": \"error\", \"message\": \"Invalid or missing authorization header\" }");

    }
}