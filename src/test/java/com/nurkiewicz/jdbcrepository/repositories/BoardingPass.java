package com.nurkiewicz.jdbcrepository.repositories;

import org.springframework.data.domain.Persistable;

import static com.nurkiewicz.jdbcrepository.JdbcRepository.pk;

/**
 * @author Tomasz Nurkiewicz
 * @since 1/20/13, 10:04 AM
 */
public class BoardingPass implements Persistable<Object[]> {

	private transient boolean persisted;

	private String flightNo;

	private int seqNo;

	private String passenger;

	private String seat;

	public BoardingPass() {
	}

	public BoardingPass(String flightNo, int seqNo, String passenger, String seat) {
		this.flightNo = flightNo;
		this.seqNo = seqNo;
		this.passenger = passenger;
		this.seat = seat;
	}

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	public String getPassenger() {
		return passenger;
	}

	public void setPassenger(String passenger) {
		this.passenger = passenger;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	@Override
	public Object[] getId() {
		return pk(flightNo, seqNo);
	}

	@Override
	public boolean isNew() {
		return !persisted;
	}

	public BoardingPass withPersisted(boolean persisted) {
		this.persisted = persisted;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BoardingPass)) return false;

		BoardingPass that = (BoardingPass) o;

		if (seqNo != that.seqNo) return false;
		if (flightNo != null ? !flightNo.equals(that.flightNo) : that.flightNo != null) return false;
		if (passenger != null ? !passenger.equals(that.passenger) : that.passenger != null) return false;
		return !(seat != null ? !seat.equals(that.seat) : that.seat != null);

	}

	@Override
	public int hashCode() {
		int result = flightNo != null ? flightNo.hashCode() : 0;
		result = 31 * result + seqNo;
		result = 31 * result + (passenger != null ? passenger.hashCode() : 0);
		result = 31 * result + (seat != null ? seat.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "BoardingPass{flightNo='" + flightNo + '\'' + ", seqNo=" + seqNo + ", passenger='" + passenger + '\'' + ", seat='" + seat + '\'' + '}';
	}
}
