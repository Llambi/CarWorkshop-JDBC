package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
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

/**
 * Clase que contiene la persistencia de los tipos de contrato.
 */
public class ContractTypeGatewayImpl implements ContractTypeGateway {

    /**
     * Metodo que inserta un tipo de contrato.
     *
     * @param contracTypeDto Informacion del nuevo tipo de contrato.
     * @throws PersistanceException
     */
    @Override
    public void addContractType(ContractTypeDto contracTypeDto) throws PersistanceException {
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
            throw new PersistanceException("Error al insertar el tipo de contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    /**
     * Metodo que elimina un tipo de contrato.
     *
     * @param id del tipo de contrato que se quiere eliminar.
     * @throws PersistanceException
     */
    @Override
    public void deleteContractType(long id) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_DELETE_CONTRACT_TYPE_ID"));
            pst.setLong(1, id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Error al eliminar el tipo de contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    /**
     * Metodo que actualiza un tipo de contrato.
     *
     * @param contracTypeDto Nueva informacion para el tipo de contrato.
     * @throws PersistanceException
     */
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

    /**
     * Metodo que recupera todos los tipos de contrato.
     *
     * @return Lista de los tipos de contrato.
     * @throws PersistanceException
     */
    @Override
    public List<ContractTypeDto> findAllContractTypes() throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<ContractTypeDto> contractTypes = new LinkedList<>();
        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_CONTRACT_TYPES"));

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

    /**
     * Metodo que recupera un tipo de contrato dado su nombre.
     *
     * @param name Que contiene el nombre del tipo de contrato que se quiere recuperar.
     * @return Tipo de contrato que se ha encontrado.
     * @throws PersistanceException
     */
    @Override
    public ContractTypeDto findContractTypeByName(String name) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ContractTypeDto contractType = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_CONTRACT_TYPE_BY_NAME"));
            pst.setString(1, name);

            rs = pst.executeQuery();
            if (rs.next()) {
                contractType = new ContractTypeDto();
                contractType.id = rs.getLong(1);
                contractType.name = rs.getString(2);
                contractType.compensationDays = rs.getInt(3);
            }
        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar el tipo de contrato por nombre:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contractType;
    }

    /**
     * Metodo que recupera el tipo de contrato de un contrato dado
     *
     * @param id Contrato que contiene el identificador del tipo de contrato a recuperar.
     * @return Tipo de contrato que se ha encontrado.
     * @throws PersistanceException
     */
    @Override
    public ContractTypeDto findContractTypeById(long id) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ContractTypeDto contractType = new ContractTypeDto();

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_CONTRACT_TYPE_BY_ID"));
            pst.setLong(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                contractType.id = rs.getLong(1);
                contractType.name = rs.getString(2);
                contractType.compensationDays = rs.getInt(3);
            } else {
                throw new PersistanceException("No existe el tipo de contrato por identificador: "
                        + id);
            }
        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar el tipo de contrato por identificador:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contractType;
    }
}
