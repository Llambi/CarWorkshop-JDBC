package uo.ri.business;

import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;

import java.util.List;

public interface MechanicCRUDService {

    void addMechanic(MechanicDto mechanic) throws BusinessException;
    void deleteMechanic(MechanicDto mechanic) throws BusinessException;
    void updateMechanic(MechanicDto mechanic) throws BusinessException;
    List<MechanicDto> findAllMechanics() throws BusinessException;
}
