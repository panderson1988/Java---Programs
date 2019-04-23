package package2;
import java.util.ArrayList;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Examples from lecture as a means to practice and learn
		int i = 2;
		System.out.println("Done");
		if (i < 3) {
			i =3 ;
			i = i + 6;
			System.out.println("Number should be" + " " + i); //Won't print if it doesn't meet if statement
		}
	}
	
	
	public void counter(String[] args) {
		
		int i = 0;
		for (int counter = 0; counter < i; counter++) {
			System.out.println(i);
		}
	}
	//public static void

public static long factorial(int n) { 
	if (n == 1) return 1; 
    return n * factorial(n-1); 
    
	}
}


