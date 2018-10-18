package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.ContractTypeGateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ContractTypeGatewayImpl implements ContractTypeGateway {

    @Override
    public void addContractType(ContractTypeDto contracTypeDto) {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_ADD_CONTRACT_TYPE"));
            pst.setString(1, contracTypeDto.name);
            pst.setInt(2, contracTypeDto.compensationDays);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(rs, pst, c);
        }
    }

    @Override
    public void deleteContractType(ContractTypeDto contracTypeDto) {

    }

    @Override
    public void updateContractType(ContractTypeDto contracTypeDto) {

    }

    @Override
    public List<ContractTypeDto> findAllContractTypes() {
        return null;
    }
}
