package uo.ri.business.impl.transactionScript.contract;

import alb.util.date.Dates;
import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.ContractCategoryDto;
import uo.ri.business.dto.ContractDto;
import uo.ri.business.dto.ContractTypeDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.GatewayFactory;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que contiene la logica para crear un nuevo contrato.
 */
public class AddContract {

    private MechanicDto mechanicDto;
    private ContractTypeDto contractTypeDto;
    private ContractCategoryDto contractCategoryDto;
    private ContractDto contractDto;
    private Connection connection;

    public AddContract(MechanicDto mechanicDto, ContractTypeDto contractTypeDto
            , ContractCategoryDto contractCategoryDto, ContractDto contractDto) {
        this.mechanicDto = mechanicDto;
        this.contractTypeDto = contractTypeDto;
        this.contractCategoryDto = contractCategoryDto;
        this.contractDto = contractDto;
        // Arreglo de fin de contrato
        this.contractDto.endDate = this.contractDto.endDate == null
                ? null : Dates.lastDayOfMonth(Dates.subMonths(this.contractDto.endDate, 1));
    }

    /**
     * Metodo que comprueba si el nuevo contrato cumple los prerequisitos y tras ello lo crea.
     *
     * @return Un Map con la informacion sobre la liquidacion si existe, si no, estara vacio.
     * @throws BusinessException
     */
    public Map<String, Object> execute() throws BusinessException {
        // Comprobar si el mecanico tiene contratos en vigor (el ultimo) en la fecha que se da, si no se añade,
        // si lo tiene pasa a extinto y se le calculara la liquidacion
        Map<String, Object> liquidacion = null;
        try {
            connection = Jdbc.createThreadConnection();
            connection.setAutoCommit(false);
            //Recuperamos los objetos necesarios para crear el contrato a partir de los datos facilitados en la ui
            recoveryObjectsState();

            // ultimo contrato del mecanico dado
            List<ContractDto> contractDtoList = GatewayFactory.getContractGateway().findContract(mechanicDto);
            if (contractDtoList.size() > 0) {
                ContractDto previousContrac = contractDtoList.get(contractDtoList.size() - 1);
                previousContrac.endDate = Dates.lastDayOfMonth(Dates.today());

                if (isPreviousContract(previousContrac)) {
                    GatewayFactory.getContractGateway().terminateContract(previousContrac);
                    liquidacion = liquidarContrato(previousContrac);
                }
            }
            GatewayFactory.getContractGateway()
                    .addContract(mechanicDto, contractTypeDto, contractCategoryDto, contractDto);

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
        return liquidacion;
    }

    /**
     * Metodo que recupera el estado del mecanico, tipo de contrato, y categoria de contrato dados a traves de sus ids.
     *
     * @throws BusinessException
     */
    private void recoveryObjectsState() throws BusinessException {
        try {
            this.mechanicDto = GatewayFactory.getMechanicGateway().findMechanic(this.mechanicDto);
            this.contractTypeDto = GatewayFactory.getContractTypeGateway().findContractType(this.contractTypeDto);
            this.contractCategoryDto = GatewayFactory.getContractCategoryGateway()
                    .findContractCategory(this.contractCategoryDto);
        } catch (PersistanceException e) {
            throw new BusinessException("Imposible recuperar los datos necesarios para crear el contrato.\n\t" + e);
        }

    }

    /**
     * Metodo que genera la liquidacion de un contrato.
     *
     * @param previousContrac contrato que se tiene que liquidar.
     * @return Un Map con toda la informacion de la liquidacion.
     */
    private Map<String, Object> liquidarContrato(ContractDto previousContrac) {
        double contractYears = isOneYearWorked(previousContrac);
        Map<String, Object> liquidacion = null;
        if (contractYears >= 1.0) {
            liquidacion = new HashMap<>();
            liquidacion.put("salarioBruto", previousContrac.yearBaseSalary);
            liquidacion.put("indemnizacion", contractTypeDto.compensationDays);
            liquidacion.put("añosContrato", Math.round(contractYears));
            liquidacion.put("total", previousContrac.yearBaseSalary
                    * contractTypeDto.compensationDays * Math.round(contractYears));
        }
        return liquidacion;
    }

    /**
     * Metodo que comprueba si se ha trabajado un año desde el inicio del contrato.
     *
     * @param previousContrac Contrato que se quiere comprobar.
     * @return Un double con el modificador.
     */
    private double isOneYearWorked(ContractDto previousContrac) {
        Date startDate = previousContrac.startDate;
        Date today = Dates.today();
        return Dates.diffDays(startDate, today) / 365.;
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
