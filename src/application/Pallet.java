package application;

/**
 * Created by bas11lbr on 23/03/16.
 */
public class Pallet {
    public String id;
    public String orderId;
    public boolean isBlocked;
    public String prodDate;
    public String cookieName;

    public Pallet(String id, String orderId, boolean isBlocked, String prodDate){
        this.id = id;
        this.orderId = orderId;
        this.isBlocked = isBlocked;
        this.prodDate = prodDate;
    }

    public Pallet(){

    }
}
