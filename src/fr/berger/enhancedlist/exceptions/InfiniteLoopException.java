package fr.berger.enhancedlist.exceptions;

import java.io.Serializable;

/**
 * Exception to raise when a loop is infinite.
 * This class extends from Exception, which means it has to be handle.
 * To use this class, an instance of it must be created beforehand, and then use {@code increment()} method at the end
 * of the loop. When the counter will reach the limit, the instance of this exception will be thrown. The default limit
 * is 1'000, and can be changed with {@code setLimit(long)} method.
 * @author Valentin Berger
 * @see java.lang.Exception
 */
public class InfiniteLoopException extends Exception implements Serializable {
	
	protected long limit = 1000L;
	protected long counter = 0L;
	
	public InfiniteLoopException() {
		super();
		setLimit(1000L);
		setCounter(0L);
	}
	
	public InfiniteLoopException(long limit) {
		super();
		setLimit(limit);
		setCounter(0L);
	}
	
	public InfiniteLoopException(String message) {
		super(message);
	}
	
	public InfiniteLoopException(String message, Exception innerException) {
		super(message, innerException);
	}
	
	/* GETTERS & SETTERS */
	
	public long getLimit() {
		return limit;
	}
	
	public void setLimit(long limit) {
		if (limit >= 0)
			this.limit = limit;
	}
	
	public long getCounter() {
		return counter;
	}
	
	public void setCounter(long counter) {
		if (counter >= 0)
			this.counter = counter;
	}
	
	public void increment() throws InfiniteLoopException {
		this.counter++;
		
		if (counter >= limit)
			throw this;
	}
}
