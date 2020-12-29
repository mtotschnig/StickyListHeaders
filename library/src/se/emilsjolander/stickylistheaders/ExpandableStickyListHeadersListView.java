package se.emilsjolander.stickylistheaders;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.customview.view.AbsSavedState;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * add expand/collapse functions like ExpandableListView
 * @author lsjwzh
 */
public class ExpandableStickyListHeadersListView extends StickyListHeadersListView {
    public interface IAnimationExecutor{
        public void executeAnim(View target,int animType);
    }

    public final static int ANIMATION_COLLAPSE = 1;
    public final static int ANIMATION_EXPAND = 0;

    ExpandableStickyListHeadersAdapter mExpandableStickyListHeadersAdapter;

    IAnimationExecutor mDefaultAnimExecutor = new IAnimationExecutor() {
        @Override
        public void executeAnim(View target, int animType) {
            if(animType==ANIMATION_EXPAND){
                target.setVisibility(VISIBLE);
            }else if(animType==ANIMATION_COLLAPSE){
                target.setVisibility(GONE);
            }
        }
    };


    public ExpandableStickyListHeadersListView(Context context) {
        super(context);
    }

    public ExpandableStickyListHeadersListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableStickyListHeadersListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ExpandableStickyListHeadersAdapter getAdapter() {
        return mExpandableStickyListHeadersAdapter;
    }

    @Override
    public void setAdapter(StickyListHeadersAdapter adapter) {
        mExpandableStickyListHeadersAdapter = (adapter instanceof SectionIndexingStickyListHeadersAdapter) ?
            new SectionIndexingExpandableStickyListHeadersAdapter(((SectionIndexingStickyListHeadersAdapter) adapter)) :
            new ExpandableStickyListHeadersAdapter(adapter);
        super.setAdapter(mExpandableStickyListHeadersAdapter);
    }

    public View findViewByItemId(long itemId){
        return mExpandableStickyListHeadersAdapter.findViewByItemId(itemId);
    }

    public long findItemIdByView(View view){
        return mExpandableStickyListHeadersAdapter.findItemIdByView(view);
    }

    public void expand(long headerId) {
        if(!mExpandableStickyListHeadersAdapter.isHeaderCollapsed(headerId)){
            return;
        }
        mExpandableStickyListHeadersAdapter.expand(headerId);
        //find and expand views in group
        List<View> itemViews = mExpandableStickyListHeadersAdapter.getItemViewsByHeaderId(headerId);
        if(itemViews==null){
            return;
        }
        for (View view : itemViews) {
            animateView(view, ANIMATION_EXPAND);
        }
    }

    public void collapse(long headerId) {
        if(mExpandableStickyListHeadersAdapter.isHeaderCollapsed(headerId)){
            return;
        }
        mExpandableStickyListHeadersAdapter.collapse(headerId);
        //find and hide views with the same header
        List<View> itemViews = mExpandableStickyListHeadersAdapter.getItemViewsByHeaderId(headerId);
        if(itemViews==null){
            return;
        }
        for (View view : itemViews) {
            animateView(view, ANIMATION_COLLAPSE);
        }
    }

    public boolean isHeaderCollapsed(long headerId){
        return  mExpandableStickyListHeadersAdapter.isHeaderCollapsed(headerId);
    }

    public List<Long> getCollapsedHeaderIds() {
        return mExpandableStickyListHeadersAdapter.getCollapseHeaderIds();
    }

    public void setCollapsedHeaderIds(List<Long> collapsedHeaderIds ) {
        mExpandableStickyListHeadersAdapter.setCollapseHeaderIds(collapsedHeaderIds);
    }

    public void setAnimExecutor(IAnimationExecutor animExecutor) {
        this.mDefaultAnimExecutor = animExecutor;
    }

    /**
     * Performs either COLLAPSE or EXPAND animation on the target view
     *
     * @param target the view to animate
     * @param type   the animation type, either ExpandCollapseAnimation.COLLAPSE
     *               or ExpandCollapseAnimation.EXPAND
     */
    private void animateView(final View target, final int type) {
        if(ANIMATION_EXPAND==type&&target.getVisibility()==VISIBLE){
            return;
        }
        if(ANIMATION_COLLAPSE==type&&target.getVisibility()!=VISIBLE){
            return;
        }
        if(mDefaultAnimExecutor !=null){
            mDefaultAnimExecutor.executeAnim(target,type);
        }

    }

    @Override
    public Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), mExpandableStickyListHeadersAdapter.getCollapseHeaderIds());
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (mExpandableStickyListHeadersAdapter != null) {
            mExpandableStickyListHeadersAdapter.setCollapseHeaderIds(ss.collapsedHeaderIds);
        }
    }

    static class SavedState extends AbsSavedState {
        private List<Long> collapsedHeaderIds;

        /**
         * Constructor called from {@link StickyListHeadersListView#onSaveInstanceState()}
         */
        SavedState(Parcelable superState, List<Long> collapsedHeaderIds) {
            super(superState);
            this.collapsedHeaderIds = collapsedHeaderIds;
        }

        private void readValues(Parcel in, ClassLoader loader) {
            collapsedHeaderIds = new ArrayList<>();
            in.readList(collapsedHeaderIds, loader);
        }

        SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            readValues(in, loader);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeList(collapsedHeaderIds);
        }

        public static final Creator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                return new SavedState(source, loader);
            }

            @Override
            public SavedState createFromParcel(Parcel source) {
                return createFromParcel(source, null);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
