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
import com.gsoeller.personalization.maps.data.amt.GoogleHIT;
import com.gsoeller.personalization.maps.mappers.amt.GoogleHITMapper;

public class GoogleHITDao {

	private DBI dbi;
	private Handle handle;
	private GoogleHITDaoImpl dao;

	public GoogleHITDao() throws IOException {
		PropertiesLoader propLoader = new PropertiesLoader();
		dbi = new DBI(propLoader.getProperty("db"), propLoader.getProperty("dbuser"), propLoader.getProperty("dbpwd"));
		dbi.registerContainerFactory(new OptionalContainerFactory());
		handle = dbi.open();
		dao = handle.attach(GoogleHITDaoImpl.class);
	}
	
	public Optional<GoogleHIT> getHIT(int hitId) {
		List<GoogleHIT> hits = dao.getHIT(hitId);
		if(hits.size() == 1) {
			return Optional.fromNullable(hits.get(0));
		}
		return Optional.absent();
	}
	
	public List<GoogleHIT> getHITS(int offset, int count, boolean readyForApproval, boolean approved) {
		return dao.getHITS(offset, count, readyForApproval, approved);
	}
	
	public int createHIT(int turkId, int control, boolean approved, boolean readyForApproval) {
		return dao.createHIT(turkId, control, approved, readyForApproval);
	}
	
	public List<GoogleHIT> getNextAvailableHITs(int offset, int count) {
		return dao.getNextAvailableHITs(offset, count);
	}
	
	public int markForApproval(int id) {
		return dao.markForApproval(id);
	}
	
	public int approve(int hitId) {
		return dao.approve(hitId);
	}
	
	private interface GoogleHITDaoImpl {
		@SqlQuery("Select * from GoogleHIT where id = :id")
		@Mapper(GoogleHITMapper.class)
		public List<GoogleHIT> getHIT(@Bind("id") int id);
		
		@SqlQuery("Select * from GoogleHIT where approved = :approved && readyForApproval = :readyForApproval LIMIT :offset, :count")
		@Mapper(GoogleHITMapper.class)
		public List<GoogleHIT> getHITS(@Bind("offset") int offset, 
				@Bind("count") int count, 
				@Bind("readyForApproval") boolean readyForApproval, 
				@Bind("approved") boolean approved);
		
		@SqlUpdate("Insert into GoogleHIT (turkId, control, approved, readyForApproval) values (:turkId, :control, :approved, :readyForApproval)")
		@GetGeneratedKeys
		public int createHIT(@Bind("turkId") int turkId, 
				@Bind("control") int control, 
				@Bind("approved") boolean approved,
				@Bind("readyForApproval") boolean readyForApproval);
		
		@SqlQuery("Select * from GoogleHIT where readyForApproval = false LIMIT :offset, :count")
		@Mapper(GoogleHITMapper.class)
		public List<GoogleHIT> getNextAvailableHITs(@Bind("offset") int offset, @Bind("count") int count);
		
		@SqlUpdate("Update GoogleHIT SET readyForApproval=true where id = :id")
		public int markForApproval(@Bind("id") int id);
		
		@SqlUpdate("Update GoogleHIT SET approved=true where id = :hitId")
		public int approve(@Bind("hitId") int hitId);
	}
	
}
