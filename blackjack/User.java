
public class User {
	public String name;
	public int balance;
	
	User(String name) {
		this.name = name;
		this.balance = 1000;
	}
	
	public String getName() {
		return name;
	}
	
	public int getBalance() {
		return balance;
	}
	
	public static void printUserInfo(User user) {
		System.out.println("*****************************");
		System.out.println("Name: " + user.name);
		System.out.println("Balance: $" + user.balance);
		System.out.println("*****************************");
	}
	
	public static void printStake(User user, int stake) {
		System.out.println("*****************************");
		System.out.println("Name: " + user.name);
		System.out.println("Balance: " + user.balance);
		System.out.println("Stake: $" + stake);
		System.out.println("*****************************");
	}
}
