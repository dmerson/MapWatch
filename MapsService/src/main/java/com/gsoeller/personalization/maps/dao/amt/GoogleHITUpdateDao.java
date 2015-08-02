package com.gsoeller.personalization.maps.dao.amt;

import io.dropwizard.jdbi.OptionalContainerFactory;

import java.io.IOException;
import java.util.List;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import com.google.common.base.Optional;
import com.gsoeller.personalization.maps.PropertiesLoader;
import com.gsoeller.personalization.maps.data.amt.GoogleHITUpdate;
import com.gsoeller.personalization.maps.mappers.amt.GoogleHITUpdateMapper;
/*
public class GoogleHITUpdateDao {
	
	private DBI dbi;
	private Handle handle;
	private GoogleHITUpdateDaoImpl dao;

	public GoogleHITUpdateDao() throws IOException {
		dbi = new DBI(PropertiesLoader.getProperty("db"), PropertiesLoader.getProperty("dbuser"), PropertiesLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		dao = handle.attach(GoogleHITUpdateDaoImpl.class);
	}
	
	public void close() {
		dao.close();
		handle.close();
		dbi.close(dao);
	}
	
	public List<GoogleHITUpdate> getUpdates(int hitId) {
		return dao.getHITUpdates(hitId);
	}
	
	public List<GoogleHITUpdate> getUpdates(int count, int offset, boolean finished) {
		return dao.getHITUpdates(finished, count, offset);
	}
	
	public Optional<GoogleHITUpdate> getUpdate(int id) {
		List<GoogleHITUpdate> updates =  dao.getHITUpdate(id);
		if(updates.size() == 1) {
			return Optional.of(updates.get(0));
		}
		return Optional.absent();
	}
	
	public int createUpdate(int hitId, int oldMap, int newMap, boolean hasBorderChange, String notes) {
		return dao.createUpdate(hitId, oldMap, newMap, hasBorderChange, notes);
	}
	
	public int countUpdates(int hitId) {
		return dao.countUpdates(hitId);
	}
	
	public Optional<GoogleHITUpdate> getUpdate(String hitId, int id) {
		List<GoogleHITUpdate> updates = dao.getUpdate(hitId, id);
		if(updates.size() == 1) {
			return Optional.fromNullable(updates.get(0));
		}
		return Optional.absent();
	}
	
	public Optional<GoogleHITUpdate> update(int id, boolean hasBorderChanged) {
		dao.update(id, hasBorderChanged);
		return getUpdate("", id);
	}
	
	public Optional<GoogleHITUpdate> updateControlResponse(int id, boolean controlResponse) {
		dao.setControlResponse(id, controlResponse);
		return getUpdate("", id);
	}
	
	public int countUpdatesWithHashSets(String firstHash, String secondHash) {
		return dao.countUpdatesWithTiles(firstHash, secondHash);
	}
	
	private interface GoogleHITUpdateDaoImpl {
		@SqlQuery("Select * from GoogleHITUpdate where hitId = :hitId")
		@Mapper(GoogleHITUpdateMapper.class)
		public List<GoogleHITUpdate> getHITUpdates(@Bind("hitId") int hitId);
		
		@SqlQuery("Select * from GoogleHITUpdate where id = :id")
		@Mapper(GoogleHITUpdateMapper.class)
		public List<GoogleHITUpdate> getHITUpdate(@Bind("id") int id);
		
		@SqlQuery("Select * from GoogleHITUpdate where finished = :finished LIMIT :offset, :count")
		@Mapper(GoogleHITUpdateMapper.class)
		public List<GoogleHITUpdate> getHITUpdates(@Bind("finished") boolean finished,
				@Bind("count") int count,
				@Bind("offset") int offset);
		
		@SqlUpdate("Insert into GoogleHITUpdate (hitId, oldMap, newMap, hasBorderChange, notes) values (:hitId, :oldMap, :newMap, :hasBorderChange, :notes)")
		@GetGeneratedKeys
		public int createUpdate(@Bind("hitId") int hitId,
				@Bind("oldMap") int oldMap,
				@Bind("newMap") int newMap,
				@Bind("hasBorderChange") boolean hasBorderChange,
				@Bind("notes") String notes);
		
		@SqlQuery("Select count(*) from GoogleHITUpdate where hitId = :hitId")
		public int countUpdates(@Bind("hitId") int hitId);
		
		@SqlQuery("Select * from GoogleHITUpdate where id = :id")
		@Mapper(GoogleHITUpdateMapper.class)
		public List<GoogleHITUpdate> getUpdate(@Bind("hitId") String hitId, @Bind("id") int id);
		
		@SqlUpdate("Update GoogleHITUpdate set hasBorderChange=:hasBorderChange, finished=true where id = :id")
		public int update(@Bind("id") int id, @Bind("hasBorderChange") boolean hasBorderChange);
		
		@SqlUpdate("Update GoogleHITUpdate set controlResponse=:controlResponse where id = :id")
		public int setControlResponse(@Bind("id") int id, @Bind("controlResponse") boolean controlResponse);
		
		@SqlQuery("select count(*) from GoogleHITUpdate hit inner join Map o on hit.oldMap = o.id inner join Map n on hit.newMap = n.id where (o.hash = :firstHash && n.hash = :secondHash) || (n.hash = :firstHash && o.hash = :secondHash);")
		public int countUpdatesWithTiles(@Bind("firstHash") String firstHash, @Bind("secondHash") String secondHash);
		
		public void close();
	}
}
*/
public interface GoogleHITUpdateDao {
	@SqlQuery("Select * from GoogleHITUpdate where hitId = :hitId")
	@Mapper(GoogleHITUpdateMapper.class)
	public List<GoogleHITUpdate> getHITUpdates(@Bind("hitId") int hitId);
	
