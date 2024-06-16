package org.moldidev.moldispizza.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
public class ObjectNotValidException extends RuntimeException {
    private final Set<String> violations;
}
