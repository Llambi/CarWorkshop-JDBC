package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.ContractTypeGateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ContractTypeGatewayImpl implements ContractTypeGateway {

    @Override
    public void addContractType(ContractTypeDto contracTypeDto) {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getCurrentConnection();

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
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_DELETE_CONTRACT_TYPE"));
            pst.setString(1, contracTypeDto.name);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(rs, pst, c);
        }
    }

    @Override
    public void updateContractType(ContractTypeDto contracTypeDto) {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_UPDATE_CONTRACT_TYPE"));
            pst.setLong(1, contracTypeDto.compensationDays);
            pst.setString(2, contracTypeDto.name);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(rs, pst, c);
        }
    }

    @Override
    public Map<ContractTypeDto, List<MechanicDto>> findAllContractTypes() {
        return null;
    }
}
