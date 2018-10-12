package uo.ri.persistence.impl;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.CardDto;
import uo.ri.business.dto.CashDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.exception.BusinessException;
import uo.ri.conf.Conf;
import uo.ri.persistence.PaymentMeanGateway;
import uo.ri.persistence.exception.PersistanceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PaymentMeanGatewayImpl implements PaymentMeanGateway {

    @Override
    public List<PaymentMeanDto> findPaymentMean(String campo, Long valor) throws PersistanceException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<PaymentMeanDto> paymentMeans = new LinkedList<>();
        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance().getProperty("SQL_FIND_ALL_MEDIOS_PAGO_GENERICO"));
            pst.setString(1, campo);
            pst.setLong(1, valor);
            rs = pst.executeQuery();

            while (rs.next()) {
                PaymentMeanDto paymentMean;
                switch (rs.getString("dtype")) {
                    case "TMetalico":
                        CashDto metalico = new CashDto();
                        metalico.id = rs.getLong("id");
                        metalico.accumulated = rs.getDouble("acumulado");
                        metalico.clientId = rs.getLong("cliente_id");
                        paymentMean = metalico;
                        break;
                    case "TBonos":
                        VoucherDto bono = new VoucherDto();
                        bono.id = rs.getLong("id");
                        bono.accumulated = rs.getDouble("acumulado");
                        bono.clientId = rs.getLong("cliente_id");
                        bono.code = rs.getString("codigo");
                        bono.available = rs.getDouble("disponible");
                        bono.description = rs.getString("descripcion");
                        paymentMean = bono;
                        break;
                    case "TTarjetasCredito":
                        CardDto tarjeta = new CardDto();
                        tarjeta.id = rs.getLong("id");
                        tarjeta.accumulated = rs.getDouble("acumulado");
                        tarjeta.clientId = rs.getLong("cliente_id");
                        tarjeta.cardNumber = rs.getString("numero");
                        tarjeta.cardType = rs.getString("tipo");
                        tarjeta.cardExpiration = rs.getTimestamp("validez");
                        paymentMean = tarjeta;
                        break;
                    default:
                        throw new PersistanceException("No existe el metodo de pago: " + rs.getString("dtype"));
                }
                paymentMeans.add(paymentMean);
            }
        } catch (SQLException e) {
            throw new PersistanceException("Error al recuperar los medios de pago:\n\t"+e.getStackTrace());
        } finally {
            Jdbc.close(rs, pst);
        }
        return paymentMeans;
    }

    @Override
    public void updatePaymentMean(PaymentMeanDto paymentMean) {
        PreparedStatement pst = null;

        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(Conf.getInstance().getProperty("SQL_UPDATE_GASTO_MEDIOPAGO_OTROS"));
            pst.setDouble(1, paymentMean.accumulated);
            pst.setLong(2, paymentMean.id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(pst);
        }
    }

    @Override
    public void updatePaymentMean(VoucherDto paymentMean) {
        PreparedStatement pst = null;
        VoucherDto voucher = (VoucherDto) paymentMean;
        try {
            pst = Jdbc.getCurrentConnection().prepareStatement(
                    Conf.getInstance().getProperty("SQL_UPDATE_GASTO_MEDIOPAGO_BONO"));
            pst.setDouble(1, voucher.accumulated);
            pst.setDouble(2, voucher.available);
            pst.setLong(3, voucher.id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(pst);
        }
    }
}
