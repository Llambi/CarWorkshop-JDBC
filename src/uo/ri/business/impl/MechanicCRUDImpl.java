package uo.ri.business.impl;

import uo.ri.business.MechanicCRUDService;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.impl.transactionScript.mechanic.AddMechanic;
import uo.ri.business.impl.transactionScript.mechanic.DeleteMechanic;
import uo.ri.business.impl.transactionScript.mechanic.ListMechanics;
import uo.ri.business.impl.transactionScript.mechanic.UpdateMechanic;

import java.util.List;

public class MechanicCRUDImpl implements MechanicCRUDService {
    @Override
    public void addMechanic(MechanicDto mechanic) throws BusinessException {
        new AddMechanic(mechanic).execute();
    }

    @Override
    public void deleteMechanic(MechanicDto mechanic) throws BusinessException {
        new DeleteMechanic(mechanic).execute();
    }

    @Override
    public void updateMechanic(MechanicDto mechanic) throws BusinessException {
        new UpdateMechanic(mechanic).execute();
    }

    @Override
    public List<MechanicDto> findAllMechanics() throws BusinessException {
        return new ListMechanics().execute();
    }
}
