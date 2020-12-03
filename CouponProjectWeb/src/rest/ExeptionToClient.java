package rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExeptionToClient implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable throwable) {
		return Response.status(500).entity("eror in system: "+ throwable.getMessage()).type(MediaType.TEXT_PLAIN).build();
	}

}
