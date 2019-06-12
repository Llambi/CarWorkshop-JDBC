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

/**
 * Clase que contiene la persistencia de los mecanicos
 */
public class MechanicGatewayImpl implements MechanicGateway {
    /**
     * Metodoque que inserta un mecanico
     *
     * @param mechanic Informacion del mecanico a insertar.
     * @throws PersistanceException
     */
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

    /**
     * Metodo que elimina un mecanico.
     *
     * @param mechanic Que contiene el identifaicador del mecanico a eliminar
     * @throws PersistanceException
     */
    @Override
    public void deleteMechanic(Long mechanic) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            c = Jdbc.getConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_DELETE_MECHANIC"));
            pst.setLong(1, mechanic);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistanceException("Error al eliminar el mecanico:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst, c);
        }
    }

    /**
     * Metodo que actualiza un mecanico.
     *
     * @param mechanic Nueva informacion del mecanico.
     * @throws PersistanceException
     */
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

    /**
     * Metodo que recupera todos los mecanicos.
     *
     * @return Lista con todos los mecanicos.
     * @throws PersistanceException
     */
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

    /**
     * Metodo que recupera todos los mecanicos con un mismo tipo de contrato.
     *
     * @param contractTypeDto Que contiene el nombre del tipo de contrato del que se quieren recuperar los mecanicos.
     * @return Lista de mecanicos de un tipo de contrato.
     * @throws PersistanceException
     */
    @Override
    public List<MechanicDto> findAllMechanicsByContractType(ContractTypeDto contractTypeDto)
            throws PersistanceException {
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

    /**
     * Metodo que recupera un mecanico dado su dni.
     *
     * @param dni Que contiene el mecanico a recuperar.
     * @return Mecanico que se ha encontrado.
     * @throws PersistanceException
     */
    @Override
    public MechanicDto findMechanicByDni(String dni) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        MechanicDto mechanic = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_MECHANIC_BY_DNI"));
            pst.setString(1, dni);

            rs = pst.executeQuery();
            mechanic = new MechanicDto();
            if (rs.next()) {
                mechanic.id = rs.getLong(1);
                mechanic.dni = rs.getString(2);
                mechanic.name = rs.getString(3);
                mechanic.surname = rs.getString(4);
            } else {
                throw new PersistanceException("No existe el mecanico: " + dni);
            }
        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar el mecanico por dni:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return mechanic;
    }

    @Override
    public MechanicDto findMechanicById(long id) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        MechanicDto mechanic = null;

        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_MECHANIC_BY_ID"));
            pst.setLong(1, id);

            rs = pst.executeQuery();
            mechanic = new MechanicDto();
            if (rs.next()) {
                mechanic.id = rs.getLong(1);
                mechanic.dni = rs.getString(2);
                mechanic.name = rs.getString(3);
                mechanic.surname = rs.getString(4);
            } else {
                throw new PersistanceException("No existe el mecanico: " + id);
            }
        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar el mecanico por dni:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
        return mechanic;
    }

    @Override
    public List<MechanicDto> findActiveMechanics() throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        LinkedList<MechanicDto> mechanics = new LinkedList<MechanicDto>();

        try {
            c = Jdbc.getConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_ACTIVE_MECHANICS"));

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
            throw new PersistanceException("Error al recuperar todos los mecanicos activos:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst, c);
        }
        return mechanics;
    }

    @Override
    public int findMechanicContracts(Long id) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int count = 0;
        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_MECHANIC_CONTRACTS"));
            pst.setLong(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            } else {
                throw new PersistanceException("No existe el mecanico: " + id);
            }
            return count;
        } catch (SQLException e) {
            throw new PersistanceException("Error de persistencia:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }

    }

    @Override
    public int findMechanicInterventions(Long id) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int count = 0;
        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_MECHANIC_INTERVENTIONS"));
            pst.setLong(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            } else {
                throw new PersistanceException("No existe el mecanico: " + id);
            }
            return count;
        } catch (SQLException e) {
            throw new PersistanceException("Error de persistencia:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }

    @Override
    public int findMechanicBreakdowns(Long id) throws PersistanceException {
        Connection c = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int count = 0;
        try {
            c = Jdbc.getCurrentConnection();

            pst = c.prepareStatement(Conf.getInstance().getProperty("SQL_FIND_MECHANIC_BREAKDOWNS"));
            pst.setLong(1, id);

            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            } else {
                throw new PersistanceException("No existe el mecanico: " + id);
            }
            return count;
        } catch (SQLException e) {
            throw new PersistanceException("Error de persistencia:\n\t" + e);
        } finally {
            Jdbc.close(rs, pst);
        }
    }
}
