package wang.raina.lolherocalendar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
/**
 * Created by wanglin on 15-7-30.
 */
public class LoadActivity extends Activity {

    public static SharedPreferences collected;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_load);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                /* Create an Intent that will start the Main WordPress Activity. */

                Intent mainIntent = new Intent(LoadActivity.this, MainActivity.class);
                LoadActivity.this.startActivity(mainIntent);
                LoadActivity.this.finish();
            }
        }, 1500); //1500 for release

    }
}