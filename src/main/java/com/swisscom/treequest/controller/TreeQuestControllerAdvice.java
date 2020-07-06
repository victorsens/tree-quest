package com.swisscom.treequest.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class TreeQuestControllerAdvice {

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<String[]> handleNotFoundException(MethodArgumentNotValidException e) {
    log.error("Exception : ", e);
    return ResponseEntity.status(BAD_REQUEST).body(e.getBindingResult().getAllErrors().stream().map(
        DefaultMessageSourceResolvable::getDefaultMessage).toArray(String[]::new));
  }

  @ExceptionHandler({IllegalStateException.class})
  public ResponseEntity<String> handleNotFoundException(IllegalStateException e) {
    log.error("Exception : ", e);
    return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
  }
}
