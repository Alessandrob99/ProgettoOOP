package OOP_Project.application.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import OOP_Project.application.models.jsonError;
import OOP_Project.application.models.user;
/**
 * 
 * @author Bedetta Alessandro
 * 
 * <p>
 * This class maps all the routes that the user can use.
 * Each route is linked to a specific method in the 'memory' class.
 * If the user hasn't had his credentials checked already,  he won't be able to access any route.
 * Any error that may show up during runtime in this phase is handled by the 'errorHandler' class
 * @see OOP_Project.application.controller.memory
 * @see OOP_Project.application.handlers.errorHandler
 * </p>
 * 
 *
 */
@RestController
public class restController {
	
	/**
	 * 
	 * This is the basic route that allows the user to list all the reviews made on a specific file
	 * @see OOP_Project.application.controller.memory
	 * @param file Indicates the file we want to check
	 * @return A JSONArray filled with the list of all the file reviews
	 */
	
	@GetMapping("/listRev/{file}")
	public Object List_Review(@PathVariable String file){
		if(user.isLOGGED_IN()) {		//If the user is not logged in he won't be able to access any route
			return memory.listReview(file); // JSONArray of reviews
			 			
		}else {
			return new jsonError("The user must authenticate his credentials before using any route",400,"UserNotLoggedError");
		}
	}
	
	/**
	 * This route allows the user to have a list of all the reviews made on a specific day.
	 * If a file name is specified the 'getDailyRevs' method is called
	 * If a 'all' is specified the 'getAllDailyRevs' method is called
	 * 
	 * @param date Indicating the date
	 * @param file If we indicate a file name the program returns the reviews made on that file, if we pass 'all' the reviews are searched on every file of the folder
	 * 
	 * @return A dailyRevsResponse instance containing all the reviews and a counter
	 */
	@PostMapping("/dailyRev/{file}")
	public Object Daily_Revs(@RequestParam(name = "date") String date,@PathVariable String file ) {
		if(user.isLOGGED_IN()) {		//If the user is not logged in he won't be able to access any route
			if(file.compareTo("all")!=0) {
				return memory.getDailyRevs(date,file);	//JSONArray of reviews made on the specified date				
			}else {
				return memory.getAllDailyRevs(date);
			}
		}else {
			return new jsonError("The user must authenticate his credentials before using any route",400,"UserNotLoggedError");
		}
	}
	
	/**
	 * 
	 * This route allows the user to check how many reviews have been made in a specific week.
	 * If a file name is specified the 'getWeeklyRevs' method is called
	 * If a 'all' is specified the 'getAllWeeklyRevs' method is called
	 * @param date The week is passed by passing a date, the system automatically finds the corresponding week
	 * @param file If we indicate a file name the program returns the reviews made on that file, if we pass 'all' the reviews are searched on every file of the folder
	 * @return A dailyRevsResponse instance containing all the reviews and a counter
	 */
	@PostMapping("/weeklyRev/{file}")
	public Object Weekly_Revs(@RequestParam(name = "date") String date,@PathVariable String file ) {
		if(user.isLOGGED_IN()) {		//If the user is not logged in he won't be able to access any route
			if(file.compareTo("all")!=0) {
				return memory.getWeeklyRevs(date,file);	//JSONArray of reviews made on the week matching that date on the file				
			}else {
				return memory.getAllWeeklyRevs(date);	//JSONArray of reviews made on the week matching that date
			}
		}else {
			return new jsonError("The user must authenticate his credentials before using any route",400,"UserNotLoggedError");
		}
	}
	
	

	
	
	/**
	 * 
	 * Once the user gives a directory path and an access token, this route allows the credentials authentication
	 * @see OOP_Project.application.models.user
	 * @param token Indicating the access token to the DropBox API
	 * @param path	Indicating the directory path (default = home folder)
	 * @return True if the authentication process goes well, false otherwise
	 */
	
	@PostMapping("/check")
	public boolean Check_User(@RequestParam(name = "token") String token,@RequestParam(name = "path", defaultValue = "") String path) {
		user.checkUser(token, path);
		return user.isLOGGED_IN();		//Returning true if the credentials are certified, or false if not
	}
	

	
	/**
	 * This route allows the user to get metadata from the directory
	 * @param file If the user indicates a single file he gets metadata from that file only, if he indicates 'all' then he gets the metadata of all the files
	 * @return An object containing the single file data or a JSONArray with all the files' metadata
	 */
	@GetMapping("/metadata/{file}")
	public Object getMetadata(@PathVariable String file) {
		if(user.isLOGGED_IN()) {		//If the user is not logged in he won't be able to access any route
			return memory.getMetadata(file); // JSONArray of metadata
			 			
		}else {
			return new jsonError("The user must authenticate his credentials before using any route",400,"UserNotLoggedError");
		}
	}
	
	/**
	 * Once the user feeds a file name, this route returns a set of statistics regarding timing between reviews
	 * @param file Indicating the file to analyze
	 * @return Returns a JSON containing all the statistics made and the relative values
	 */
	@GetMapping("/stats/{file}")
	public Object Stats(@PathVariable String file) {
		if(user.isLOGGED_IN()) {		//If the user is not logged in he won't be able to access any route
			 return memory.getStats(file); //JSONObject (statResponse) 

		}else {
			return new jsonError("The user must authenticate his credentials before using any route",400,"UserNotLoggedError");
		}
	}
	/**
	 * This route allows the user to access a quick guide to all of the application features 
	 * @return A String of text ( the guide ) 
	 */
	@GetMapping("/help")
	public Object Help() {
		if(user.isLOGGED_IN()) {		//If the user is not logged in he won't be able to access any route
			return "ROUTES:\n-GET /listRev/(file_name)\nSpecifying a file name in this route will return a list \n"
					+ "of reviews linked to a set of information usefull to the identification\n\n"
					+ "-GET /stats/(file_name)\nSpecifying a file name in this route will return a JSONObject containing\n"
					+ "statistics regarding the time lapse between every revision made.\n"
					+ "(Date format :  MM=months DD=days hh=hours mm=minutes ss = seconds)\n\n"
					+ "-POST /dailyRev/(file_name/all)\nSpecifying a file name and adding the date attribute, the app will return  \n"
					+ "a JSON arrray containing all the reviews (+ linked information) made on that date on the given file,and a counter.\n"
					+ "If 'all' is passed instead of the file name, all the review made on that day will be returned.\n"
					+ "(The date must be written respecting the yyyy-mm-DD form)\n\n"
					+ "-POST /weeklyRev/(file_name/all)\nSpecifying a file name and adding the date attribute the app will return  \n"
					+ "a JSON arrray containing a counter and all the reviews (+ linked information) made on the week matched to that day\n"
					+ "If 'all' is passed instead of the file name, all the review made on the corresponding week will be returned.\n"
					+ "(The date must be written respecting the yyyy-mm-DD form)\n\n"
					+ "-GET /metadata/(file_name/all)\nThis route allows the user to get metadata from the directory\n"
					+ "If the user indicates a single file he gets metadata from that file only, if he indicates 'all'\n "
					+ "then he gets the metadata of all the files in the directory.\n\n"
					+ "-POST /check\nThis is the route that performs the user authentication, the user only needs\n"
					+ "to specify the 'token' and 'path' attributes(access token to the DropBox API and folder path)\n"
					+ "A boolean indicating if the credentials are valid or not is returned.";
		}else {
			return new jsonError("The user must authenticate his credentials before using any route",400,"UserNotLoggedError");
		}
		
		
	}
}