package uo.ri.persistence;

import uo.ri.business.dto.BreakdownDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.persistence.exception.PersistanceException;

import java.util.List;

public interface BreakdownGateway {

    BreakdownDto findBreakdown(long id) throws PersistanceException;
    void updateBreakdown(Long id,String column, String status) throws PersistanceException;
    void updateBreakdown(Long id,String column, Long status) throws PersistanceException;
    void updateBreakdown(Long id,String column, Double status) throws PersistanceException;
    List<BreakdownDto> findUninvoicedBreakdown(Long id) throws PersistanceException;

    List<BreakdownDto> findMechanicBreakDowns(ContractDto mechanic);
}
