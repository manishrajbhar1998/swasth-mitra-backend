package com.swasthyamitra.healthportal.enums;

public enum RoleEnum {
    SUPER_ADMIN,       // Full access across system
    STATE_ADMIN,       // State-level access
    DISTRICT_ADMIN,    // District-level access
    DISTRIBUTOR_ADMIN, // Block-level access
    EMPLOYEE,         // General employee role with limited or custom access
    USER
}
