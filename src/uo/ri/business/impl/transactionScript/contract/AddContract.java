package uo.ri.business.impl.transactionScript.contract;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;

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

/**
 * Clase que contiene la logica para crear un nuevo contrato.
 */
public class AddContract {

    private Connection connection;
    private ContractDto contractDto;

    private ContractGateway contractGateway =
            GatewayFactory.getContractGateway();
    private MechanicGateway mechanicGateway =
            GatewayFactory.getMechanicGateway();
    private ContractCategoryGateway categoryGateway =
            GatewayFactory.getContractCategoryGateway();
    private ContractTypeGateway typeGateway =
            GatewayFactory.getContractTypeGateway();

    public AddContract(ContractDto c) {
        contractDto = c;
        this.contractDto.startDate = Dates.firstDayOfMonth(Dates
                .addMonths(this.contractDto.startDate, 1));
        this.contractDto.endDate = this.contractDto.endDate == null
                ? null : Dates.lastDayOfMonth(this.contractDto.endDate);
    }

    /**
     * Metodo que comprueba si el nuevo contrato cumple los
     * prerequisitos y tras ello lo crea.
     *
     * @return Un Map con la informacion sobre la liquidacion
     * si existe, si no, estara vacio.
     * @throws BusinessException
     */
    public void execute() throws BusinessException {
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);
            checkData();

            // ultimo contrato del mecanico dado
            ContractDto previousContrac = this.contractGateway
                    .findContractByMechanicId(this.contractDto.mechanicId)
                    .stream()
                    .max(Comparator.comparing(o -> o.startDate))
                    .orElse(null);

            if (previousContrac != null) {
                previousContrac.endDate = Dates
                        .lastDayOfMonth(previousContrac.endDate == null
                                ? Dates.today()
                                : previousContrac.endDate);
                contractGateway.terminateContract(previousContrac);
            }

            contractGateway.addContract(this.contractDto);

            connection.commit();
        } catch (SQLException | PersistanceException e) {
            try {
                connection.rollback();
                throw new BusinessException
                        ("Imposible añadir el contrato.\n\t" + e);
            } catch (SQLException ignored) {
                throw new BusinessException("Fallo en rollback.");
            }
        } finally {
            Jdbc.close(connection);
        }
    }

    /**
     * Metodo que recupera el estado del mecanico, tipo de contrato,
     * y categoria de contrato dados a traves de sus ids.
     *
     * @throws BusinessException
     */
    private void checkData() throws BusinessException,
            PersistanceException {

        if (mechanicGateway.findMechanicById(this.contractDto.mechanicId)
                == null)
            throw new BusinessException(" El mecanico no existe.");
        if (typeGateway.findContractTypeById(this.contractDto.typeId)
                == null)
            throw new BusinessException(" El tipo de contrato no existe.");
        if (categoryGateway
                .findContractCategoryById(this.contractDto.categoryId)
                == null)
            throw new BusinessException(" La categoria no existe.");
        if (this.contractDto.endDate != null && Dates
                .isBefore(this.contractDto.endDate,
                        this.contractDto.startDate))
            throw new BusinessException(" Fecha no valida.");
        else if (this.contractDto.yearBaseSalary <= 0)
            throw new BusinessException("Salario menor o igual que 0.");


    }

}
