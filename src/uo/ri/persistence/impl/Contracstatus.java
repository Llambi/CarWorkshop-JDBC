package uo.ri.persistence.impl;

public enum ContracStatus {
    FINISHED {
        public String toString() {
            return "FINISHED";
        }
    },
    ACTIVE {
        public String toString() {
            return "ACTIVE";
        }
    }

}
