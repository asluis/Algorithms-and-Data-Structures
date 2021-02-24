class MyMergeSort{
    public int[] sortArray(int[] nums) {
        mergeSort(nums, new int[nums.length], 0, nums.length - 1);
        return nums;
    }
    
    
    // temp array will only be used for mergeHalves function
    private void mergeSort(int[] nums, int[] temp, int leftStart, int rightEnd){
        if(leftStart >= rightEnd){ // Out of bounds, stop
            return;
        }
        
        int midPoint = (leftStart + rightEnd) / 2;
        
        mergeSort(nums, temp, leftStart, midPoint);// Sort from left to the middle
        // Plus 1 because we already covered this element above
        mergeSort(nums, temp, midPoint + 1, rightEnd); // Sort from middle to rightEnd
        
        mergeHalves(nums, temp, leftStart, rightEnd); // Merge from left starting position to the right end
        
    }
    
    public void mergeHalves(int[] arr, int[] temp, int leftStart, int rightEnd){
        int midPoint = (leftStart + rightEnd) / 2;
        
        int rightStart = midPoint + 1;
        
        int size = rightEnd - leftStart + 1;
        
        // Indices used for tracking current location
        int left = leftStart;
        int right = rightStart;
        int index = leftStart; // Used for temp array, since leftStart is first element we have available it will also keep track of location in temp
        
        while(left <= midPoint && right <= rightEnd){// While both still in bounds
            if(arr[left] <= arr[right]){
                temp[index] = arr[left];
                left++;
            }else{
                temp[index] = arr[right];
                right++;
            }
            index++;
        }
        
        // Copy remainder of either array into our temp array
        
        System.arraycopy(arr,left, temp, index, midPoint - left + 1);
        System.arraycopy(arr,right, temp, index, rightEnd - right + 1);

        // Now copy temp into arr
        
        System.arraycopy(temp, leftStart, arr, leftStart, size);
    }

}
