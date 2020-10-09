
public class Sorting {
	
	public static void main(String[] args) {
		
		
		int arr[] = {54,343,3,34,63};
		int length = arr.length;
		System.out.println("Length of array is "+length);
		
		for(int i = 0; i < length-1;i++) {
			 
			int minVal = i;
			for(int j = i+1;j<length;j++) {
				if(arr[j] < arr[minVal]) {
					minVal = j;
				}
				int temp = arr[minVal];
				arr[minVal] = arr[i];
				arr[i] = temp;
			}
		}
		
		for(int i = 0; i < arr.length;i++) {
			System.out.print(arr[i]+" ");
		}
		
		
		
		
		
		
	}

}
