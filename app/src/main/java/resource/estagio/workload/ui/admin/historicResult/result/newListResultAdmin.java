package resource.estagio.workload.ui.admin.historicResult.result;

public class newListResultAdmin {

    private String customerName;
    private String projectName;
    private int allHours;
    private int hour;

    public newListResultAdmin(String customerName, String projectName, int allHours, int hour) {
        this.customerName = customerName;
        this.projectName = projectName;
        this.allHours = allHours;
        this.hour = hour;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getAllHours() {
        return allHours;
    }

    public void setAllHours(int allHours) {
        this.allHours = allHours;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
