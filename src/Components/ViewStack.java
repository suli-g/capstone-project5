package Components;

import java.util.Stack;

/**
 * {@inheritDoc Stack}
 */
public class ViewStack<T> extends Stack<T> {
    /** 
     * Prints the current object to the console.
     * @return Object
     */
    public synchronized T show() {
        T currentObject =  super.peek();
        System.out.println(currentObject);
        return currentObject;
    }
}
