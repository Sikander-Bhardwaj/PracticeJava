
public class Test {
	public static void main(String[] args) {
		
		int n = 5;
		for(int i = 0; i < n; i++) {
			for(int j = n-i;j > 1;j-- ) {
				System.out.print(" ");
			}
			for(int k = 0;k<=i;k++) {
				System.out.print("* ");
			}
			System.out.println();
			
			
			if(i==n-1) {
				for(int j = 0;j<n-1;j++) {
					for(int space = 0;space<j;space++) {
						System.out.print(" ");
					}
					for(int star =j;star<n-1;star++) {
						System.out.print(" *");
					}
					
					
					System.out.println();
				}
			}
			
			
			
			
		}
		System.out.println("----------------------------------");
		for(int i = 0; i < n; i++) {
			for(int j = n-i;j > 1;j-- ) {
				System.out.print(" ");
			}
			for(int k = 0;k<=i;k++) {
				System.out.print("* ");
			}
			System.out.println();
		}
		
		System.out.println("----------------------------------");
		
		
		
		for(int i = 0; i < n;i++) {
			
			
			for(int space = n-i;space >1;space--) {
				System.out.print(" ");
			}
			
			for(int star = 0;star<=i;star++) {
				System.out.print("*");
			}
			System.out.println();
			
			
		}
		
		
		System.out.println("----------------------------------");
		
		
		for(int i = 0; i < n;i++) {
			
			for(int star = i;star<n;star++) {
				System.out.print("*");
			}
			
			for(int space = 0;space <i;space++ ) {
				System.out.print("");
			}
			
			System.out.println();
		}
		
		
		System.out.println("----------------------------------");
		
		
		for(int i = 0; i < n;i++) {
			
			for(int space = 0;space<i;space++) {
				System.out.print(" ");
			}
			
			for(int star = i;star<n;star++) {
				System.out.print("*");
			}
			System.out.println();
			
			
			
			
			
		}
		
		
		
		
		
		
	}

}
