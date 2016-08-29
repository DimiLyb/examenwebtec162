package edu.ap.jaxrs;

import java.io.*;

import javax.ws.rs.*;
import javax.json.*;

@Path("/inhaalex")
public class ProductResource {
	
	static final String FILE = "/Users/vjdagi/Desktop/inhaalex.json";
	
	/*@GET
	@Produces({"text/html"})
	public String getProductsHTML() {
		String htmlString = "<html><body>";
		try {
			JAXBContext jaxbContext1 = JAXBContext.newInstance(ProductsXML.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();
			File XMLfile = new File("/Users/philippepossemiers/Desktop/Products.xml");
			ProductsXML productsXML = (ProductsXML)jaxbUnmarshaller.unmarshal(XMLfile);
			ArrayList<Product> listOfProducts = productsXML.getProducts();
			
			for(Product product : listOfProducts) {
				htmlString += "<b>Name : " + product.getName() + "</b><br>";
				htmlString += "Id : " + product.getId() + "<br>";
				htmlString += "Brand : " + product.getBrand() + "<br>";
				htmlString += "Description : " + product.getDescription() + "<br>";
				htmlString += "Price : " + product.getPrice() + "<br>";
				htmlString += "<br><br>";
			}
		} 
		catch (JAXBException e) {
		   e.printStackTrace();
		}
		return htmlString;
	}*/
	
	@GET
	@Produces({"text/html"})
	public String getProductsHTML() {
		String htmlString = "<html><body>";
		try {
			JsonReader reader = Json.createReader(new StringReader(getProductsJSON()));
			JsonObject rootObj = reader.readObject();
			JsonArray array = rootObj.getJsonArray("inhaalex");
			//JsonArray sortarray = new JsonArray();
			//for (int i = 0; i < array.size(); i++)
			//{
			//	
			//}
			
			
			for(int i = 0 ; i < array.size(); i++) {
				JsonObject obj = array.getJsonObject(i);
				
				htmlString += "<b>Datum : " + obj.getString("datum") + "</b><br>";
				htmlString += "Naam : " + obj.getString("naam") + "<br>";
				htmlString += "Examen : " + obj.getString("examen") + "<br>";
				htmlString += "Reden : " + obj.getString("reden") + "<br>";
			
				htmlString += "<br><br>";
			}
		}
		catch(Exception ex) {
			htmlString = "<html><body>" + ex.getMessage();
		}
		
		return htmlString + "</body></html>";
	}
	
	@GET
	@Produces({"application/json"})
	public String getProductsJSON() {
		String jsonString = "";
		try {
			InputStream fis = new FileInputStream(FILE);
	        JsonReader reader = Json.createReader(fis);
	        JsonObject obj = reader.readObject();
	        reader.close();
	        fis.close();
	        
	        jsonString = obj.toString();
		} 
		catch (Exception ex) {
			jsonString = ex.getMessage();
		}
		
		return jsonString;
	}
	
	@GET
	@Path("{id}")
	@Produces({"application/json"})
	public String getProductJSON(@PathParam("id") String id) {
		String jsonString = "";
		try {
			InputStream fis = new FileInputStream(FILE);
	        JsonReader reader = Json.createReader(fis);
	        JsonObject jsonObject = reader.readObject();
	        reader.close();
	        fis.close();
	        
	        JsonArray array = jsonObject.getJsonArray("products");
	        for(int i = 0; i < array.size(); i++) {
	        	JsonObject obj = array.getJsonObject(i);
	        	if(obj.getString("id").equalsIgnoreCase(id)) {
	            	jsonString = obj.toString();
	            	break;
	            }
	        }
		} 
		catch (Exception ex) {
			jsonString = ex.getMessage();
		}
		
		return jsonString;
	}
	
	@POST
	@Consumes({"application/json"})
	public String addProduct(String inhaalJSON) {
		String returnCode = "";
		try {
			// read existing products
			InputStream fis = new FileInputStream(FILE);
	        JsonReader jsonReader1 = Json.createReader(fis);
	        JsonObject jsonObject = jsonReader1.readObject();
	        jsonReader1.close();
	        fis.close();
	        
	        JsonReader jsonReader2 = Json.createReader(new StringReader(inhaalJSON));
	        JsonObject newObject = jsonReader2.readObject();
	        jsonReader2.close();
	        
	        JsonArray array = jsonObject.getJsonArray("inhaalex");
	        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
	        
	        for(int i = 0; i < array.size(); i++){
	        	// add existing products
	        	JsonObject obj = array.getJsonObject(i);
	        	arrBuilder.add(obj);
	        }
	        // add new product
	        arrBuilder.add(newObject);
	        
	        // now wrap it in a JSON object
	        JsonArray newArray = arrBuilder.build();
	        JsonObjectBuilder builder = Json.createObjectBuilder();
	        builder.add("inhaalex", newArray);
	        JsonObject newJSON = builder.build();

	        // write to file
	        OutputStream os = new FileOutputStream(FILE);
	        JsonWriter writer = Json.createWriter(os);
	        writer.writeObject(newJSON);
	        writer.close();
		} 
		catch (Exception ex) {
			returnCode = ex.getMessage();
		}
		
		return returnCode;
	}
}