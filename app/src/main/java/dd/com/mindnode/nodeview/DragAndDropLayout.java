//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dd.com.mindnode.nodeview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.hyean.dd.IDroppable.CaptureListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import java.util.ArrayList;
import java.util.List;

public class DragAndDropLayout extends RelativeLayout {
    private static final int STATUS_IDLE = 1;
    private static final int STATUS_DRAG = 2;
    private static final int STATUS_FLYING = 3;
    private static int mMinDistance;
    private float mLastX;
    private float mLastY;
    private int mStatus = 1;
    private List<View> mDroppableViews = new ArrayList();
    private IDraggable mDraggable;
    private IDraggableGhost mGhostDraggable;
    private IDroppable mDroppable;

    public DragAndDropLayout(Context context) {
        super(context);
        this.init();
    }

    public DragAndDropLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public DragAndDropLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        ViewConfiguration configuration = ViewConfiguration.get(this.getContext());
        mMinDistance = configuration.getScaledTouchSlop();
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch(ev.getAction()) {
        case 0:
            this.mLastX = ev.getX(0);
            this.mLastY = ev.getY(0);
            this.mDraggable = this.findTopChildUnder(this, ev.getX(0), ev.getY(0));
        case 1:
        case 3:
        default:
            break;
        case 2:
            float xDis = ev.getX(0) - this.mLastX;
            float yDis = ev.getY(0) - this.mLastY;
            if (this.mDraggable != null && this.mStatus == 1 && xDis * xDis + yDis * yDis > (float)(mMinDistance * mMinDistance)) {
                this.mGhostDraggable = this.mDraggable.toGhost();
                this.makeGhostPosition(this.mGhostDraggable);
                this.reCollectDroppables();
                this.mStatus = 2;
            }
        }

