/**
 * 
 */
package net.xingws.dropwizard.sample.metrics;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import net.xingws.dropwizard.sample.reporter.Reporter;

/**
 * @author bxing
 *
 */
public class MeterSample {
	
	private static final int NUM_SERVICES = 5;

	private static ExecutorService executor = Executors.newFixedThreadPool(NUM_SERVICES);
	static final MetricRegistry metrics = new MetricRegistry();
	static Reporter reporter;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  reporter = new Reporter(metrics, 10);
	      Meter requests = metrics.meter("requests.test");
	      
	      for(int i=0; i<NUM_SERVICES;++i) {
	    	  Runner runner = new Runner(requests);
	    	  executor.submit(runner);
	      }
	      
	      wait5min();
	      
	      executor.shutdown();
	}


	  static void wait5min() {
	      try {
	          Thread.sleep(5*1000*60*500);
	      }
	      catch(InterruptedException e) {}
	  }
}


class Runner implements Runnable {
	private Meter requests;
	private static Random rand=new Random();
	
	public Runner(Meter requests) {
		this.requests = requests;
	}
	
	@Override
	public void run() {
		while(true) {
			this.requests.mark();
			try {
				Thread.sleep(rand.nextInt(5) * 1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
