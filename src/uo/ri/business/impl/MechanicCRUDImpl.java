package uo.ri.business.impl;

import java.util.List;

import uo.ri.business.MechanicCrudService;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.impl.transactionScript.mechanic.AddMechanic;
import uo.ri.business.impl.transactionScript.mechanic.DeleteMechanic;
import uo.ri.business.impl.transactionScript.mechanic.FindActiveMechanics;
import uo.ri.business.impl.transactionScript.mechanic.FindMechanicById;
import uo.ri.business.impl.transactionScript.mechanic.ListMechanics;
import uo.ri.business.impl.transactionScript.mechanic.UpdateMechanic;

public class MechanicCRUDImpl implements MechanicCrudService {

    @Override
    public void addMechanic(MechanicDto mecanico)
            throws BusinessException {
        new AddMechanic(mecanico).execute();
    }

    @Override
    public void deleteMechanic(Long idMecanico)
            throws BusinessException {
        new DeleteMechanic(idMecanico).execute();
    }

    @Override
    public void updateMechanic(MechanicDto mecanico)
            throws BusinessException {
        new UpdateMechanic(mecanico).execute();
    }

    @Override
    public MechanicDto findMechanicById(Long id)
            throws BusinessException {
        return new FindMechanicById(id).execute();
    }

    @Override
    public List<MechanicDto> findAllMechanics()
            throws BusinessException {
        return new ListMechanics().execute();
    }

    @Override
    public List<MechanicDto> findActiveMechanics()
            throws BusinessException {
        return new FindActiveMechanics().execute();
    }
}
