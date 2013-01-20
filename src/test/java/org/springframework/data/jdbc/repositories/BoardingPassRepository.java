package org.springframework.data.jdbc.repositories;

import org.springframework.data.jdbc.AbstractJdbcRepository;
import org.springframework.data.jdbc.RowUnmapper;
import org.springframework.data.jdbc.TableDescription;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/20/13, 10:09 AM
 */
public class BoardingPassRepository extends AbstractJdbcRepository<BoardingPass, Object[]> {
	public BoardingPassRepository() {
		super(MAPPER, UNMAPPER, new TableDescription("BOARDING_PASS", null, "flight_no", "seq_no")
		);
	}

	@Override
	protected BoardingPass postCreate(BoardingPass entity, Number generatedId) {
		return entity.withPersisted(true);
	}

	public static final RowMapper<BoardingPass> MAPPER = new RowMapper<BoardingPass>() {
		@Override
		public BoardingPass mapRow(ResultSet rs, int rowNum) throws SQLException {
			final BoardingPass boardingPass = new BoardingPass();
			boardingPass.setFlightNo(rs.getString("flight_no"));
			boardingPass.setSeqNo(rs.getInt("seq_no"));
			boardingPass.setPassenger(rs.getString("passenger"));
			boardingPass.setSeat(rs.getString("seat"));
			return boardingPass.withPersisted(true);
		}
	};

	public static final RowUnmapper<BoardingPass> UNMAPPER = new RowUnmapper<BoardingPass>() {
		@Override
		public Map<String, Object> mapColumns(BoardingPass boardingPass) {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("flight_no", boardingPass.getFlightNo());
			map.put("seq_no", boardingPass.getSeqNo());
			map.put("passenger", boardingPass.getPassenger());
			map.put("seat", boardingPass.getSeat());
			return map;

		}
	};

}
