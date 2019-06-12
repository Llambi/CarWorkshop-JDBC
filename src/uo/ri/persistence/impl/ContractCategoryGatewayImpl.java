package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.ContractCategoryGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que contiene la logica de las categorias de contratos.
 */
public class ContractCategoryGatewayImpl implements ContractCategoryGateway {


    @Override
    public ContractCategoryDto findContractCategoryById(long id) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ContractCategoryDto contractCategory = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_CONTRACT_CATEGORY_BY_ID"));
            pst.setLong(1, id);

            rs = pst.executeQuery();
            contractCategory = new ContractCategoryDto();
            if (rs.next()) {
                contractCategory.id = rs.getLong(1);
                contractCategory.name = rs.getString(2);
                contractCategory.productivityPlus = rs.getDouble(3);
                contractCategory.trieniumSalary = rs.getDouble(4);
            } else {
                throw new PersistanceException("No existe el tipo de contrato: " + id);
            }
        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar la categoria de contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contractCategory;
    }

    /**
     * Metodo que recupera una categoria de contrato por su nombre.
     *
     * @param name Categoria de contrato que contiene el nombre que se queiere buscar.
     * @return Categoria de contrato que se ha obtenido.
     * @throws PersistanceException
     */
    @Override
    public ContractCategoryDto findContractCategoryByName(String name) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ContractCategoryDto contractCategory = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_CONTRACT_CATEGORY_BY_NAME"));
            pst.setString(1, name);

            rs = pst.executeQuery();
            contractCategory = new ContractCategoryDto();
            if (rs.next()) {
                contractCategory.id = rs.getLong(1);
                contractCategory.name = rs.getString(2);
                contractCategory.productivityPlus = rs.getDouble(3);
                contractCategory.trieniumSalary = rs.getDouble(4);
            } else {
                throw new PersistanceException("No existe el tipo de contrato: " + name);
            }
        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar la categoria de contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contractCategory;
    }

    @Override
    public void addContractCategory(ContractCategoryDto dto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_ADD_CONTRACT_CATEGORY"));
            pst.setString(1, dto.name);
            pst.setDouble(2, dto.productivityPlus);
            pst.setDouble(3, dto.trieniumSalary);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Error al insertar una categoria de contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    @Override
    public void deleteContractCategory(Long id) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_DELETE_CONTRACT_CATEGORY"));
            pst.setLong(1, id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Error al eliminar la categoria de contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    @Override
    public void updateContractCategori(ContractCategoryDto dto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_UPDATE_CONTRACT_CATEGORY"));
            pst.setDouble(1, dto.productivityPlus);
            pst.setDouble(2, dto.trieniumSalary);
            pst.setLong(3, dto.id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Error al actualizar la categoria de contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst, c);
        }
    }

    @Override
    public List<ContractCategoryDto> findAllContractCategories() throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<ContractCategoryDto> contractCategories = new LinkedList<>();
        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_CONTRACT_CATEGORIES"));

            rs = pst.executeQuery();
            while (rs.next()) {
                ContractCategoryDto contractCategory = new ContractCategoryDto();
                contractCategory.id = rs.getLong(1);
                contractCategory.name = rs.getString(2);
                contractCategory.productivityPlus = rs.getDouble(3);
                contractCategory.trieniumSalary = rs.getDouble(4);

                contractCategories.add(contractCategory);
            }

        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar los tipos de contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contractCategories;
    }
}
