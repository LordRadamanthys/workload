package resource.estagio.workload.ui;


import android.app.Dialog;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Objects;

import resource.estagio.workload.R;
import resource.estagio.workload.infra.App;
import resource.estagio.workload.infra.ConstantApp;


public class SettingsFragment extends Fragment {
    private Switch switchFingerPrint;
    private Button buttonClearPrefSettings;
    private TextView textNameUserSettings;
    private Button buttonChosserYes;
    private Button buttonChosserNo;
    private Dialog dialog;
    private ImageView imageViewExitAdmin;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_settings, container, false);

        loadUi (view);
        haveFingerPrint ();
        initSwitch ();
        actionSwitch ();
        imageViewExitAdmin.setOnClickListener (v -> DialogApp.showExitDialog (getActivity ()));
        buttonClearPrefSettings.setOnClickListener (v -> showDialogChooser ());

        return view;
    }

    private void actionSwitch() {
        switchFingerPrint.setOnCheckedChangeListener ((buttonView, isChecked) -> {
            setActionSwitch (isChecked);
        });
    }

    private void loadUi(View view) {
        switchFingerPrint = view.findViewById (R.id.switchFingerPrint);
        buttonClearPrefSettings = view.findViewById (R.id.buttonClearPrefSettings);
        textNameUserSettings = view.findViewById (R.id.textView_name_user_settings);
        textNameUserSettings.setText (ConstantApp.HELLO + App.getUser ().getName ());
        imageViewExitAdmin = view.findViewById (R.id.image_view_exit_admin);
        imageViewExitAdmin.setVisibility (App.getUser ().isAdmin () ? View.VISIBLE : View.GONE);
    }

    private void showDialogChooser() {
        dialog = new Dialog (getContext (), R.style.CustomAlertDialog);
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.setContentView (R.layout.activity_dialog_chooser);
        dialog.setCancelable (false);
        TextView text = dialog.findViewById (R.id.text_view_subtitle_project);
        text.setText (ConstantApp.DIALOG_MESSAGE_CLEAN_SETTINGS);
        dialog.getWindow ().setSoftInputMode (WindowManager.LayoutParams.
                SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show ();

        buttonChosserYes = dialog.findViewById (R.id.button_dialog_chooser_yes);
        buttonChosserNo = dialog.findViewById (R.id.buttton_dialog_chooser_no);

        buttonChosserYes.setOnClickListener (v -> {
            App.getPref ().clear ();
            initSwitch ();
            switchFingerPrint.setChecked (false);
            dialog.dismiss ();

            Toast.makeText (getActivity (), getString (R.string.clear_preferences), Toast.LENGTH_SHORT).show ();
        });
        buttonChosserNo.setOnClickListener (v -> dialog.dismiss ());
    }

    private void setActionSwitch(boolean isChecked) {
        if (isChecked) {
            App.getPref ().saveFinger (1);
        } else {
            App.getPref ().saveFinger (0);
        }
    }

    private void initSwitch() {
        if (App.getPref ().getFingerPrint () == 1) {
            switchFingerPrint.setChecked (true);
        } else {
            switchFingerPrint.setChecked (false);
        }
    }

    private void haveFingerPrint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager)
                    Objects.requireNonNull (getActivity ()).getSystemService (Context.FINGERPRINT_SERVICE);

            if (!fingerprintManager.isHardwareDetected ()) {
                // Device doesn't support fingerprint authentication
                switchFingerPrint.setVisibility (View.GONE);
            } else if (!fingerprintManager.hasEnrolledFingerprints ()) {
                // User hasn't enrolled any fingerprints to authenticate with
                switchFingerPrint.setVisibility (View.GONE);
            }
            if (App.getPref ().getItsSave () == 0) {
                switchFingerPrint.setVisibility (View.GONE);
            } else {
                switchFingerPrint.setVisibility (View.VISIBLE);
            }
        } else {
            switchFingerPrint.setVisibility (View.GONE);
        }
    }

}