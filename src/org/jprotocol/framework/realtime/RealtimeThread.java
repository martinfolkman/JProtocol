package org.jprotocol.framework.realtime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jprotocol.quantity.Quantity;
import org.jprotocol.quantity.Unit;


/**
 */
public class RealtimeThread {
	private static final String THREAD_NAME = "JProtocol " + RealtimeThread.class.getSimpleName();
	private final long frameRateInMs;
    
	private volatile int frameCounter;
	private volatile boolean running;

	private final List<IRealtimeListener> listeners = new ArrayList<IRealtimeListener>();

	public RealtimeThread(Quantity frameRate) {
    	this.frameRateInMs = (long) frameRate.convert(Unit.ms).getValue();
    }
    
	public synchronized void addListener(IRealtimeListener l) {
		listeners.add(l);
	}
	
	public synchronized void removeListener(IRealtimeListener l) {
		listeners.remove(l);
	}
	
    public synchronized void start() {
		new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	running = true;
		        long lastTime = new Date().getTime();
		        while (running) {
		            try {
		                Thread.sleep(frameRateInMs);
		                long newTimestamp = new Date().getTime();
		                long timeDiff = newTimestamp - lastTime;
		                while (timeDiff >= frameRateInMs) {
		                    long timeIncr = timeDiff % frameRateInMs;
		                    timeIncr = timeIncr == 0 ? frameRateInMs : timeIncr;
		                	notifyListeners();
		                    lastTime += timeIncr;
		                    timeDiff -= timeIncr;
		                    frameCounter++;
		                }
		            } catch (InterruptedException e) {
		                //
		            }
		        }
		    }
		}, THREAD_NAME).start();
    }

    public synchronized void stop() {
    	running = false;
    }
    
	private synchronized void notifyListeners() {
		for (IRealtimeListener l: listeners) {
			l.realtimeTic(frameCounter);
		}
	}
}
