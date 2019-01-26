
public enum Status {
	disconnect, checkUser, cordinates, addUser, checkConnection, deafult;

	public static Status convert(int key) {
		switch (key) {
		case 0:
			return disconnect;
				
		case 1:
			return checkUser;
			
		case 2:
			return cordinates;
			
		case 3:
			return addUser;
			
		case 4:
			return checkConnection;

		default:
			return deafult;
		}
	}

	public static int convert(Status key) {
		switch (key) {
		case disconnect:
			return 0;

		case checkUser:
			return 1;

		case cordinates:
			return 2;

		case addUser:
			return 3;

		case checkConnection:
			return 4;

		default:
			return -1;
		}
	}
}