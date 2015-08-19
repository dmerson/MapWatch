package com.gsoeller.personalization.maps.data;

import org.joda.time.DateTime;

public class GoogleMap implements Map {

	private GoogleMapRequest mapRequest;
	private DateTime dateTime;
	private boolean hasChanged;
	private int id;
	private String path;
	private String hash;
	private int fetchJob;
	
	public String toString() {
		return path;
	}
	
	private GoogleMap(MapBuilder builder) {
		this.mapRequest = builder.mapRequest;
		this.dateTime = builder.dateTime;
		this.hasChanged = builder.hasChanged;
		this.id = builder.id;
		this.path = builder.path;
		this.hash = builder.hash;
		this.fetchJob = builder.fetchJob;
	}
	
	public String getHash() {
		return hash;
	}
	
	public String getPath() {
		return path;
	}
	
	public int getId() {
		return id;
	}
	
	public GoogleMapRequest getMapRequest() {
		return mapRequest;
	}
	
	public DateTime getDateTime() {
		return dateTime;
	}
	
	public boolean hasChanged() {
		return hasChanged;
	}
	
	public int getFetchJob() {
		return fetchJob;
	}
	
	public static class MapBuilder {
		
		private GoogleMapRequest mapRequest;
		private DateTime dateTime;
		private boolean hasChanged;
		private int id;
		private String path;
		private String hash;
		private int fetchJob;
		
		public MapBuilder setHash(String hash) {
			this.hash = hash;
			return this;
		}
		
		public MapBuilder setPath(String path) {
			this.path = path;
			return this;
		}
		
		public MapBuilder setMapRequest(GoogleMapRequest mapRequest) {
			this.mapRequest = mapRequest;
			return this;
		}
		
		public MapBuilder setDateTime(DateTime dateTime) {
			this.dateTime = dateTime;
			return this;
		}
		
		public MapBuilder setHasChanged(boolean hasChanged) {
			this.hasChanged = hasChanged;
			return this;
		}
		public MapBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public MapBuilder setFetchJob(int fetchJob) {
			this.fetchJob = fetchJob;
			return this;
		}
		
		public GoogleMap build() {
			return new GoogleMap(this);
		}
	}
}
