package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.Conf;
import uo.ri.persistence.MechanicGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MechanicGatewayImpl implements MechanicGateway {
    @Override
    public void addMechanic(MechanicDto mechanic) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_ADD_MECHANIC"));
            pst.setString(1, mechanic.dni);
            pst.setString(2, mechanic.name);
            pst.setString(3, mechanic.surname);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Error al insertar el mecanico:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst, c);
        }
    }

    @Override
    public void deleteMechanic(MechanicDto mechanic) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_DELETE_MECHANIC"));
            pst.setLong(1, mechanic.id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Error al eliminar el mecanico:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst, c);
        }
    }

    @Override
    public void updateMechanic(MechanicDto mechanic) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_UPDATE_MECHANIC"));
            pst.setString(1, mechanic.name);
            pst.setString(2, mechanic.surname);
            pst.setLong(3, mechanic.id);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Error al actualizar el mecanico:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst, c);
        }
    }

    @Override
    public List<MechanicDto> findAllMechanics() throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        LinkedList<MechanicDto> mechanics = new LinkedList<MechanicDto>();

        try {
            c = Jdbc.getConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_ALL_MECHANICS"));

            rs = pst.executeQuery();
            while (rs.next()) {
                MechanicDto mechanic = new MechanicDto();
                mechanic.id = rs.getLong(1);
                mechanic.dni = rs.getString(2);
                mechanic.name = rs.getString(3);
                mechanic.surname = rs.getString(4);
                mechanics.add(mechanic);
            }
        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar todos los mecanicos:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst, c);
        }
        return mechanics;
    }

    @Override
    public List<MechanicDto> findAllMechanicsByContractType(ContractTypeDto contractTypeDto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        LinkedList<MechanicDto> mechanics = new LinkedList<>();

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_MECHANICS_BY_CONTRACT_TYPE"));
            pst.setString(1, contractTypeDto.name);

            rs = pst.executeQuery();
            while (rs.next()) {
                MechanicDto mechanic = new MechanicDto();
                mechanic.id = rs.getLong(1);
                mechanic.dni = rs.getString(2);
                mechanic.name = rs.getString(3);
                mechanic.surname = rs.getString(4);
                mechanics.add(mechanic);
            }
        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar el mecanico con un tipo de contrato:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return mechanics;
    }

    @Override
    public MechanicDto findMechanic(MechanicDto mechanicDto) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        MechanicDto mechanic = new MechanicDto();

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_MECHANIC_BY_DNI"));
            pst.setString(1, mechanicDto.dni);

            rs = pst.executeQuery();
            if (rs.next()) {
                mechanic.id = rs.getLong(1);
                mechanic.dni = rs.getString(2);
                mechanic.name = rs.getString(3);
                mechanic.surname = rs.getString(4);
            } else {
                throw new PersistanceException("No existe el mecanico: " + mechanicDto.dni);
            }
        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar el mecanico por dni:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return mechanic;
    }
}
