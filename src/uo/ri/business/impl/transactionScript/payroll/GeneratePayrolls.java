package uo.ri.business.impl.transactionScript.payroll;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.PayrollDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.BreakdownGateway;
import uo.ri.persistence.PayrollGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GeneratePayrolls {
    private final PayrollGateway payrollGateway = GatewayFactory.getPayrollGateway();
    private final Date generationDate = Dates.lastDayOfMonth(Dates.subMonths(Dates.today(), 1));
    private final BreakdownGateway breakdownGateway = GatewayFactory.getBreakdownGateway();
    private Connection connection;

    public int execute() throws BusinessException {
        int count = 0;
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);

            List<ContractDto> contracts = getContractsThatGeneratePayrolls();
            List<PayrollDto> payrolls = new LinkedList<>();
            for (ContractDto c : contracts) {
                payrolls.add(generatePayroll(c));
            }
            count = savePayrolls(contracts, payrolls);

            connection.commit();

        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible generar las nominas\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Error en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }

        return count;
    }

    private List<ContractDto> getContractsThatGeneratePayrolls() throws PersistanceException {
        Date getFirstDayPreviousMonth = Dates.firstDayOfMonth(Dates.subMonths(Dates.today(), 1));
        return payrollGateway.getContractsThatGeneratePayrolls(generationDate, getFirstDayPreviousMonth);
    }

    private PayrollDto generatePayroll(ContractDto contrato) throws PersistanceException {
        PayrollDto payroll = new PayrollDto();
        payroll.date = generationDate;
        payroll.baseSalary = contrato.yearBaseSalary
                / 14;
        payroll.extraSalary = calculateExtraSalary(payroll.baseSalary);

        ContractCategoryDto category = GatewayFactory.getContractCategoryGateway().findContractCategoryById(contrato.categoryId);
        payroll.productivity = category.productivityPlus
                * getProductivityPlus(contrato);
        payroll.triennium = category.trieniumSalary
                * getTrieniums(contrato);
        payroll.grossTotal = payroll.baseSalary + payroll.extraSalary
                + payroll.productivity + payroll.triennium;
        payroll.irpf = getIrpf(contrato.yearBaseSalary,
                payroll.grossTotal);
        payroll.socialSecurity = contrato.yearBaseSalary / 12 * 0.05;
        return payroll;
    }

    private double calculateExtraSalary(double baseSalary) {
        if (Calendar.getInstance().get(Calendar.MONTH) + 1 == 6
                || Calendar.getInstance().get(Calendar.MONTH)
                + 1 == 12)
            return baseSalary;
        return 0;
    }

    private double getProductivityPlus(ContractDto contract) throws PersistanceException {
        return breakdownGateway.getTotalAmountOfMechanicBreakdowns(Dates.month(generationDate), contract.mechanicId);
    }

    private int getTrieniums(ContractDto c) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Dates.today());
        int value = calendar.get(Calendar.YEAR);
        calendar.setTime(c.startDate);
        value -= calendar.get(Calendar.YEAR);
        return value / 3;
    }

    private double getIrpf(double baseSalary, double grossTotal) {
        if (baseSalary < 12000)
            return 0.0;
        else if (baseSalary >= 12000 && baseSalary < 30000)
            return grossTotal * 0.1;
        else if (baseSalary >= 30000 && baseSalary < 40000)
            return grossTotal * 0.15;
        else if (baseSalary >= 40000 && baseSalary < 50000)
            return grossTotal * 0.2;
        else if (baseSalary >= 50000 && baseSalary < 60000)
            return grossTotal * 0.3;
        else
            return grossTotal * 0.4;
    }

    private int savePayrolls(List<ContractDto> contracts,
                             List<PayrollDto> payrolls) throws PersistanceException {
        int count = 0;
        for (int i = 0; i < contracts.size(); i++) {
            this.generatePayrolls(payrolls.get(i),
                    contracts.get(i));
            count++;
        }
        return count;
    }

    private void generatePayrolls(PayrollDto payroll, ContractDto contract) throws PersistanceException {
        payrollGateway.generatePayrolls(generationDate, payroll, contract);
    }

}
