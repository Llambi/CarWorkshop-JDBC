package uo.ri.persistence;

import uo.ri.persistence.exception.PersistanceException;

public interface InterventionGateway {
    Double getManPowerTotalImport(Long idBreakdown) throws PersistanceException;
}
