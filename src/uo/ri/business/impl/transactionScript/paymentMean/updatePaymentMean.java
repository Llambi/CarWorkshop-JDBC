package uo.ri.business.impl.transactionScript.paymentMean;

import alb.util.jdbc.Jdbc;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.conf.Conf;
import uo.ri.conf.GatewayFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class updatePaymentMean {

    private PaymentMeanDto paymentMean;

    public updatePaymentMean(PaymentMeanDto paymentMean) {
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
        GatewayFactory.getPaymentMeanGateway().updatePaymentMean(paymentMean);
    }

    private void updateVoucher() {
        GatewayFactory.getPaymentMeanGateway().updatePaymentMean((VoucherDto) paymentMean);
    }
}
