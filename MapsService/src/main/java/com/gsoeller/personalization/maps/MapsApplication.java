package com.gsoeller.personalization.maps;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.gsoeller.personalization.maps.amt.HitGenerator;
import com.gsoeller.personalization.maps.jobs.AMTHITJob;
import com.gsoeller.personalization.maps.jobs.ComparisonJob;
import com.gsoeller.personalization.maps.jobs.FetchJob;
import com.gsoeller.personalization.maps.jobs.GenerateGifJob;
import com.gsoeller.personalization.maps.jobs.RequestJob;
import com.gsoeller.personalization.maps.jobs.SQLStressTestJob;
import com.gsoeller.personalization.maps.resources.AMTControlResource;
import com.gsoeller.personalization.maps.resources.AMTResource;
import com.gsoeller.personalization.maps.resources.MapUpdateResource;
import com.gsoeller.personalization.maps.resources.MapsResource;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MapsApplication extends Application<MapsConfiguration> {
	
	public static void main(String[] args) throws Exception {
		Options options = new Options();
		options.addOption("test", false, "Run the application/job using the maps.properties file");
		options.addOption("s", false, "Run the dropwizard server to get REST api access");
		//options.addOption("create", false, "Create the requests to the maps api to be run every time period");
		//options.addOption("fetch", false, "Run the job to fetch requests for each map");
		//options.addOption("compare", true, "Run the job to compare the latest fetched maps for personalization");
		Option option = new Option("h", "Help message");
		
		Option fetch = new Option("fetch", "Run the job to fetch requests for each map with the given map number");
		fetch.hasArg();
		fetch.setType(Integer.class);
		fetch.setArgs(1);
		options.addOption(fetch);
		
		Option compare = new Option("compare", "Run compare job");
		compare.hasArg();
		compare.setType(Integer.class);
		compare.setArgs(1);
		options.addOption(compare);
		
		Option mapProvider = new Option("mp", "Map provider to run the job for");
		mapProvider.hasArg();
		mapProvider.setType(String.class);
		mapProvider.setArgs(1);
		options.addOption(mapProvider);
		
		Option create = new Option("create", "Create the requests to the maps api to be run every time period");
		create.hasArg();
		create.setType(Integer.class);
		create.setArgs(1);
		options.addOption(create);
		
		Option createGifs = new Option("creategifs", "Create Gifs fromt the map request id file");
		options.addOption(createGifs);
		
		Option testSQL = new Option("testSQL", "Run stress tests for the sql queries");
		testSQL.hasArg();
		testSQL.setType(Integer.class);
		testSQL.setArgs(1);
		options.addOption(testSQL);
		
		Option update = new Option("update", "Check for updates between jobs");
		options.addOption(update);
		
		//Option readEmail = new Option("readEmail", "Read personalization email");
		//options.addOption(readEmail);
		
		options.addOption(option);
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args);
		String configFile;
		if(cmd.hasOption("test")) {
			configFile = "/Users/garysoeller/dev/src/MapsPersonalization/MapsService/src/main/resources/maps.properties";
		} else {
			configFile = "/home/soelgary/dev/src/MapsPersonalization/MapsService/src/main/resources/achtung.properties";
		}
		
		if(cmd.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Maps Personalization", options);
		} else if(cmd.hasOption("s")) {
			PropertiesLoader propLoader = new PropertiesLoader(configFile);
			new MapsApplication().run(new String[] {"server", propLoader.getProperty("config")});
		} else if(cmd.hasOption("create")) {
			String mapNumber = (String) cmd.getOptionValue("create");
			String map = (String) cmd.getOptionValue("mp");
			startRequestJob(Integer.parseInt(mapNumber), map);
		} else if(cmd.hasOption("fetch")) {
			System.out.println("fetching");
			String mapNumber = (String) cmd.getOptionValue("fetch");
			String map = (String) cmd.getOptionValue("mp");
			startFetchJob(Integer.parseInt(mapNumber), map);
		} else if(cmd.hasOption("compare")) {
			String fetchJob = (String) cmd.getOptionValue("compare");
			String map = (String) cmd.getOptionValue("mp");
			startCompareJob(Integer.parseInt(fetchJob), map);
		} else if(cmd.hasOption("creategifs")) {
			startGifJob();
		} else if(cmd.hasOption("readEmail")) {
			//readEmailJob();
		} else if(cmd.hasOption("testSQL")) {
			String numQueries = (String) cmd.getOptionValue("testSQL");
			startSQLStressTests(Integer.parseInt(numQueries));
		} else if(cmd.hasOption("update")) {
			System.out.println("MEHHHHHHHHHHHHHHH");
			//HitGenerator hit = new HitGenerator();
			//hit.sendHITToTurk(0);
			startUpdateJob(1);
		}
		else {
			new MapsApplication().run(args);
		}
	}

	@Override
	public void initialize(Bootstrap<MapsConfiguration> bootstrap) {
		bootstrap.addBundle(new MigrationsBundle<MapsConfiguration>() {
			public DataSourceFactory getDataSourceFactory(
					MapsConfiguration configuration) {
				return configuration.getDataSourceFactory();
			}
		});
	}

	@Override
	public void run(MapsConfiguration config, Environment environment)
			throws Exception {
		FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
	    filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
	    filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
	    filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
	    filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
	    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		environment.jersey().register(new MapUpdateResource());
		environment.jersey().register(new AMTResource());
		environment.jersey().register(new AMTControlResource());
	}
	
	private static void startCompareJob(int fetchJob, String mapProvider) throws SchedulerException {
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();
		
		JobDetail job = JobBuilder.newJob(ComparisonJob.class)
				.withIdentity("Comparison Job", "group1")
				.build();
		
		job.getJobDataMap().put("fetchJob", fetchJob);
		job.getJobDataMap().put("mapProvider", mapProvider);
		
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("Comparison Trigger", "group1")
				.startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
								.withIntervalInHours(24).repeatForever())
				.build();
		sched.scheduleJob(job, trigger);
	}
	
	private static void startRequestJob(int mapNumber, String mapProvider) throws SchedulerException {
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();

		JobDetail job = JobBuilder.newJob(RequestJob.class)
				.withIdentity("Request Job", "group1").build();

		job.getJobDataMap().put("mapNumber", mapNumber);
		job.getJobDataMap().put("mapProvider", mapProvider);
		
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("Request Trigger", "group1")
				.startNow()
				.build();
		sched.scheduleJob(job, trigger);
	}

	private static void startFetchJob(int mapNumber, String mapProvider) throws SchedulerException {
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();

		JobDetail job = JobBuilder.newJob(FetchJob.class)
				.withIdentity("Fetch Job", "group1").build();
		
		job.getJobDataMap().put("mapNumber", mapNumber);
		job.getJobDataMap().put("mapProvider", mapProvider);
		
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("Fetch Trigger", "group1")
				.startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInMinutes(60)
						.repeatForever())
				.build();
		sched.scheduleJob(job, trigger);
	}
	
	private static void startGifJob() throws SchedulerException {
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();

		JobDetail job = JobBuilder.newJob(GenerateGifJob.class)
				.withIdentity("Gif Job", "group1").build();
		
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("Fetch Trigger", "group1")
				.startNow()
				.build();
		sched.scheduleJob(job, trigger);		
	}
	
	private static void startSQLStressTests(int numQueries) throws SchedulerException {
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();

		JobDetail job = JobBuilder.newJob(SQLStressTestJob.class)
				.withIdentity("Stress Test Job", "group1").build();
		
		job.getJobDataMap().put("numQueries", numQueries);
		
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("Stress Trigger", "group1")
				.startNow()
				.build();
		sched.scheduleJob(job, trigger);		
	}
	
	private static void startUpdateJob(int fetchJob) throws SchedulerException {
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();

		JobDetail job = JobBuilder.newJob(AMTHITJob.class)
				.withIdentity("AMT HIT Job", "group1").build();
		
		job.getJobDataMap().put("fetchJob", fetchJob);
		
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("AMTHIT Trigger", "group1")
				.startNow()
				.build();
		sched.scheduleJob(job, trigger);		
	}
	
	/*
	private static void readEmailJob() throws SchedulerException {
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		sched.start();

		JobDetail job = JobBuilder.newJob(ReadEmailJob.class)
				.withIdentity("Read Email Job", "group1").build();
		
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("Fetch Trigger", "group1")
				.startNow()
				.build();
		sched.scheduleJob(job, trigger);
	}
	*/
}