package wallettrial_2;

public class Booth {
    private String boothNumber;

    public Booth(String boothNumber) {
        this.boothNumber = boothNumber ;
    }

    public String getboothNumber() {
        return boothNumber;
    }

    public static Booth fromString(String boothData){
        return new Booth(boothData);
    }

    @Override
    public String toString() {
        return boothNumber;
    }
}
