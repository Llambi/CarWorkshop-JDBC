package uo.ri.persistence;

import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.PayrollDto;
import uo.ri.persistence.exception.PersistanceException;

import java.util.Date;
import java.util.List;

public interface PayrollGateway {
    Double getTotalBaseSalary(ContractTypeDto contractTypeDto) throws PersistanceException;

    int countPayRolls(ContractDto contractDto) throws PersistanceException;

    List<PayrollDto> findAllPayrolls() throws PersistanceException;

    List<PayrollDto> findPayrollsByMechanicId(Long id) throws PersistanceException;

    PayrollDto findPayrollsById(Long id) throws PersistanceException;

    void deletePayrollById(Long id) throws PersistanceException;

    int deletePayrollByDate(Date newestDate) throws PersistanceException;

    List<ContractDto> getContractsThatGeneratePayrolls(Date generationDate, Date getFirstDayPreviousMonth) throws PersistanceException;

    void generatePayrolls(Date generationDate, PayrollDto payroll, ContractDto contract) throws PersistanceException;
}
