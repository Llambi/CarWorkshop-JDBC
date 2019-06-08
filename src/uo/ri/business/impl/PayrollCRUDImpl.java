package uo.ri.business.impl;

import uo.ri.business.PayrollService;
import uo.ri.business.dto.PayrollDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.impl.transactionScript.payroll.*;

import java.util.List;

public class PayrollCRUDImpl implements PayrollService {
    @Override
    public List<PayrollDto> findAllPayrolls() throws BusinessException {
        return new FinAllPayrolls().execute();
    }

    @Override
    public List<PayrollDto> findPayrollsByMechanicId(Long id) throws BusinessException {
        return new FindPayrollsbyMechanicId(id).execute();
    }

    @Override
    public PayrollDto findPayrollById(Long id) throws BusinessException {
        return new FindPayrollById(id).execute();
    }

    @Override
    public void deleteLastPayrollForMechanicId(Long id) throws BusinessException {
        new DeleteLastPayrollForMechanicId(id).excecute();
    }

    @Override
    public int deleteLastGenetaredPayrolls() throws BusinessException {
        return new DeleteLastGeneratePayrolls().execute();
    }

    @Override
    public int generatePayrolls() throws BusinessException {
        return new GeneratePayrolls().execute();
    }
}
