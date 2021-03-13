package lifter.app;

public class Schedule {
    String id;
    String email;
    String day;
    String from;
    String to;
    String muscle;
    String fromSpecific;

    public Schedule() {

    }

    public Schedule(String id, String email, String day, String from, String to, String muscle, String fromSpecific) {
        this.id = id;
        this.email = email;
        this.day = day;
        this.from = from;
        this.to = to;
        this.muscle = muscle;
        this.fromSpecific = fromSpecific;
    }

    public String getId() { return id; }

    public String getEmail() {return email;}

    public String getDay() { return day; }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getMuscle() {
        return muscle;
    }

    public String getFromSpecific() {
        return fromSpecific;
    }
}

