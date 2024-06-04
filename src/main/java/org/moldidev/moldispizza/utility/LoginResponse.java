package org.moldidev.moldispizza.utility;

public record LoginResponse(String username,
                            String jwtToken,
                            Long expiresIn) {
}
