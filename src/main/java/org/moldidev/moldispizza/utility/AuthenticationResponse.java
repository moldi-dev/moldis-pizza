package org.moldidev.moldispizza.utility;

public record AuthenticationResponse(String username,
                                     String jwtToken,
                                     Long expiresIn) {
}
