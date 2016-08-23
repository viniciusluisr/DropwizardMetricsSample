/**
 * 
 */
package net.xingws.dropwizard.sample.metrics;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SlidingTimeWindowReservoir;

import net.xingws.dropwizard.sample.reporter.Reporter;

/**
 * @author bxing
 *
 */
public class HistogramsSample {

	private static final int NUM_SERVICES = 5;

	private static ExecutorService executor = Executors.newFixedThreadPool(NUM_SERVICES);
	static final MetricRegistry metrics = new MetricRegistry();
	static Reporter reporter;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  reporter = new Reporter(metrics, 10);
		  Histogram histogram =  metrics.register("histogram.test", new Histogram(new SlidingTimeWindowReservoir(1, TimeUnit.MINUTES)));
	      
	      for(int i=0; i<NUM_SERVICES;++i) {
	    	  Runner1 runner = new Runner1(histogram);
	    	  executor.submit(runner);
	      }
	      
	      wait5min();
	      
	      executor.shutdown();
	}


	  static void wait5min() {
	      try {
	          Thread.sleep(500*1000*60);
	      }
	      catch(InterruptedException e) {}
	  }
}

class Runner1 implements Runnable {
	private Histogram histogram;
	private static Random rand=new Random();
	
	public Runner1(Histogram histogram) {//MetricRegistry metrics, String name) {
		this.histogram = histogram;
		//this.histogram = metrics.register(name, new Histogram(new SlidingTimeWindowReservoir(1, TimeUnit.MINUTES)));
		//this.histogram = metrics.histogram(name);
	}
	
	@Override
	public void run() {
		while(true) {
			long size = Math.abs(rand.nextLong() % 1000);
//			System.out.println(size);
			this.histogram.update(size);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
