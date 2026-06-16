package project.training.com.example.demo.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TransactionFilter extends OncePerRequestFilter {

    @Value("${spring.application.name}")
    private String serviceName;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String transactionId = request.getHeader("X-Transaction-Id");

        if (transactionId == null || transactionId.isBlank()) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Missing X-Transaction-Id header");
            return;
        }

        response.setHeader("X-Transaction-Id", transactionId);
        response.setHeader("X-Service-Name", serviceName);

        filterChain.doFilter(request, response);
    }
}
