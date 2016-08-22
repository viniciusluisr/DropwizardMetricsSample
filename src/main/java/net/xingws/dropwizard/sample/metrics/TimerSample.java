/**
 * 
 */
package net.xingws.dropwizard.sample.metrics;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import net.xingws.dropwizard.sample.reporter.Reporter;

/**
 * @author bxing
 *
 */
public class TimerSample {

	private static final int NUM_SERVICES = 5;

	private static ExecutorService executor = Executors.newFixedThreadPool(NUM_SERVICES);
	static final MetricRegistry metrics = new MetricRegistry();
	static Reporter reporter;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		reporter = new Reporter(metrics, 10);
		Timer timer = metrics.timer("timer.test");

		for (int i = 0; i < NUM_SERVICES; ++i) {
			Runner2 runner = new Runner2(timer);
			executor.submit(runner);
		}

		wait5min();

		executor.shutdown();
	}

	static void wait5min() {
		try {
			Thread.sleep(500 * 1000 * 60);
		} catch (InterruptedException e) {
		}
	}
}

class Runner2 implements Runnable {
	private Timer timer;
	private static Random rand = new Random();

	public Runner2(Timer timer) {
		this.timer = timer;
	}

	@Override
	public void run() {
		while (true) {
			Timer.Context context = this.timer.time();
			try {
				Thread.sleep(rand.nextInt(5000));
			} catch (InterruptedException e) {
			} finally {
				context.stop();
			}
		}
	}
}
