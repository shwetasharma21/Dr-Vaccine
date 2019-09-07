
package drVaccine;

public class Vaccine {
    private int vacId;
    private int vacMonth;
    private String vacName;

    private String vacDesc;
    Vaccine(int  vacId, int vacMonth, String vacName,String vacDesc){
        this.vacId=vacId;
        this.vacMonth=vacMonth;
        this.vacName=vacName;
        this.vacDesc=vacDesc;
    }
    public String toString(){
        return vacName;
    }
    public int getVacId() {
        return vacId;
    }
    public int getVacMonth() {
        return vacMonth;
    }
    public String getVacName() {
        return vacName;
    }
    public String getVacDesc() {
        return vacDesc;
    }
}
