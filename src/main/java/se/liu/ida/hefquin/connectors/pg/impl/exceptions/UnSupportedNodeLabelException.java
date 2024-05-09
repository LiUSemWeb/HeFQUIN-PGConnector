package se.liu.ida.hefquin.connectors.pg.impl.exceptions;

public class UnSupportedNodeLabelException extends IllegalArgumentException
{
	private static final long serialVersionUID = 3941135850672518556L;

	public UnSupportedNodeLabelException( final String message ) {
		super(message);
	}
}
