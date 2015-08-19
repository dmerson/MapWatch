package com.gsoeller.personalization.maps.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gsoeller.personalization.maps.data.GoogleMap;
import com.gsoeller.personalization.maps.data.GoogleMapRequest;

public class GoogleMapWrapper implements ResultSetMapper<GoogleMap> {

	public GoogleMap map(int arg0, ResultSet r, StatementContext arg2)
			throws SQLException {
		return new GoogleMap.MapBuilder()
			.setId(r.getInt("id"))
			.setHasChanged(r.getBoolean("hasChanged"))
			.setMapRequest(new GoogleMapRequest.MapRequestBuilder().setId(r.getInt("mapRequest")).build())
			.setDateTime(new DateTime(r.getTimestamp("dateTime")))
			.setPath(r.getString("path"))
			.setHash(r.getString("hash"))
			.setFetchJob(r.getInt("fetchJob"))
			.build();
	}
}