package com.freezeapp;

import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.freezeapp.adapter.AppAdapter;
import com.freezeapp.model.AppModel;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Main Activity Class
 *
 * @author Rakesh
 * @since 10/11/2017.
 */
public class MainActivity extends AppCompatActivity {

    ListView list;
    SwipeRefreshLayout refreshLayout;
    AppAdapter adapter;
    List<AppModel> itemlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            Process root = Runtime.getRuntime().exec("su");
        } catch (Exception e) {
            e.printStackTrace();
        }

        list = (ListView) findViewById(R.id.list);
        adapter = new AppAdapter(this, itemlist);
        list.setAdapter(adapter);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        fetchInfo();
                    }
                }
        );

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                showDialog(itemlist.get(i));
            }
        });

        fetchInfo();

    }


    /**
     * Method to get list of applications
     */
    private void fetchInfo() {

        refreshLayout.setRefreshing(true);

        itemlist.clear();

        List<AppModel> itemlistE = new ArrayList<>();
        List<AppModel> itemlistD = new ArrayList<>();

        List<ApplicationInfo> appinfo = getPackageManager().getInstalledApplications(0);

        for (ApplicationInfo info : appinfo) {
            ApplicationInfo ai = info;
            try {
                AppModel model = new AppModel();
                model.setAppname(ai.loadLabel(getPackageManager()).toString());
                model.setPkg(ai.packageName);
                model.setIcon(ai.loadIcon(getPackageManager()));


                if (getPackageManager().getApplicationEnabledSetting(ai.packageName) ==
                        getPackageManager().COMPONENT_ENABLED_STATE_DISABLED) {
                    model.setDisabled(true);
                    itemlistD.add(model);
                } else {
                    model.setDisabled(false);
                    itemlistE.add(model);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        Collections.sort(itemlistD, new Comparator<AppModel>() {
            public int compare(AppModel obj1, AppModel obj2) {
                return obj1.getAppname().compareToIgnoreCase(obj2.getAppname()); // To compare string values
            }
        });


        Collections.sort(itemlistE, new Comparator<AppModel>() {
            public int compare(AppModel obj1, AppModel obj2) {
                return obj1.getAppname().compareToIgnoreCase(obj2.getAppname()); // To compare string values
            }
        });


        itemlist.addAll(itemlistD);
        itemlist.addAll(itemlistE);

        adapter.notifyDataSetChanged();

        refreshLayout.setRefreshing(false);
    }


    /**
     * Method to show Freeze/De-freeze dialog
     *
     * @param model
     */
    private void showDialog(final AppModel model) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setIcon(model.getIcon());
        builderSingle.setTitle(model.getAppname());

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
        if (model.isDisabled())
            arrayAdapter.add(getString(R.string.lbl_de_freez));
        else
            arrayAdapter.add(getString(R.string.lbl_freez));


        builderSingle.setNegativeButton(R.string.lbl_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (model.isDisabled())
                            KillApp(false, model.getPkg());
                        else
                            KillApp(true, model.getPkg());
                        break;
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fetchInfo();
                    }
                }, 1000);
            }
        });
        builderSingle.show();
    }


    /**
     * Method to disable/enable app
     *
     * @param flag true to disable
     * @param pkg  package name of the application
     */
    private void KillApp(boolean flag, String pkg) {
        Process suProcess = null;
        try {
            suProcess = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            os.writeBytes("adb shell" + "\n");
            os.flush();
            if (flag)
                os.writeBytes("pm disable " + pkg + "\n");
            else
                os.writeBytes("pm enable " + pkg + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getResources().getString(R.string.msg_no_access), Toast.LENGTH_LONG).show();
        }
    }


}