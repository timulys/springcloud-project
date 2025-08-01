package com.example.common.exception;

import com.example.common.advice.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * GlobalExceptionHandler 자동 등록
 */
@AutoConfiguration
@Import(GlobalExceptionHandler.class)
public class CommonExceptionAutoConfig {}