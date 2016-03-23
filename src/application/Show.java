package application;

// Container for the database data
// Modify as needed

public class Show {

		String title;
		String date;
		String venue;
		Integer freeSeats;
		
		// constructor for "no show"
		public Show() {
			init("","","",-1);
		}
		
		// constructor defining all content
		public Show(String t, String d, String v, Integer fs) {
			init(t,d,v,fs);
		}
		
		// constructor defining only the title
		public Show(String t) {
			init(t,"","",-1);
		}
		
		// all constructors use this
		private void init(String t, String d, String v, Integer fs) {
			title = t; date = d; venue = v; freeSeats = fs;
		}
}
