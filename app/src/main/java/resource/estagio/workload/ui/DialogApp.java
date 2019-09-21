package resource.estagio.workload.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

import resource.estagio.workload.R;
import resource.estagio.workload.infra.App;
import resource.estagio.workload.ui.home.timeline.adapterTimeLine.AdapterTimeline;
import resource.estagio.workload.ui.login.LoginActivity;

public class DialogApp {

    public static void showDialogConfirm(String message, boolean status, Context context) {

        Dialog dialog = createDialog (context,
                status ? R.layout.activity_check : R.layout.activity_dialog_error);

        TextView text = dialog.findViewById (status ? R.id.textDialog : R.id.text_dialog_error);
        text.setText (message);
        Button buttonConfirmCheck = dialog.findViewById (R.id.button_dialog_error);
        buttonConfirmCheck.setOnClickListener (v -> {
            dialog.dismiss ();
        });
    }



    public static Dialog createDialog(Context context, int layout) {
        Dialog dialog = new Dialog (context, R.style.CustomAlertDialog);
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.setContentView (layout);
        dialog.setCancelable (false);
        dialog.getWindow ().setSoftInputMode (WindowManager.LayoutParams.
                SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.show();
        return dialog;
    }

    public static void showDialogConnection(Context context) {
        Dialog dialog = createDialog (context, R.layout.activity_dialog_connection);
        Button buttonConnection = dialog.findViewById (R.id.button_dialog_connection);
        buttonConnection.setOnClickListener (v -> dialog.dismiss ());
    }

    public static void showExitDialog(Activity activity) {

        Dialog dialog = createDialog (activity, R.layout.activity_dialog_chooser );
        Button buttonChosserYes = dialog.findViewById(R.id.button_dialog_chooser_yes);
        Button buttonChosserNo = dialog.findViewById(R.id.buttton_dialog_chooser_no);

        buttonChosserYes.setOnClickListener(v -> {
            App.getPref().clear();
            Intent intent = new Intent(activity, LoginActivity.class);
            dialog.dismiss();
            activity.startActivity(intent);
            activity.finish();
        });
        buttonChosserNo.setOnClickListener(v -> dialog.dismiss());
    }

    public static void showRemoveDialog(Activity activity, String message, DialogListener listener) {

        Dialog dialog = createDialog (activity, R.layout.activity_dialog_chooser );
        TextView subtitle = dialog.findViewById(R.id.text_view_subtitle_project);
        subtitle.setText(message);
        Button buttonChosserYes = dialog.findViewById(R.id.button_dialog_chooser_yes);
        Button buttonChosserNo = dialog.findViewById(R.id.buttton_dialog_chooser_no);
        buttonChosserYes.setOnClickListener(v -> {
            listener.setChooser(true);
            dialog.dismiss();

        });
        buttonChosserNo.setOnClickListener(v -> {
            listener.setChooser(false);
            dialog.dismiss();
        });

    }

    public interface DialogListener {
        void setChooser(boolean chooser);
    }
}