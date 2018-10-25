package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.ContractTypeGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ContractTypeGatewayImpl implements ContractTypeGateway {

    @Override
    public void addContractType(ContractTypeDto contracTypeDto) throws PersistanceException {
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
            throw new PersistanceException("Error al insertar el tipo de contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst, c);
        }
    }

    @Override
    public void deleteContractType(ContractTypeDto contracTypeDto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_DELETE_CONTRACT_TYPE"));
            pst.setString(1, contracTypeDto.name);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Error al eliminar el tipo de contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    @Override
    public void updateContractType(ContractTypeDto contracTypeDto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_UPDATE_CONTRACT_TYPE"));
            pst.setLong(1, contracTypeDto.compensationDays);
            pst.setString(2, contracTypeDto.name);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Error al actualizar el tipo de contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst, c);
        }
    }

    @Override
    public List<ContractTypeDto> findAllContractTypes() throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<ContractTypeDto> contractTypes = new LinkedList<>();
        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_CONTRACT_TYPE"));

            rs = pst.executeQuery();
            while (rs.next()) {
                ContractTypeDto contractTypeDto = new ContractTypeDto();
                contractTypeDto.id = rs.getLong(1);
                contractTypeDto.name = rs.getString(2);
                contractTypeDto.compensationDays = rs.getInt(3);

                contractTypes.add(contractTypeDto);
            }

        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar los tipos de contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contractTypes;
    }

    @Override
    public ContractTypeDto findContractType(ContractTypeDto contractTypeDto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ContractTypeDto contractType = new ContractTypeDto();

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_CONTRACT_TYPE_BY_NAME"));
            pst.setString(1, contractTypeDto.name);

            rs = pst.executeQuery();
            if (rs.next()) {
                contractType.id = rs.getLong(1);
                contractType.name = rs.getString(2);
                contractType.compensationDays = rs.getInt(3);
            } else {
                throw new PersistanceException("No existe el tipo de contrato con nombre: " + contractTypeDto.name);
            }
        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar el tipo de contrato por nombre:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contractType;
    }

    @Override
    public ContractTypeDto findContractType(ContractDto contractDto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ContractTypeDto contractType = new ContractTypeDto();

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_CONTRACT_TYPE_BY_ID"));
            pst.setLong(1, contractDto.typeId);

            rs = pst.executeQuery();
            if (rs.next()) {
                contractType.id = rs.getLong(1);
                contractType.name = rs.getString(2);
                contractType.compensationDays = rs.getInt(3);
            } else {
                throw new PersistanceException("No existe el tipo de contrato por identificador: " + contractDto.typeId);
            }
        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar el tipo de contrato por identificador:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contractType;
    }
}
