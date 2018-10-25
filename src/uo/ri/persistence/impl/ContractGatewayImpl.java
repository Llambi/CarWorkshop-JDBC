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

/**
 * Clase que contiene la persistencia de los contratos.
 */
public class ContractGatewayImpl implements ContractGateway {
    /**
     * Metodo que recupera uno o mas contratos mediante su tipo.
     *
     * @param contractTypeDto Categoria de contrato cuyo nombre se quiere usar para encontrar un contrato.
     * @return Lista de contratos con la categoria dada.
     * @throws PersistanceException
     */
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

            rs = pst.executeQuery();

            while (rs.next()) {
                ContractDto contract = new ContractDto();
                contract.id = rs.getLong(1);
                contract.startDate = rs.getDate(2);
                contract.endDate = rs.getDate(3);
                contract.yearBaseSalary = rs.getDouble(4);
                contract.compensation = rs.getDouble(5);
                contract.status = rs.getString(6);
                contract.categoryId = rs.getLong(7);
                contract.typeId = rs.getLong(8);
                contract.mechanicId = rs.getLong(9);


                contracts.add(contract);
            }

        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar los contratos de un tipo:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contracts;
    }

    /**
     * Metodo qu erecupera uno o mas contratos de un mecanico dado.
     *
     * @param mechanicDto Mecanico cuyo dni se quiere usar para encontrar los contratos.
     * @return Lista de contratos del mecanico dado.
     * @throws PersistanceException
     */
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

            rs = pst.executeQuery();

            while (rs.next()) {
                ContractDto contract = new ContractDto();
                contract.id = rs.getLong(1);
                contract.startDate = rs.getDate(2);
                contract.endDate = rs.getDate(3);
                contract.yearBaseSalary = rs.getDouble(4);
                contract.compensation = rs.getDouble(5);
                contract.status = rs.getString(6);
                contract.categoryId = rs.getLong(7);
                contract.typeId = rs.getLong(8);
                contract.mechanicId = rs.getLong(9);
                contracts.add(contract);
            }

        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar los contratos de un mecanico:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contracts;
    }

    /**
     * Metodo que recupera un contrato dado su identificador.
     *
     * @param contractDto Que contiene el identificador a buscar.
     * @return Contrato encontrado.
     * @throws PersistanceException
     */
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

            rs = pst.executeQuery();

            while (rs.next()) {
                contract.id = rs.getLong(1);
                contract.startDate = rs.getDate(2);
                contract.endDate = rs.getDate(3);
                contract.yearBaseSalary = rs.getDouble(4);
                contract.compensation = rs.getDouble(5);
                contract.status = rs.getString(6);
                contract.categoryId = rs.getLong(7);
                contract.typeId = rs.getLong(8);
                contract.mechanicId = rs.getLong(9);
            }

        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar un contrato por su identificador:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contract;
    }

    /**
     * Metodo que inserta un nuevo contrato.
     *
     * @param mechanicDto         Mecanico del contrato.
     * @param contractTypeDto     Tipo del contrato.
     * @param contractCategoryDto Categoria del contrato.
     * @param contractDto         Informacion del nuecvo contrato.
     * @throws PersistanceException
     */
    @Override
    public void addContract(MechanicDto mechanicDto, ContractTypeDto contractTypeDto
            , ContractCategoryDto contractCategoryDto, ContractDto contractDto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_ADD_CONTRACT"));
            pst.setDate(1, new java.sql.Date(contractDto.startDate.getTime()));
            pst.setDate(2, contractDto.endDate == null
                    ? null : new java.sql.Date(contractDto.endDate.getTime()));
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

    /**
     * Metodo que finaliza un contrato
     *
     * @param contractDto Informacion de la finalizacion del contrato.
     * @throws PersistanceException
     */
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

    /**
     * Metodo para actualizar un contrato.
     *
     * @param contractDto Nueca informacion del contrato.
     * @throws PersistanceException
     */
    @Override
    public void updateContract(ContractDto contractDto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_UPDATE_CONTRACT"));
            pst.setDouble(1, contractDto.yearBaseSalary);
            pst.setDate(2, new java.sql.Date(contractDto.endDate.getTime()));
            pst.setLong(3, contractDto.id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Error al actualizar el contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    /**
     * Metodo que elimina un contrato.
     *
     * @param contractDto Que contiene el identificador del contrato que se quiere eliminar.
     * @throws PersistanceException
     */
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
