package com.dapcomputer.inventariosapi.infraestructura.servicios;

public class SoftDeletePolicy {
    private final UbicacionChildPolicy ubicacionPolicy;
    private final Long defaultUbicacionId;
    private final boolean cascadeUsersOnRole;

    private SoftDeletePolicy(Builder builder) {
        this.ubicacionPolicy = builder.ubicacionPolicy;
        this.defaultUbicacionId = builder.defaultUbicacionId;
        this.cascadeUsersOnRole = builder.cascadeUsersOnRole;
    }

    public UbicacionChildPolicy getUbicacionPolicy() {
        return ubicacionPolicy;
    }

    public Long getDefaultUbicacionId() {
        return defaultUbicacionId;
    }

    public boolean isCascadeUsersOnRole() {
        return cascadeUsersOnRole;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UbicacionChildPolicy ubicacionPolicy = UbicacionChildPolicy.INACTIVATE_CHILDREN;
        private Long defaultUbicacionId;
        private boolean cascadeUsersOnRole = false;

        public Builder ubicacionPolicy(UbicacionChildPolicy policy) {
            this.ubicacionPolicy = policy;
            return this;
        }

        public Builder defaultUbicacionId(Long id) {
            this.defaultUbicacionId = id;
            return this;
        }

        public Builder cascadeUsersOnRole(boolean cascade) {
            this.cascadeUsersOnRole = cascade;
            return this;
        }

        public SoftDeletePolicy build() {
            return new SoftDeletePolicy(this);
        }
    }
}
