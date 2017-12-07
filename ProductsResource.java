import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

@Path("/products")
public class ProductsResource {

	@Autowired
	private ProductsService productsSvc;

	@GET
	@Path("/{category}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProducts(@PathParam("category") String category, @QueryParam("priceStart") long priceStart,
			@QueryParam("priceEnd") long priceEnd, @QueryParam("sortRatings") boolean sortRatings,
			@QueryParam("sortReviews") boolean sortReviews) {
		Products products = null;
		products = productsSvc.getProductsByCategory(category, priceStart, priceEnd);
		if (products != null) {
			if (sortRatings || sortReviews) {
				ProductsSortUtil.sortByParams(products.getItems(), sortRatings, sortReviews);
			} else {
				ProductsSortUtil.sortByPopularity(products.getItems());
			}
			return Response.ok(products).build();
		} else {
			ErrorMessage errorMessage = new ErrorMessage(404, "No Products for the category");
			return Response.status(Status.NOT_FOUND).entity(errorMessage).build();
		}
	}

}
