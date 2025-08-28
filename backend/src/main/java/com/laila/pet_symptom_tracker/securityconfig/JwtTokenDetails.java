package com.laila.pet_symptom_tracker.securityconfig;

import java.util.Date;

public record JwtTokenDetails(String email, String[] roles, Date issuedAt, Date expiresAt) {
  public boolean isExpired() {
    return expiresAt.before(new Date());
  }
}
