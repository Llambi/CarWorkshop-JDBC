package uo.ri.business.impl.transactionScript.mechanic;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.Conf;
import uo.ri.conf.GatewayFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateMechanic {

    private MechanicDto mechanic;

    public UpdateMechanic(MechanicDto mechanic) {
        this.mechanic = mechanic;
    }

    public void execute(){
        // Procesar
        GatewayFactory.getMechanicGateway().updateMechanic(mechanic);
    }
}
