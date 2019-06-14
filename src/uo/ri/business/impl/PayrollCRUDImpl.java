package uo.ri.business.impl;

import java.util.List;

import uo.ri.business.PayrollService;
import uo.ri.business.dto.PayrollDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.impl.transactionScript.payroll.DeleteLastGeneratePayrolls;
import uo.ri.business.impl.transactionScript.payroll.DeleteLastPayrollForMechanicId;
import uo.ri.business.impl.transactionScript.payroll.FinAllPayrolls;
import uo.ri.business.impl.transactionScript.payroll.FindPayrollById;
import uo.ri.business.impl.transactionScript.payroll.FindPayrollsbyMechanicId;
import uo.ri.business.impl.transactionScript.payroll.GeneratePayrolls;

public class PayrollCRUDImpl implements PayrollService {
    @Override
    public List<PayrollDto> findAllPayrolls()
            throws BusinessException {
        return new FinAllPayrolls().execute();
    }

    @Override
    public List<PayrollDto> findPayrollsByMechanicId(Long id)
            throws BusinessException {
        return new FindPayrollsbyMechanicId(id).execute();
    }

    @Override
    public PayrollDto findPayrollById(Long id)
            throws BusinessException {
        return new FindPayrollById(id).execute();
    }

    @Override
    public void deleteLastPayrollForMechanicId(Long id)
            throws BusinessException {
        new DeleteLastPayrollForMechanicId(id).excecute();
    }

    @Override
    public int deleteLastGenetaredPayrolls()
            throws BusinessException {
        return new DeleteLastGeneratePayrolls().execute();
    }

    @Override
    public int generatePayrolls() throws BusinessException {
        return new GeneratePayrolls().execute();
    }
}
