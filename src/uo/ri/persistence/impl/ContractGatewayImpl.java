package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.ContractGateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ContractGatewayImpl implements ContractGateway {
    @Override
    public List<ContractDto> findContract(ContractTypeDto contractTypeDto) {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<ContractDto> contracts = new LinkedList<>();

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_CONTRACT_TYPE_CONTRACT"));
            pst.setString(1, contractTypeDto.name);

            pst.executeUpdate();
            rs = pst.executeQuery();

            while (rs.next()) {
                ContractDto contract = new ContractDto();
                contract.mechanicId = rs.getLong(1);
                contract.typeId = rs.getLong(2);
                contract.categoryId = rs.getLong(3);
                contract.startDate = rs.getDate(4);
                contract.endDate = rs.getDate(5);
                contract.yearBaseSalary = rs.getDouble(6);
                contracts.add(contract);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contracts;
    }

    @Override
    public List<ContractDto> findContract(MechanicDto mechanicDto) {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<ContractDto> contracts = new LinkedList<>();

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_CONTRACT_BY_MECHANIC"));
            pst.setString(1, mechanicDto.dni);

            pst.executeUpdate();
            rs = pst.executeQuery();

            while (rs.next()) {
                ContractDto contract = new ContractDto();
                contract.mechanicId = rs.getLong(1);
                contract.typeId = rs.getLong(2);
                contract.categoryId = rs.getLong(3);
                contract.startDate = rs.getDate(4);
                contract.endDate = rs.getDate(5);
                contract.yearBaseSalary = rs.getDouble(6);
                contracts.add(contract);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contracts;
    }

    @Override
    public void addContract(MechanicDto mechanicDto, ContractTypeDto contractTypeDto, ContractCategoryDto contractCategoryDto, ContractDto contractDto) {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_ADD_CONTRACT"));
            pst.setDate(1, new java.sql.Date(contractDto.startDate.getTime()));
            pst.setDate(2, new java.sql.Date(contractDto.endDate.getTime()));
            pst.setDouble(3, contractDto.yearBaseSalary);
            pst.setDouble(4, contractDto.compensation);
            pst.setString(5, Contracstatus.ACTIVE.toString());
            pst.setLong(6, mechanicDto.id);
            pst.setLong(7, contractCategoryDto.id);
            pst.setLong(8, contractTypeDto.id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    @Override
    public void updateContract(ContractDto contractDto) {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_UPDATE_CONTRACT"));
            pst.setString(1, Contracstatus.FINISHED.toString());
            pst.setLong(2, contractDto.id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }
}
