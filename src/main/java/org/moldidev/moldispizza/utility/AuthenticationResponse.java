package org.moldidev.moldispizza.utility;

public record AuthenticationResponse(String accessToken,
                                     String refreshToken,
                                     String rememberMeToken) {
}
