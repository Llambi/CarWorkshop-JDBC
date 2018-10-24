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

public class ContractCategoryGatewayImpl implements ContractCategoryGateway {
    @Override
    public ContractCategoryDto findContractCategory(ContractCategoryDto contractCategoryDto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ContractCategoryDto contractCategory = new ContractCategoryDto();

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_CONTRACT_CATEGORY_BY_NAME"));
            pst.setString(1, contractCategory.name);

            rs = pst.executeQuery();
            if (rs.next()) {
                contractCategory.id = rs.getLong(1);
                contractCategory.name = rs.getString(2);
                contractCategory.productivityPlus = rs.getDouble(3);
                contractCategory.trieniumSalary = rs.getDouble(4);
            } else {
                throw new PersistanceException("No existe el tipo de contrato: " + contractCategoryDto.name);
            }
        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar la categoria de contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return contractCategory;
    }
}
