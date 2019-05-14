package uo.ri.business.impl.transactionScript.contract;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.ContractCategoryGateway;
import uo.ri.persistence.ContractGateway;
import uo.ri.persistence.ContractTypeGateway;
import uo.ri.persistence.MechanicGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Clase que contiene la logica para crear un nuevo contrato.
 */
public class AddContract {

    private Connection connection;
    private ContractDto contractDto;

    private ContractGateway contractGateway = GatewayFactory.getContractGateway();
    private MechanicGateway mechanicGateway = GatewayFactory.getMechanicGateway();
    private ContractCategoryGateway categoryGateway = GatewayFactory.getContractCategoryGateway();
    private ContractTypeGateway typeGateway = GatewayFactory.getContractTypeGateway();

    public AddContract(ContractDto c) {
        contractDto = c;
        this.contractDto.startDate = Dates.firstDayOfMonth(this.contractDto.startDate);
        this.contractDto.endDate = this.contractDto.endDate == null
                ? null : Dates.lastDayOfMonth(Dates.subMonths(this.contractDto.endDate, 1));
    }

    /**
     * Metodo que comprueba si el nuevo contrato cumple los prerequisitos y tras ello lo crea.
     *
     * @return Un Map con la informacion sobre la liquidacion si existe, si no, estara vacio.
     * @throws BusinessException
     */
    public void execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);
            //Recuperamos los objetos necesarios para crear el contrato a partir de los datos facilitados en la ui
            checkData();

            // ultimo contrato del mecanico dado
            List<ContractDto> contractDtoList = this.contractGateway.findContractByMechanicId(this.contractDto.mechanicId);
            if (contractDtoList.size() > 0) {
                ContractDto previousContrac = contractDtoList.get(contractDtoList.size() - 1);
                previousContrac.endDate = Dates.lastDayOfMonth(Dates.today());

                if (isPreviousContract(previousContrac)) {
                    contractGateway.terminateContract(previousContrac);
                }
            }
            contractGateway.addContract(this.contractDto);

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException("Imposible añadir el contrato.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Fallo en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
    }

    /**
     * Metodo que recupera el estado del mecanico, tipo de contrato, y categoria de contrato dados a traves de sus ids.
     *
     * @throws BusinessException
     */
    private void checkData() throws BusinessException {
        try {
            mechanicGateway.findMechanicById(this.contractDto.mechanicId);
            typeGateway.findContractTypeById(this.contractDto.typeId);
            categoryGateway.findContractCategoryById(this.contractDto.categoryId);
            if (this.contractDto.endDate != null && Dates.isBefore(this.contractDto.endDate, this.contractDto.startDate))
                throw new BusinessException(" Fecha no valida.");
            else if (this.contractDto.yearBaseSalary <= 0)
                throw new BusinessException("Salario menor o igual que 0.");

        } catch (PersistanceException e) {
            throw new BusinessException("Imposible recuperar los datos necesarios para crear el contrato.\n\t" + e);
        }

    }

    /**
     * Metodo que comprueba si el nuevo contrato se pisa con el viejo.
     *
     * @param previousContrac Contrato anterior con el que comparar el nuevo.
     * @return Falso si no es pisa, True si se pisa.
     */
    private boolean isPreviousContract(ContractDto previousContrac) {
        boolean flag = false;
        if (previousContrac.endDate == null) {
            // hay que extiguir contrato y liquidar
            flag = true;

        } else {
            if (Dates.isBefore(contractDto.startDate, previousContrac.endDate)) {
                // hay que extiguir contrato y liquidar
                flag = true;
            }
        }
        return flag;
    }
}
