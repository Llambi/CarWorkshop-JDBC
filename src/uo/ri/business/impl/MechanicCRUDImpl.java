package uo.ri.business.impl;

import uo.ri.business.MechanicCRUDService;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.impl.mechanic.AddMechanic;
import uo.ri.business.impl.mechanic.DeleteMechanic;
import uo.ri.business.impl.mechanic.ListMechanics;
import uo.ri.business.impl.mechanic.UpdateMechanic;

import java.util.List;

public class MechanicCRUDImpl implements MechanicCRUDService {
    @Override
    public void addMechanic(MechanicDto mechanic) {
        new AddMechanic(mechanic).execute();
    }

    @Override
    public void deleteMechanic(MechanicDto mechanic) {
        new DeleteMechanic(mechanic).execute();
    }

    @Override
    public void updateMechanic(MechanicDto mechanic) {
        new UpdateMechanic(mechanic).execute();
    }

    @Override
    public List<MechanicDto> findAllMechanics() {
        return new ListMechanics().execute();
    }
}
