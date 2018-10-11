package uo.ri.persistence;

import uo.ri.persistence.exception.PersistanceException;

public interface SpareGateway {
    Double getSpareTotalImport(Long idBreakdown) throws PersistanceException;
}
