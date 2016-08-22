/**
 * 
 */
package net.xingws.dropwizard.sample.metrics;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import net.xingws.dropwizard.sample.reporter.Reporter;

/**
 * @author bxing
 *
 */
public class CountersSample {

	private static final int NUM_SERVICES = 5;

	private static ExecutorService executor = Executors.newFixedThreadPool(NUM_SERVICES);
	static final MetricRegistry metrics = new MetricRegistry();
	static Reporter reporter;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  reporter = new Reporter(metrics, 10);
		  Counter counter = metrics.counter("counter.test");
	      
	      for(int i=0; i<NUM_SERVICES;++i) {
	    	  Runner3 runner = new Runner3(counter);
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


class Runner3 implements Runnable {
	private Counter counter;
	private static Random rand=new Random();
	
	public Runner3(Counter counter) {
		this.counter = counter;
	}
	
	@Override
	public void run() {
		while(true) {
			if(rand.nextBoolean())
				this.counter.inc();
			try {
				Thread.sleep(rand.nextInt(5) * 1000);
			} catch (InterruptedException e) {
			}
			
			if(rand.nextBoolean())
				this.counter.dec();
		}
	}
}