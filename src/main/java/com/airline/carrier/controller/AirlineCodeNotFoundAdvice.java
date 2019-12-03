
package com.airline.carrier.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class AirlineCodeNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(AirlineCodeNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String airlineCodeNotFoundHandler(AirlineCodeNotFoundException ex) {
    return ex.getMessage();
  }
}