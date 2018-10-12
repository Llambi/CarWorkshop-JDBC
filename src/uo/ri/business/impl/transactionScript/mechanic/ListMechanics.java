package uo.ri.business.impl.transactionScript.mechanic;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.MechanicDto;
import uo.ri.conf.Conf;
import uo.ri.conf.GatewayFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ListMechanics {

    public List<MechanicDto> execute() {
       return GatewayFactory.getMechanicGateway().findAllMechanics();
    }
}
