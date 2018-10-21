package uo.ri.persistence;

import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;

import java.util.List;

public interface ContractGateway {
    List<ContractDto> findContract(ContractTypeDto contractDto);
    List<ContractDto> findContract(MechanicDto mechanicDto);
    void addContract(MechanicDto mechanicDto, ContractTypeDto contractTypeDto, ContractCategoryDto contractCategoryDto, ContractDto contractDto);
    void updateContract(ContractDto previousContrac);
}
