package uo.ri.persistence;

import uo.ri.business.dto.ClientDto;
import uo.ri.persistence.exception.PersistanceException;

public interface ClientGateway {

    ClientDto findClient(String campo, Long valor) throws PersistanceException;
}
