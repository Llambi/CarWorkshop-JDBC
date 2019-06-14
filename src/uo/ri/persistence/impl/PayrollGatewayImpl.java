package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.PayrollDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.PayrollGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que contiene la persistencia de nominas.
 */
public class PayrollGatewayImpl implements PayrollGateway {
    /**
     * Metodo que recupera el total de salario base dado un tipo
     * de contrato.
     *
     * @param contractTypeDto Que contiene el nombre del tipo de
     *                        contrato del que se quieren sumar
     *                        los salarios base.
     * @return Double con el acumulado de salarios base.
     * @throws PersistanceException
     */
    @Override
    public Double getTotalBaseSalary(ContractTypeDto contractTypeDto)
            throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Double acumSalary = -1d;
        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance()
                    .getProperty("SQL_GET_ACUMSALARI_BY_CONTRACT_TYPE"));
            pst.setString(1, contractTypeDto.name);
            rs = pst.executeQuery();

            if (rs.next()) {
                acumSalary = rs.getDouble(1);
            } else {
                throw new PersistanceException
                        ("No existe un acumulado de salario por tipo" +
                                " de contrato con nombre: "
                        + contractTypeDto.name);
            }

        } catch (SQLException e) {
            throw new PersistanceException
                    ("Error al recuperar el acumulado de salario de" +
                            " un tipo de contrato:\n\t"
                    + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return acumSalary;
    }

    /**
     * Metodo que recupera el numero de nominas emitidas a un contrato
     *
     * @param contractDto Que contiene ep identificador del contrato de las nominas.
     * @return Numero de nominas de un contrato.
     * @throws PersistanceException
     */
    @Override
    public int countPayRolls(ContractDto contractDto)
            throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int payrolls = 0;
        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance()
                    .getProperty("SQL_COUNT_PAYROLLS_BY_CONTRACT_ID"));
            pst.setLong(1, contractDto.id);
            rs = pst.executeQuery();

            if (rs.next()) {
                payrolls = rs.getInt(1);
            } else {
                throw new PersistanceException
                        ("No existen nominas para el contrato con identificador: "
                        + contractDto.id);
            }

        } catch (SQLException e) {
            throw new PersistanceException
                    ("Error al recuperar las nominas de un" +
                            " contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return payrolls;
    }

    @Override
    public List<PayrollDto> findAllPayrolls() throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        List<PayrollDto> payrollsList = null;
        try {
            c = Jdbc.getCurrentConnection();
            payrollsList = new LinkedList<>();
            pst = c.prepareStatement(Conf.getInstance()
                    .getProperty("SQL_FIND_ALL_PAYROLL"));
            rs = pst.executeQuery();
            while (rs.next()) {
                payrollsList.add(getPayrollData(rs));
            }
        } catch (SQLException e) {
            throw new PersistanceException("Fallo de persistencia");
        } finally {
            Jdbc.close(rs, pst);
        }
        return payrollsList;
    }

    @Override
    public List<PayrollDto> findPayrollsByMechanicId(Long id)
            throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        List<PayrollDto> payrollsList = null;
        try {
            c = Jdbc.getCurrentConnection();
            payrollsList = new LinkedList<>();
            pst = c.prepareStatement(Conf.getInstance()
                    .getProperty("SQL_GET_PAYROLLS_MECHANIC"));
            pst.setLong(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                payrollsList.add(getPayrollData(rs));
            }
        } catch (SQLException e) {
            throw new PersistanceException("Fallo de persistencia");
        } finally {
            Jdbc.close(rs, pst);
        }
        return payrollsList;
    }

    @Override
    public PayrollDto findPayrollsById(Long id)
            throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PayrollDto payrollDto = null;
        try {
            c = Jdbc.getCurrentConnection();
            pst = c.prepareStatement(Conf.getInstance()
                    .getProperty("SQL_GET_PAYROLL_ID"));
            pst.setLong(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                payrollDto = getPayrollData(rs);
            }
        } catch (SQLException e) {
            throw new PersistanceException("Fallo de persistencia");
        } finally {
            Jdbc.close(rs, pst);
        }
        return payrollDto;
    }

    @Override
    public void deletePayrollById(Long id) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance()
                    .getProperty("SQL_DELETE_LAST_PAYROLL_MACHANIC"));
            pst.setLong(1, id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException
                    ("Error al eliminar la nomina:\n\t" + e.getMessage());
        } finally {
            Jdbc.close(pst);
        }
    }

    @Override
    public int deletePayrollByDate(Date newestDate)
            throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        int count = 0;
        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance()
                    .getProperty("SQL_DELETE_LAST_GENERATED_PAYROLLS"));
            pst.setDate(1, new java.sql.Date(newestDate.getTime()));

            count = pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException
                    ("Error al eliminar las nominas generadas:\n\t"
                            + e.getMessage());
        } finally {
            Jdbc.close(pst);
        }
        return count;
    }

    @Override
    public List<ContractDto> getContractsThatGeneratePayrolls
            (Date date1, Date date2) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<ContractDto> contractsList = null;
        try {
            c = Jdbc.getCurrentConnection();
            contractsList = new LinkedList<>();
            ContractDto contractDto = null;
            pst = c.prepareStatement(Conf.getInstance()
                    .getProperty("SQL_GET_CONTRACTS_PAYROLLS"));
            pst.setDate(1, new java.sql.Date(date1.getTime()));
            pst.setDate(2, new java.sql.Date(date1.getTime()));
            pst.setDate(3, new java.sql.Date(date2.getTime()));
            rs = pst.executeQuery();

            while (rs.next()) {
                contractDto = new ContractDto();
                contractDto.id = rs.getLong("id");
                contractDto.yearBaseSalary = rs.getDouble(
                        "salariobase");
                contractDto.categoryId = rs.getLong(
                        "categoriacontrato_id");
                contractDto.mechanicId = rs.getLong("mecanico_id");
                contractDto.startDate = rs.getDate("startdate");
                contractDto.endDate = rs.getDate("enddate");
                contractsList.add(contractDto);
            }
        } catch (SQLException e) {
            throw new PersistanceException("Fallo de persistencia");
        } finally {
            Jdbc.close(rs, pst);
        }
        return contractsList;
    }

    @Override
    public void generatePayrolls(Date date, PayrollDto payroll,
                                 ContractDto contract)
            throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            c = Jdbc.getCurrentConnection();
            pst = c.prepareStatement(Conf.getInstance()
                    .getProperty("SQL_GENERAR_PAYROLLS"));
            pst.setDate(1, new java.sql.Date(date.getTime()));
            pst.setDouble(2, payroll.baseSalary);
            pst.setDouble(3, payroll.extraSalary);
            pst.setDouble(4, payroll.productivity);
            pst.setDouble(5, payroll.triennium);
            pst.setDouble(6, payroll.irpf);
            pst.setDouble(7, payroll.socialSecurity);
            pst.setLong(8, contract.id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistanceException("Fallo de persistencia");
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    public PayrollDto getPayrollData(ResultSet rs)
            throws PersistanceException {
        PayrollDto p = null;
        try {
            p = new PayrollDto();
            p.id = rs.getLong(1);
            p.date = rs.getDate(2);
            p.baseSalary = rs.getDouble(3);
            p.extraSalary = rs.getDouble(4);
            p.productivity = rs.getDouble(5);
            p.triennium = rs.getDouble(6);
            p.irpf = rs.getDouble(7);
            p.socialSecurity = rs.getDouble(8);

            p.grossTotal = p.baseSalary + p.extraSalary
                    + p.productivity + p.triennium;
            p.discountTotal = p.irpf + p.socialSecurity;
            p.netTotal = p.grossTotal - p.discountTotal;
        } catch (SQLException e) {
            throw new PersistanceException("Error en creacion de payroll");
        }
        return p;
    }
}
