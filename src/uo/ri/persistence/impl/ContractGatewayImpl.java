package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.ContractGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static uo.ri.persistence.impl.ContracStatus.ACTIVE;
import static uo.ri.persistence.impl.ContracStatus.FINISHED;

public class ContractGatewayImpl implements ContractGateway {
    @Override
    public List<ContractDto> findContract(ContractTypeDto contractTypeDto) throws PersistanceException {
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
            throw new PersistanceException("Error los contratos de un tipo:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contracts;
    }

    @Override
    public List<ContractDto> findContract(MechanicDto mechanicDto) throws PersistanceException {
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
            throw new PersistanceException("Error al recuperar los contratos de un mecanico:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contracts;
    }

    @Override
    public ContractDto findContract(ContractDto contractDto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ContractDto contract = new ContractDto();

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_CONTRACT_BY_ID"));
            pst.setLong(1, contractDto.id);

            pst.executeUpdate();
            rs = pst.executeQuery();

            while (rs.next()) {
                contract.mechanicId = rs.getLong(1);
                contract.typeId = rs.getLong(2);
                contract.categoryId = rs.getLong(3);
                contract.startDate = rs.getDate(4);
                contract.endDate = rs.getDate(5);
                contract.yearBaseSalary = rs.getDouble(6);
                contract.id = contractDto.id;
            }

        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar un contrato por su identificador:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contract;
    }

    @Override
    public void addContract(MechanicDto mechanicDto, ContractTypeDto contractTypeDto, ContractCategoryDto contractCategoryDto, ContractDto contractDto) throws PersistanceException {
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
            pst.setString(5, ACTIVE.toString());
            pst.setLong(6, mechanicDto.id);
            pst.setLong(7, contractCategoryDto.id);
            pst.setLong(8, contractTypeDto.id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Error al insertar un contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    @Override
    public void terminateContract(ContractDto contractDto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_TERMINATE_CONTRACT"));
            pst.setString(1, FINISHED.toString());
            pst.setDate(2, new java.sql.Date(contractDto.endDate.getTime()));
            pst.setLong(3, contractDto.id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Error al extinguir el contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    @Override
    public void updateContract(ContractDto contractDto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_UPDATE_CONTRACT"));
            pst.setDouble(1, contractDto.compensation);
            pst.setDouble(2, contractDto.yearBaseSalary);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Error al actualizar el contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    @Override
    public void deleteContract(ContractDto contractDto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_DELETE_CONTRACT"));
            pst.setLong(1, contractDto.id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Error al eliminar el contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }
}
