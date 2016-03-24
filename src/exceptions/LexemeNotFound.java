package exceptions;

public class LexemeNotFound extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LexemeNotFound(Throwable e){
		initCause(e);
	}
}
