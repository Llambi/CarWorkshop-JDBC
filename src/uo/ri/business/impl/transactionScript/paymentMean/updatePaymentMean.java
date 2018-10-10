package uo.ri.business.impl.transactionScript.paymentMean;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.conf.Conf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class updatePaymentMean {

    private PaymentMeanDto paymentMean;

    private Connection connection;

    public updatePaymentMean(Connection connection, PaymentMeanDto paymentMean) {
        this.connection = connection;
        this.paymentMean = paymentMean;
    }

    public void execute() {
        if (paymentMean instanceof VoucherDto){
            updateVoucher();
        }else{
            updatePayment();
        }

    }

    private void updatePayment() {
        PreparedStatement pst = null;

        try {
            pst = connection.prepareStatement(Conf.getInstance().getProperty("SQL_UPDATE_GASTO_MEDIOPAGO_OTROS"));
            pst.setDouble(1, paymentMean.accumulated);
            pst.setLong(2, paymentMean.id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Jdbc.close(pst);
        }
    }

    private void updateVoucher() {
        PreparedStatement pst = null;
        VoucherDto voucher = (VoucherDto) paymentMean;
        try {
            pst = connection.prepareStatement(
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
