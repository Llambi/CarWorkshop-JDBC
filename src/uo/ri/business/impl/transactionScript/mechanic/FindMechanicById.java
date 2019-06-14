package uo.ri.business.impl.transactionScript.mechanic;

import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

public class FindMechanicById {
    private Long id;

    public FindMechanicById(Long id) {
        this.id = id;
    }

    public MechanicDto execute() throws BusinessException {
        try {
            return GatewayFactory.getMechanicGateway()
                    .findMechanicById(id);
        } catch (PersistanceException e) {
            throw new BusinessException
                    ("Imposible recuperar el mecanico.\n\t" + e);
        }
    }
}
