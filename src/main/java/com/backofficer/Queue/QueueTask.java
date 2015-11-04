package com.backofficer.Queue;

import com.backofficer.payment.Payment;
import com.backofficer.payment.PaymentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class QueueTask {

    @Autowired
    private PaymentDAO payment;
    List<Payment> oldPaymentList;
    private ApplicationContext context;

    private Thread t;
    private AtomicLong counter = new AtomicLong();
    private List<DeferredResult<List<Payment>>> subscribedClient = Collections.synchronizedList(new ArrayList<DeferredResult<List<Payment>>>());

    public void run() {

        oldPaymentList = new ArrayList<Payment>(payment.getAllPayments());

        t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while(true) {
                            counter.incrementAndGet();
                            if (counter.get() > Long.MAX_VALUE - 100) {
                                counter.set(0);
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                //log.error(e);
                            }

                            List<Payment> paymentList = payment.getAllPayments();

                            if(paymentList != null){
                                if(cmp(paymentList,oldPaymentList)==false){
                                    oldPaymentList = new ArrayList<Payment>(paymentList);
                                    synchronized(subscribedClient) {
                                        Iterator<DeferredResult<List<Payment>>> it = subscribedClient.iterator();
                                        while(it.hasNext()) {
                                            DeferredResult<List<Payment>> dr = it.next();
                                            dr.setResult(paymentList);
                                            it.remove();
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
        t.setDaemon(true);
        t.setName("BroadcastDeferredThread");
        t.start();
    }


    public void addSubscribed(DeferredResult<List<Payment>> client) {
        synchronized(subscribedClient) {
            subscribedClient.add(client);
        }
    }

    private static boolean cmp( List<?> l1, List<?> l2 ) {

        if ((l1 == null && l2!= null) || (l1 != null && l2== null)){
            return false;
        }

        ArrayList<?> cp = new ArrayList<>( l1 );
        for ( Object o : l2 ) {
            if ( !cp.remove( o ) ) {
                return false;
            }
        }
        return cp.isEmpty();
    }

}
