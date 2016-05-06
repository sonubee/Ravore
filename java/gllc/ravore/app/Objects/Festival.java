package gllc.ravore.app.Objects;

/**
 * Created by bhangoo on 4/29/2016.
 */
public class Festival {

    String address;
    String camping;
    String date;
    String imageUrl;
    String location;
    String name;
    String price;
    String website;
    String ticketsSite;
    double lat;
    double longi;

    public Festival(){}

    public Festival(String address, String camping, String date, String imageUrl, String location, String name, String price, String website, String ticketsSite, double lat, double longi) {
        this.address = address;
        this.camping = camping;
        this.date = date;
        this.imageUrl = imageUrl;
        this.location = location;
        this.name = name;
        this.price = price;
        this.website = website;
        this.ticketsSite = ticketsSite;
        this.lat = lat;
        this.longi = longi;
    }

    public String getAddress() {
        return address;
    }

    public String getCamping() {
        return camping;
    }

    public String getDate() {
        return date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {return price;}

    public String getWebsite() {
        return website;
    }

    public String getTicketsSite() {return ticketsSite;}

    public double getLat() {return lat;}

    public double getLongi() {return longi;}

}
