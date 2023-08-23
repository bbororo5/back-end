package com.example.moviewbackend.handler;

import com.example.moviewbackend.dto.CommonResponseDto;
import com.example.moviewbackend.exception.CustomResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<CommonResponseDto> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        CommonResponseDto responseDto = CommonResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CommonResponseDto> handleNullPointerException(NullPointerException exception) {
        CommonResponseDto responseDto = CommonResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponseDto> handleIllegalArgumentException(IllegalArgumentException exception) {
        CommonResponseDto responseDto = CommonResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @ExceptionHandler(CustomResponseException.class)
    public ResponseEntity<CommonResponseDto> handleCustomRequestException(CustomResponseException exception) {
        CommonResponseDto responseDto = CommonResponseDto.builder()
                .status(exception.getStatus())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(builder.toString());
    }
}

