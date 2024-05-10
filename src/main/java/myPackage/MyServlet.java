package myPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// API setup
					String apikey = "Use Your API Key";
					
					// Get the input name city variable from html file
					String city = request.getParameter("city");
					
					// Create the url for the open weather API request 
					String apiurl = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+ apikey;
					
		try {
			
			// API Integration 
			URL url = new URL(apiurl);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			
			// Reading the data from Network
			InputStream inputstream = connection.getInputStream();
			InputStreamReader reader = new InputStreamReader(inputstream);
			
			// take input from input reader so will create Scanner class
			Scanner sc = new Scanner(reader);		
			
			
			//Store in string Builder 
			StringBuilder responseContent = new StringBuilder();
			
			
			
			while(sc.hasNext()) {
				responseContent.append(sc.nextLine());
			}
			
			sc.close();
			
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(responseContent.toString(),JsonObject.class);
			
			//System.out.println(jsonObject);
			
			//Date Time
			long dateTimestamp = jsonObject.get("dt").getAsLong()*1000;
			String date = new Date(dateTimestamp).toString();
			
			//Temperature 
			 double temperatureKelvin = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
			 int temperatureCelsius = (int)  (temperatureKelvin-273.15);
			 
			 //Humidity
			 int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
			 
			 //windspeed
			 double windspeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
			 
			 //weatherCondition
			 String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
			
			 request.setAttribute("date", date);
			 request.setAttribute("city", city);
			 request.setAttribute("temperature", temperatureCelsius);
			 request.setAttribute("weatherCondition",weatherCondition);
			 request.setAttribute("humidity", humidity);
			 request.setAttribute("windspeed", windspeed);
			 request.setAttribute("weatherData", responseContent.toString());
			 
			 connection.disconnect();
			 
			 //Forward the request to the weather.jsp page
			 request.getRequestDispatcher("index.jsp").forward(request,response);
			 
			 
			
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
		
	
	}

}
