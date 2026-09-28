package com.smartcityx.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntime(RuntimeException ex, WebRequest request, Model model) throws RuntimeException {
        if (ex instanceof org.springframework.security.access.AccessDeniedException ||
            ex instanceof org.springframework.security.core.AuthenticationException) {
            throw ex;
        }
        
        ex.printStackTrace();
        String path = request.getDescription(false);
        if (path.contains("/api/")) {
            return buildApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        model.addAttribute("errorTitle", "Something went wrong");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", 500);
        return "error/generic";
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Page Not Found");
        model.addAttribute("errorMessage", "The page you are looking for does not exist.");
        model.addAttribute("errorCode", 404);
        return "error/generic";
    }

    @ExceptionHandler(Exception.class)
    public Object handleGeneral(Exception ex, WebRequest request, Model model) {
        String path = request.getDescription(false);
        if (path.contains("/api/")) {
            return buildApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
        model.addAttribute("errorTitle", "Error");
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
        model.addAttribute("errorCode", 500);
        return "error/generic";
    }

    private ResponseEntity<Map<String, Object>> buildApiError(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
