package elyland.cachemap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
        CacheMap<Integer, String> map = new CacheMapImpl<>();
        map.setTimeToLive(100);
        map.put(1, "one");
        Thread.sleep(200);
        map.put(1, "two");
        System.out.println(map.get(1));
    }
}
