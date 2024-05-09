package se.liu.ida.hefquin.connectors.pg.impl.exceptions;

public class UnSupportedPropertyNameException extends IllegalArgumentException
{
	private static final long serialVersionUID = 4620253439565355196L;

	public UnSupportedPropertyNameException( final String message ) {
		super(message);
	}
}
