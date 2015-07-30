package wang.raina.lolherocalendar;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import android.graphics.Outline;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;



public class MainActivity extends ActionBarActivity {

    TextView date;
    TextView date_week;
    TextView date_day;
    DataUtil dataUtil;

    ListView goodThing;
    ListView badThing;//宜+不宜

    private RandomUtil randomUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//广告开始
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
//广告结束
    //列表开始
        date = (TextView) findViewById(R.id.calendar_day);

//        date_day = (TextView) findViewById(R.id.calendar_day);
        goodThing = (ListView) findViewById(R.id.calendar_good_content);
        badThing = (ListView) findViewById(R.id.calendar_bad_content);

        ListView list = (ListView) findViewById(R.id.calendar_good_content);
        //生成动态数组，并且转载数据
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for(int i=0;i<68;i++)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "This is Title.....");
            map.put("ItemText", "This is text.....");
            mylist.add(map);
        }
        //生成适配器，数组===》ListItem
        SimpleAdapter mSchedule = new SimpleAdapter(this, //没什么解释
                mylist,//数据来源
                R.layout.my_listitem,//ListItem的XML实现

                //动态数组与ListItem对应的子项
                new String[] {"ItemTitle", "ItemText"},

                //ListItem的XML文件里面的两个TextView ID
                new int[] {R.id.item_title,R.id.item_text});
        //添加并且显示
        list.setAdapter(mSchedule);
    //列表结束

//start-悬浮的fab 点击事件+调用分享
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        // Your FAB click action here...
//                Toast.makeText(getBaseContext(), "FAB Clicked", Toast.LENGTH_SHORT).show();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "队友送超鬼？匹配小学生？排位跪一天？到处挂机狗？明明可以躺赢抱大腿，却偏偏妄想带着猪队友 Carry；不是技术不行，只怪队友太渣，快用超神老黄历，看看你的超神五杀 MVP 在哪天？快来下载：https://gdgdocs.org/r/hFwuFU");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
//end-悬浮的fab 点击事件+调用分享
    }


//创建日期start
    @Override
    public void onResume() {
        super.onResume();
        createData();

    }
    private void createData() {
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH)+1;//0-11（1月 to 12月）
        int d = cal.get(Calendar.DATE);


        date.setText(y + " 年 "+ m + "  月 "+d + " 日");

        dataUtil = new DataUtil(MainActivity.this);
        randomUtil = new RandomUtil();
//创建日期over
//创建list开始

        List<HashMap<String, String>> datagood = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 3; i++) {
            HashMap<String, String> tmp = new HashMap<String, String>();
            int index = randomUtil.randomInt(dataUtil.activities.size());
            String title = dataUtil.activities.get(index).get("name");
            tmp.put("name", parseTitle(title));
            tmp.put("content", dataUtil.activities.get(index).get("good"));
            dataUtil.activities.remove(index);
            datagood.add(tmp);
        }
        List<HashMap<String, String>> databad = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 3; i++) {
            HashMap<String, String> tmp = new HashMap<String, String>();
            int index = randomUtil.randomInt(dataUtil.activities.size());
            String title = dataUtil.activities.get(index).get("name");
            tmp.put("name", parseTitle(title));
            tmp.put("content", dataUtil.activities.get(index).get("bad"));
            dataUtil.activities.remove(index);
            databad.add(tmp);

        }
        MyListAdapter goodList = new MyListAdapter(MainActivity.this, datagood);
        goodThing.setAdapter(goodList);
        MyListAdapter badList = new MyListAdapter(MainActivity.this, databad);
        badThing.setAdapter(badList);

    }
//辅助list的函数
    private class MyListAdapter extends BaseAdapter {
        Context context;
        List<HashMap<String, String>> data;

        public MyListAdapter(Context context,
                             List<HashMap<String, String>> result) {
            this.data = result;
            this.context = context;
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return data.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            final HolderView holder;
            if (convertView == null) {
                convertView = View
                        .inflate(context, R.layout.my_listitem, null);
                holder = new HolderView();
                holder.title = (TextView) convertView.findViewById(R.id.item_title);
                holder.content = (TextView) convertView
                        .findViewById(R.id.item_text);

                convertView.setTag(holder);
            } else {
                holder = (HolderView) convertView.getTag();
            }

            if (null != data.get(position).get("name")) {
                holder.title.setText(data.get(position).get("name"));
            }

            if (null != data.get(position).get("content")) {
                holder.content.setText(data.get(position).get("content"));
            }

            return convertView;
        }

        private class HolderView {
            TextView title;
            TextView content;
        }
    }
//parseTitle方法，辅助list
    private String parseTitle(String title) {
        if (title.contains("%a")) {
            String tool = dataUtil.tools[randomUtil
                    .randomInt(dataUtil.tools.length)];
            title = title.replace("%a", tool);
        } else if (title.contains("%b")) {
            String var = dataUtil.varNames[randomUtil
                    .randomInt(dataUtil.varNames.length)];
            title = title.replace("%b", var);
        } else if (title.contains("%c")) {
            int number = randomUtil.randomInt(10000);
            title = title.replace("%b", number + "");
        }
        return title;
    }

    private android.support.v7.widget.ShareActionProvider mShareActionProvider;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//之前开始代码
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Set up ShareActionProvider's default share intent
//        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
//        mShareActionProvider = (android.support.v7.widget.ShareActionProvider)
//                MenuItemCompat.getActionProvider(shareItem);
//        mShareActionProvider.setShareIntent(getDefaultIntent());

        return super.onCreateOptionsMenu(menu);
    }
    /** Defines a default (dummy) share intent to initialize the action provider.
     * However, as soon as the actual content to be used in the intent
     * is known or changes, you must update the share intent by again calling
     * mShareActionProvider.setShareIntent()
     */
//    private Intent getDefaultIntent() {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_TEXT, "每个人生命中都至少应该有一条犬，我觉得不然这个人的生命可以说是不完整的。" +
//                "而当我们选择了某一条犬之后，我们就应该尽全力去照顾它。哪种狗狗适合你呢？下载热门狗狗大全：https://gdgdocs.org/r/hFwuFU");
//        intent.setType("text/plain");
//        Intent.createChooser(intent, getResources().getText(R.string.send_to));
//        return intent;
//    }

//之前分享结束



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }








}


