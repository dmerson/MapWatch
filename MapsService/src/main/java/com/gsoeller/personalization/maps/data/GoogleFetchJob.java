package com.gsoeller.personalization.maps.data;

import org.joda.time.DateTime;

public class GoogleFetchJob implements FetchJob {
	private int id;
	private DateTime startTime;
	private boolean finished;
	private int mapNumber;

	private GoogleFetchJob(int id, DateTime startTime, boolean finished, int mapNumber) {
		this.id = id;
		this.startTime = startTime;
		this.finished = finished;
		this.mapNumber = mapNumber;
	}

	public static class FetchJobBuilder {
		private int id;
		private DateTime startTime;
		private boolean finished;
		private int mapNumber;

		public FetchJobBuilder setMapNumber(int mapNumber) {
			this.mapNumber = mapNumber;
			return this;
		}

		public FetchJobBuilder setId(int id) {
			this.id = id;
			return this;
		}

		public FetchJobBuilder setStartTime(DateTime startTime) {
			this.startTime = startTime;
			return this;
		}

		public FetchJobBuilder setFinished(boolean finished) {
			this.finished = finished;
			return this;
		}

		public GoogleFetchJob build() {
			return new GoogleFetchJob(id, startTime, finished, mapNumber);
		}
	}
}
