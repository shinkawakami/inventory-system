package com.example.inventory.controller.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.inventory.exception.BusinessException;
import com.example.inventory.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error/not_found";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, Model model) {
        model.addAttribute("errorMessage", "システムエラーが発生しました。");
        return "error/system_error";
    }
}