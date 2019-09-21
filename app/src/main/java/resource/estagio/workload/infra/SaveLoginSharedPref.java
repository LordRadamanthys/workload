package resource.estagio.workload.infra;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveLoginSharedPref {
    private Context context;
    private long id;
    private String token;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setItsSave(int itsSave) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("itsSave", itsSave);
        //editor.putInt("fingerPrint", 1);
        editor.apply();

        this.getSharedPreferences();
    }

    private int itsSave = 0;
    private int fingerPrint = 0;
    private String user;
    private String password;
    private SharedPreferences sharedPreferences;

    public SaveLoginSharedPref(Context context) {

        this.context = context;
        sharedPreferences = context.getSharedPreferences("employeeLogin", Context.MODE_PRIVATE);
        getSharedPreferences();
    }

    public int getFingerPrint() {
        return fingerPrint;
    }

    public int getItsSave() {
        this.getSharedPreferences();
        return itsSave;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }


    public void saveLogin(String name, String password) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", name);
        editor.putString("password", password);
        editor.putInt("itsSave", 1);
        //editor.putInt("fingerPrint", 1);
        editor.apply();

        this.getSharedPreferences();
    }


    public void saveFinger(int key){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("fingerPrint", key);
        this.fingerPrint = key;
        editor.apply();

        this.getSharedPreferences();
    }


    public void saveToken(String token, long id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.putLong("idUser", id);

        editor.apply();
        this.getSharedPreferences();
    }
    public void clear() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void getSharedPreferences() {

        this.itsSave = sharedPreferences.getInt("itsSave", 0);
        this.fingerPrint = sharedPreferences.getInt("fingerPrint", 0);
        this.user = sharedPreferences.getString("user", "");
        this.password = sharedPreferences.getString("password", "");

        this.token = sharedPreferences.getString("token", "");
        this.id = sharedPreferences.getLong("idUser", 0);
    }

}
