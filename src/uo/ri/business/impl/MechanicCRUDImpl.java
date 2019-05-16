package uo.ri.business.impl;

import uo.ri.business.MechanicCrudService;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.business.impl.transactionScript.mechanic.*;

import java.util.List;

public class MechanicCRUDImpl implements MechanicCrudService {

    @Override
    public void addMechanic(MechanicDto mecanico) throws BusinessException {
        new AddMechanic(mecanico).execute();
    }

    @Override
    public void deleteMechanic(Long idMecanico) throws BusinessException {
        new DeleteMechanic(idMecanico).execute();
    }

    @Override
    public void updateMechanic(MechanicDto mecanico) throws BusinessException {
        new UpdateMechanic(mecanico).execute();
    }

    @Override
    public MechanicDto findMechanicById(Long id) throws BusinessException {
        return new FindMechanicById(id).execute();
    }

    @Override
    public List<MechanicDto> findAllMechanics() throws BusinessException {
        return new ListMechanics().execute();
    }

    @Override
    public List<MechanicDto> findActiveMechanics() throws BusinessException {
        //TODO:
        return new FindActiveMechanics().execute();
    }
}
