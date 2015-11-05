package com.backofficer.Queue;

import com.backofficer.payment.Payment;
import com.backofficer.payment.PaymentDAO;
import com.backofficer.payment.impl.PaymentMysqlDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.File;

import javax.jms.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.JmsUtils;
import org.springframework.transaction.annotation.Transactional;

public class MQReader implements MessageListener {
    private static Logger logger = LoggerFactory.getLogger(MQReader.class);

    @Autowired
    private PaymentDAO payment;

    @Override
    public void onMessage(Message message) {
        try {

            TextMessage tm = (TextMessage) message;
            Notification notification = new Notification(tm.getJMSMessageID(),tm.getText());
            saveToBD(notification);

        } catch (JMSException e) {
            throw JmsUtils.convertJmsAccessException(e);
        }
    }

    /**
     * Returns how many times the message has been delivered
     *
     * @param message
     * @return
     * @throws JMSException
     */
    private int getDeliveryNumber(Message message) throws JMSException {
        return message.getIntProperty("JMSXDeliveryCount");
    }

    @Transactional
    private void saveToBD(Notification notification) {

        Payment payment2 = new Payment();
        payment.updatePayment(payment2);

    }
}
