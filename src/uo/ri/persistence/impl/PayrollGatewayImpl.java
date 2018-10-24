package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.PayrollGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PayrollGatewayImpl implements PayrollGateway {
    @Override
    public Double getTotalBaseSalary(ContractTypeDto contractTypeDto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Double acumSalary = -1d;
        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_GET_ACUMSALARI_BY_CONTRACT_TYPE"));
            pst.setString(1, contractTypeDto.name);
            pst.executeUpdate();
            rs = pst.executeQuery();

            if (rs.next()) {
                acumSalary = rs.getDouble(1);
            } else {
                throw new PersistanceException("No existe un acumulado de salario por tipo de contrato con nombre: " + contractTypeDto.name);
            }

        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar el acumulado de salario de un tipo de contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return acumSalary;
    }

    @Override
    public int countPayRolls(ContractDto contractDto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int payrolls = 0;
        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_COUNT_PAYROLLS_BY_CONTRACT_ID"));
            pst.setLong(1, contractDto.id);
            pst.executeUpdate();
            rs = pst.executeQuery();

            if (rs.next()) {
                payrolls = rs.getInt(1);
            } else {
                throw new PersistanceException("No existen nominas para el contrato con identificador: " + contractDto.id);
            }

        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar las nominas de un contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return payrolls;
    }
}
