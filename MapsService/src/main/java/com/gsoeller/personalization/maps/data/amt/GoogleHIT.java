package com.gsoeller.personalization.maps.data.amt;

import java.util.List;

import org.joda.time.DateTime;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class GoogleHIT extends HIT {
	
	public List<GoogleHITUpdate> updates;
	public GoogleControlUpdate control;

	public GoogleHIT() {};
	
	private GoogleHIT(int id, 
			int turkId, 
			List<GoogleHITUpdate> updates, 
			GoogleControlUpdate control, 
			boolean approved, 
			boolean readyForApproval,
			String hitId,
			boolean controlResponse,
			boolean finished,
			Optional<DateTime> created) {
		super(id, turkId, approved, readyForApproval, hitId, controlResponse, finished, created);
		this.updates = updates;
		this.control = control;
	}
	
	public List<GoogleHITUpdate> getUpdates() {
		return updates;
	}

	public void setUpdates(List<GoogleHITUpdate> updates) {
		this.updates = updates;
	}

	public GoogleControlUpdate getControl() {
		return control;
	}

	public void setControl(GoogleControlUpdate control) {
		this.control = control;
	}

	public static class GoogleHITBuilder {
		private int id;
		private int turkId;
		private List<GoogleHITUpdate> updates = Lists.newArrayList();
		private GoogleControlUpdate control;
		private boolean approved;
		private boolean readyForApproval;
		private String hitId;
		private boolean controlResponse;
		private boolean finished;
		private Optional<DateTime> created = Optional.absent();
		
		public GoogleHITBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public GoogleHITBuilder setTurkId(int turkId) {
			this.turkId = turkId;
			return this;
		}
		
		public GoogleHITBuilder setUpdates(List<GoogleHITUpdate> updates) {
			this.updates = updates;
			return this;
		}
		
		public GoogleHITBuilder setControl(GoogleControlUpdate control) {
			this.control = control;
			return this;
		}
		
		public GoogleHITBuilder setApproved(boolean approved) {
			this.approved = approved;
			return this;
		}
		
		public GoogleHITBuilder setReadyForApproval(boolean readyForApproval) {
			this.readyForApproval = readyForApproval;
			return this;
		}
		
		public GoogleHITBuilder setHitId(String hitId) {
			this.hitId = hitId;
			return this;
		}
		
		public GoogleHITBuilder setControlResponse(boolean controlResponse) {
			this.controlResponse = controlResponse;
			return this;
		}
		
		public GoogleHITBuilder setFinished(boolean finished) {
			this.finished = finished;
			return this;
		}
		
		public GoogleHITBuilder setCreated(DateTime created) {
			this.created = Optional.of(created);
			return this;
		}
		
		public GoogleHIT build() {
			return new GoogleHIT(id, turkId, updates, control, approved, readyForApproval, hitId, controlResponse, finished, created);
		}
	}
}