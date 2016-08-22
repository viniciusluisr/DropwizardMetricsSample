/**
 * 
 */
package net.xingws.dropwizard.sample.reporter;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

/**
 * @author bxing
 *
 */
public class Reporter {
	 private ConsoleReporter reporter;
	 
	 public Reporter(MetricRegistry metrics, long durationInSecond) {
	      this.reporter = ConsoleReporter.forRegistry(metrics)
		          .convertRatesTo(TimeUnit.SECONDS)
		          .convertDurationsTo(TimeUnit.MILLISECONDS)
		          .build();
		      reporter.start(durationInSecond, TimeUnit.SECONDS);
	 }
}
