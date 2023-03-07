package org.petproject.sensors.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class URLRewriteFilter implements Filter {
  private final String API_PATTERN = "^\\/api\\/(.+)$";
  private final String POINT_EXCLUSION_PATTERN = "^([^.]+)$";

  @Override
  public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
    final HttpServletRequest servletRequest = (HttpServletRequest) request;
    final String requestURI = servletRequest.getRequestURI();
    final String contextPath = servletRequest.getContextPath();
    if(!requestURI.equals(contextPath) &&
            !requestURI.matches(API_PATTERN) && // Check if the requested URL is not a controller (/api/**)
            requestURI.matches(POINT_EXCLUSION_PATTERN) // Check if there are no "." in requested URL
    ) {
      final RequestDispatcher dispatcher = request.getRequestDispatcher("/");
      dispatcher.forward(request, response);
      return;
    }
    chain.doFilter(request, response);
  }
}