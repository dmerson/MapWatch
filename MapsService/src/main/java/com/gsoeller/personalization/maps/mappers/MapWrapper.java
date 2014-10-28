package com.gsoeller.personalization.maps.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import com.gsoeller.personalization.maps.data.Map;

public class MapWrapper implements ResultSetMapper<Map> {

	public Map map(int arg0, ResultSet r, StatementContext arg2)
			throws SQLException {
		return new Map.MapBuilder()
			.setId(r.getInt("id"))
			.setHasChanged(r.getBoolean("hasChanged"))
			.setMapRequest(r.getInt("mapRequest"))
			.setDateTime(new DateTime(r.getTimestamp("dateTime")))
			.setPath(r.getString("path"))
			.setHash(r.getString("hash"))
			.setFetchJob(r.getInt("fetchJob"))
			.build();
	}
}