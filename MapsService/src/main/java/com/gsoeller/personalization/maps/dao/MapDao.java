package com.gsoeller.personalization.maps.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.gsoeller.personalization.maps.data.Map;
import com.gsoeller.personalization.maps.mappers.MapWrapper;

public interface MapDao {

	@SqlQuery("Select * from Map")
	@Mapper(MapWrapper.class)
	public List<Map> getMaps();
	
	@SqlQuery("Select * from Map where mapRequest = 1 order by dateTime DESC limit 1")
	@Mapper(MapWrapper.class)
	public List<Map> getMapMostRecentWithMapRequestId(@Bind("mapRequest") int mapRequest);
	
	@SqlUpdate("Insert into Map (hasChanged, mapRequest, path) values (:hasChanged, :mapRequest, :path)")
	@GetGeneratedKeys
	public int saveMap(@Bind("hasChanged") boolean hasChanged, @Bind("mapRequest") int mapRequest, @Bind("path") String path);
}