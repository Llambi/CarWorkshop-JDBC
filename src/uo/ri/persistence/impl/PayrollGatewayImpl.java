package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.business.ContractTypeCRUDService;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.PayrollGateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PayrollGatewayImpl implements PayrollGateway {
    @Override
    public Double getTotalBaseSalary(ContractTypeDto contractTypeDto) {
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
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return acumSalary;
    }
}
