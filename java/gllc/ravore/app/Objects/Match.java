package gllc.ravore.app.Objects;

/**
 * Created by bhangoo on 5/17/2016.
 */
public class Match {

    String person1Id;
    String person2Id;
    String eventName;
    String dateMatched;

    public Match(String person1Id, String person2Id, String eventName, String dateMatched) {
        this.person1Id = person1Id;
        this.person2Id = person2Id;
        this.eventName = eventName;
        this.dateMatched = dateMatched;
    }

    public String getPerson1Id() {
        return person1Id;
    }

    public String getPerson2Id() {
        return person2Id;
    }

    public String getEventName() {
        return eventName;
    }

    public String getDateMatched() {
        return dateMatched;
    }


}
