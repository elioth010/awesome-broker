package com.elioth.awesome.message.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Users are not allowed to send messages to themselves")
public class NoCircularMessageAllowedException extends RuntimeException{
}
