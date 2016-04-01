package gllc.ravore.app.Objects;

/**
 * Created by bhangoo on 3/14/2016.
 */
public class Orders {

    int kandiCount;
    int beadCount;
    int subtotalPrice;
    double shippingPrice;
    double totalPrice;
    String orderNumber;
    String os;
    String address;
    String date;
    String email;
    String fullName;
    String suiteApt;
    String status;
    String deviceId;

    public Orders(int kandiCount, int beadCount, int subtotalPrice, double shippingPrice, double totalPrice, String orderNumber, String os, String address, String date, String email, String fullName, String suiteApt, String status, String deviceId) {
        this.kandiCount = kandiCount;
        this.beadCount = beadCount;
        this.subtotalPrice = subtotalPrice;
        this.shippingPrice = shippingPrice;
        this.totalPrice = totalPrice;
        this.orderNumber = orderNumber;
        this.os = os;
        this.address = address;
        this.date = date;
        this.email = email;
        this.fullName = fullName;
        this.suiteApt = suiteApt;
        this.status = status;
        this.deviceId = deviceId;
    }

    public Orders(){}

    public int getKandiCount() {
        return kandiCount;
    }

    public int getBeadCount() {
        return beadCount;
    }

    public int getSubtotalPrice() {
        return subtotalPrice;
    }

    public double getShippingPrice() {
        return shippingPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getOs() {
        return os;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSuiteApt() {
        return suiteApt;
    }

    public String getStatus() {
        return status;
    }

    public String getDeviceId() {
        return deviceId;
    }







}
