package com.backofficer.payment;

import java.util.List;

public interface PaymentDAO {
    public List<Payment> getAllPayments();
    public Payment getPayment(int id);
    public void updatePayment(Payment student);
    public void deletePayment(Payment student);
}
