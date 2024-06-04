package org.moldidev.moldispizza.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private Integer errorCode;
    private Date createdAt;
    private String errorMessage;
    private String errorDescription;
}