        return this.mStatus == 2;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
        case 0:
        default:
            break;
        case 1:
        case 3:
            this.onTouchUp();
            break;
        case 2:
            float x = event.getX(0);
            float y = event.getY(0);
            this.onTouchMove(x, y);
        }

        return this.mStatus == 2;
    }

    private void onTouchMove(float x, float y) {
        float disX = x - this.mLastX;
        float disY = y - this.mLastY;
        if (this.mGhostDraggable != null && this.mGhostDraggable instanceof View) {
            View view = (View)this.mGhostDraggable;
            LayoutParams params = (LayoutParams)view.getLayoutParams();
            this.setGhostPosition((float)params.leftMargin + disX, (float)params.topMargin + disY);
            IDroppable droppable = this.checkHitDroppable();
            if (droppable != this.mDroppable) {
                this.onCaptureDroppable(droppable);
                this.mDroppable = droppable;
            }
        }

        this.mLastX = x;
        this.mLastY = y;
    }

    private void onTouchUp() {
        if (this.mGhostDraggable != null) {
            if (this.mDroppable != null) {
                IDraggable captured = this.mDroppable.getCapturedDraggable();
                if (captured != null) {
                    captured.reset();
                }

                this.mDroppable.captureDraggable(this.mGhostDraggable.getNatureDraggable(), new CaptureListener() {
                    public void onMarkCaptured() {
                        DragAndDropLayout.this.removeGhostView();
                        DragAndDropLayout.this.mStatus = 1;
                    }
                });
            } else {
                this.ghostFlyBack();
            }

        }
    }

    private void ghostFlyBack() {
        this.mStatus = 3;
        final int startX = ((View)this.mGhostDraggable).getLeft();
        final int startY = ((View)this.mGhostDraggable).getTop();
        int[] xy = this.getLocationInThis((View)this.mGhostDraggable.getNatureDraggable());
        final int endX = xy[0];
        final int endY = xy[1];
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
        animator.setDuration(300L);
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float)valueAnimator.getAnimatedValue();
                DragAndDropLayout.this.setGhostPosition((float)(endX - startX) * value + (float)startX, (float)(endY - startY) * value + (float)startY);
            }
        });
        animator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                DragAndDropLayout.this.removeGhostView();
                if (DragAndDropLayout.this.mGhostDraggable != null) {
                    DragAndDropLayout.this.mGhostDraggable.getNatureDraggable().reset();
                }

                DragAndDropLayout.this.mStatus = 1;
            }

            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();
    }

    private void setGhostPosition(float x, float y) {
        if (this.mGhostDraggable != null && this.mGhostDraggable instanceof View) {
            View view = (View)this.mGhostDraggable;
            LayoutParams params = (LayoutParams)view.getLayoutParams();
            params.leftMargin = (int)Math.min(Math.max(0.0F, x), (float)(this.getWidth() - view.getWidth()));
            params.topMargin = (int)Math.min(Math.max(0.0F, y), (float)(this.getHeight() - view.getHeight()));
            this.requestLayout();
        }

    }

    private void onCaptureDroppable(IDroppable droppable) {
        if (this.mDroppable != null) {
            this.mDroppable.setCapturing(false);
        }

        this.mDroppable = droppable;
        if (this.mDroppable != null) {
            this.mDroppable.setCapturing(true);
        }

    }

    private IDroppable checkHitDroppable() {
        if (this.mGhostDraggable != null && this.mDroppableViews != null && !this.mDroppableViews.isEmpty()) {
            View ghostView = (View)this.mGhostDraggable;
            Rect ghostRect = new Rect(ghostView.getLeft(), ghostView.getTop(), ghostView.getLeft() + ghostView.getWidth(), ghostView.getTop() + ghostView.getHeight());

            for(int i = 0; i < this.mDroppableViews.size(); ++i) {
                View droppable = (View)this.mDroppableViews.get(i);
                int[] xy = this.getLocationInThis(droppable);
                Rect rect = new Rect(xy[0], xy[1], xy[0] + droppable.getWidth(), xy[1] + droppable.getHeight());
                if (rect.intersect(ghostRect)) {
                    return (IDroppable)droppable;
                }
            }
        }

        return null;
    }

    private void reCollectDroppables() {
        this.mDroppableViews.clear();
        this.collectDroppables(this.mDroppableViews, this);
    }

    private void collectDroppables(List<View> droppableViews, View view) {
        if (view instanceof IDroppable) {
            droppableViews.add(view);
        }

        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup)view).getChildCount();

            for(int i = childCount - 1; i >= 0; --i) {
                View child = ((ViewGroup)view).getChildAt(i);
                this.collectDroppables(droppableViews, child);
            }
        }

    }

    protected void makeGhostPosition(IDraggableGhost ghostView) {
        if (ghostView instanceof View) {
            if (ghostView.getNatureDraggable() != null && ghostView.getNatureDraggable() instanceof View) {
                View natureView = (View)ghostView.getNatureDraggable();
                int[] xy = this.getLocationInThis(natureView);
                this.addGhostView((View)ghostView, xy, natureView.getWidth(), natureView.getHeight());
            }

        }
    }

    private void addGhostView(View view, int[] xy, int width, int height) {
        LayoutParams params = new LayoutParams(width, height);
        params.leftMargin = xy[0];
        params.topMargin = xy[1];
        this.removeGhostView();
        this.addView(view, params);
    }

    public void flyBack(final IDroppable droppable) {
        if (droppable != null && droppable.getCapturedDraggable() != null) {
            droppable.captureDraggable((IDraggable)null, new CaptureListener() {
                public void onMarkCaptured() {
                    int[] xy = DragAndDropLayout.this.getLocationInThis((View)droppable);
                    View ghostView = (View)droppable.getCapturedDraggable().toGhost();
                    View draggableView = (View)droppable.getCapturedDraggable();
                    DragAndDropLayout.this.addGhostView(ghostView, xy, draggableView.getWidth(), draggableView.getHeight());
                    DragAndDropLayout.this.ghostFlyBack();
                }
            });
        }

    }

    private void removeGhostView() {
        for(int i = 0; i < this.getChildCount(); ++i) {
            View child = this.getChildAt(i);
            if (child instanceof IDraggableGhost) {
                this.removeView(child);
                break;
            }
        }

    }

    public IDraggable findTopChildUnder(View view, float x, float y) {
        if (view instanceof IDraggable) {
            int[] xy = this.getLocationInThis(view);
            if (x >= (float)xy[0] && x < (float)(xy[0] + view.getWidth()) && y >= (float)xy[1] && y < (float)(xy[1] + view.getHeight())) {
                return (IDraggable)view;
            }
        }

        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup)view).getChildCount();

            for(int i = childCount - 1; i >= 0; --i) {
                View child = ((ViewGroup)view).getChildAt(i);
                IDraggable result = this.findTopChildUnder(child, x, y);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    private int[] getLocationInThis(View view) {
        int[] location = new int[2];
        View v = view;

        do {
            location[0] += v.getLeft();
            location[1] += v.getTop();
            v = (View)v.getParent();
        } while(v != this);

        return location;
    }
}
