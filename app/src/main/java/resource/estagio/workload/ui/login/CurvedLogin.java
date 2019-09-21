package resource.estagio.workload.ui.login;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CurvedLogin extends View {

    private Path path;
    private Paint paint;
    private Point curveStartPoint = new Point();
    private Point curveCenterPoint = new Point();
    private Point curveEndPoint = new Point();
    private Point curveEndCenterPoint = new Point();
    private Point curveStartCenterPoint = new Point();
    public int viewWidth;
    public int viewHeight;
    public CurvedLogin(Context context) {
        super(context);
        init();
    }

    public CurvedLogin(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurvedLogin(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.parseColor("#4050D5"));
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = getWidth();
        viewHeight = getHeight();
        curveStartPoint.set(0 ,0);
        curveCenterPoint.set(viewWidth/2 - (int)(viewWidth*0.15), viewHeight + (int)(viewHeight*0.1));
        curveEndPoint.set(viewWidth , 0);
        curveStartCenterPoint.set(0, viewHeight - (int)(viewHeight*0.25));
        curveEndCenterPoint.set(viewWidth, viewHeight - (int)(viewHeight*0.05));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.moveTo(curveStartPoint.x, curveStartPoint.y);
        path.lineTo(curveStartPoint.x, curveStartPoint.y);
        path.lineTo(curveStartCenterPoint.x, curveStartCenterPoint.y);
        path.cubicTo(curveStartCenterPoint.x, curveStartCenterPoint.y,
                curveCenterPoint.x, curveCenterPoint.y,
                curveEndCenterPoint.x, curveEndCenterPoint.y);
        path.lineTo(curveEndCenterPoint.x, curveEndCenterPoint.y);
        path.lineTo(curveEndPoint.x, curveEndPoint.y);
        canvas.drawPath(path, paint);
    }
}