	@SqlQuery("Select * from GoogleHITUpdate where id = :id")
	@Mapper(GoogleHITUpdateMapper.class)
	public List<GoogleHITUpdate> getHITUpdate(@Bind("id") int id);
	
	@SqlQuery("Select * from GoogleHITUpdate where finished = :finished LIMIT :offset, :count")
	@Mapper(GoogleHITUpdateMapper.class)
	public List<GoogleHITUpdate> getHITUpdates(@Bind("finished") boolean finished,
			@Bind("count") int count,
			@Bind("offset") int offset);
	
	@SqlUpdate("Insert into GoogleHITUpdate (hitId, oldMap, newMap, hasBorderChange, notes) values (:hitId, :oldMap, :newMap, :hasBorderChange, :notes)")
	@GetGeneratedKeys
	public int createUpdate(@Bind("hitId") int hitId,
			@Bind("oldMap") int oldMap,
			@Bind("newMap") int newMap,
			@Bind("hasBorderChange") boolean hasBorderChange,
			@Bind("notes") String notes);
	
	@SqlQuery("Select count(*) from GoogleHITUpdate where hitId = :hitId")
	public int countUpdates(@Bind("hitId") int hitId);
	
	@SqlQuery("Select * from GoogleHITUpdate where id = :id")
	@Mapper(GoogleHITUpdateMapper.class)
	public List<GoogleHITUpdate> getUpdate(@Bind("hitId") String hitId, @Bind("id") int id);
	
	@SqlUpdate("Update GoogleHITUpdate set hasBorderChange=:hasBorderChange, finished=true where id = :id")
	public int update(@Bind("id") int id, @Bind("hasBorderChange") boolean hasBorderChange);
	
	@SqlUpdate("Update GoogleHITUpdate set controlResponse=:controlResponse where id = :id")
	public int setControlResponse(@Bind("id") int id, @Bind("controlResponse") boolean controlResponse);
	
	@SqlQuery("select count(*) from GoogleHITUpdate hit inner join Map o on hit.oldMap = o.id inner join Map n on hit.newMap = n.id where (o.hash = :firstHash && n.hash = :secondHash) || (n.hash = :firstHash && o.hash = :secondHash);")
	public int countUpdatesWithTiles(@Bind("firstHash") String firstHash, @Bind("secondHash") String secondHash);
	
	public void close();
}