package com.onlineadplatform.logic.payload;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResponseValidator {

    public ResponseEntity<Object> validate(BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(result.getAllErrors())) {
                errorMap = result
                        .getAllErrors()
                        .stream()
                        .collect(Collectors.toMap(DefaultMessageSourceResolvable::getCode,
                                DefaultMessageSourceResolvable::getDefaultMessage, (a, b) -> b));
            }
            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

}
