package uo.ri.persistence;

import java.util.List;

import uo.ri.business.dto.BreakdownDto;
import uo.ri.persistence.exception.PersistanceException;

public interface BreakdownGateway {

    BreakdownDto findBreakdown(long id) throws PersistanceException;

    void updateBreakdown(Long id, String column, String status) throws PersistanceException;

    void updateBreakdown(Long id, String column, Long status) throws PersistanceException;

    void updateBreakdown(Long id, String column, Double status) throws PersistanceException;

    List<BreakdownDto> findUninvoicedBreakdownByDni(String id) throws PersistanceException;

    List<BreakdownDto> findBreakDownsByMechanicId(long id) throws PersistanceException;

    double getTotalAmountOfMechanicBreakdowns(Integer month, Long mechanicId) throws PersistanceException;
}
