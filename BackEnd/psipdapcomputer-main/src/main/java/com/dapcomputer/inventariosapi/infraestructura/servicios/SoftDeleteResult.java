package com.dapcomputer.inventariosapi.infraestructura.servicios;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SoftDeleteResult {
    private final String entity;
    private final Object id;
    private final SoftDeleteMode mode;
    private final boolean executed;
    private final Map<String, Long> counts;
    private final Instant at;
    private final String reason;
    private final String deletedBy;

    private SoftDeleteResult(Builder builder) {
        this.entity = builder.entity;
        this.id = builder.id;
        this.mode = builder.mode;
        this.executed = builder.executed;
        this.counts = Collections.unmodifiableMap(new LinkedHashMap<>(builder.counts));
        this.at = builder.at;
        this.reason = builder.reason;
        this.deletedBy = builder.deletedBy;
    }

    public String getEntity() {
        return entity;
    }

    public Object getId() {
        return id;
    }

    public SoftDeleteMode getMode() {
        return mode;
    }

    public boolean isExecuted() {
        return executed;
    }

    public Map<String, Long> getCounts() {
        return counts;
    }

    public Instant getAt() {
        return at;
    }

    public String getReason() {
        return reason;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String entity;
        private Object id;
        private SoftDeleteMode mode = SoftDeleteMode.DRY_RUN;
        private boolean executed;
        private Map<String, Long> counts = new LinkedHashMap<>();
        private Instant at = Instant.now();
        private String reason;
        private String deletedBy;

        public Builder entity(String entity) {
            this.entity = entity;
            return this;
        }

        public Builder id(Object id) {
            this.id = id;
            return this;
        }

        public Builder mode(SoftDeleteMode mode) {
            this.mode = mode;
            return this;
        }

        public Builder executed(boolean executed) {
            this.executed = executed;
            return this;
        }

        public Builder addCount(String table, long count) {
            this.counts.merge(table, count, Long::sum);
            return this;
        }

        public Map<String, Long> getCounts() {
            return counts;
        }

        public Builder at(Instant at) {
            this.at = at;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder deletedBy(String deletedBy) {
            this.deletedBy = deletedBy;
            return this;
        }

        public SoftDeleteResult build() {
            return new SoftDeleteResult(this);
        }
    }
}
