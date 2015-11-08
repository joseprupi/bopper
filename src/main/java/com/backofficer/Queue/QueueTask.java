package com.backofficer.Queue;

import com.backofficer.message.Message;
import com.backofficer.message.MessageDAO;
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
    private MessageDAO message;
    List<Message> oldMessageList;
    private ApplicationContext context;

    private Thread t;
    private AtomicLong counter = new AtomicLong();
    private List<DeferredResult<List<Message>>> subscribedClient = Collections.synchronizedList(new ArrayList<DeferredResult<List<Message>>>());

    public void run() {

        oldMessageList = new ArrayList<Message>(message.getAllMessages());

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

                            List<Message> messageList = message.getAllMessages();

                            if(messageList != null){
                                if(cmp(messageList,oldMessageList)==false){
                                    oldMessageList = new ArrayList<Message>(messageList);
                                    synchronized(subscribedClient) {
                                        Iterator<DeferredResult<List<Message>>> it = subscribedClient.iterator();
                                        while(it.hasNext()) {
                                            DeferredResult<List<Message>> dr = it.next();
                                            dr.setResult(messageList);
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


    public void addSubscribed(DeferredResult<List<Message>> client) {
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
