package se.emilsjolander.stickylistheaders;

import android.widget.SectionIndexer;

public class SectionIndexingExpandableStickyListHeadersAdapter extends ExpandableStickyListHeadersAdapter implements SectionIndexer {

	SectionIndexingExpandableStickyListHeadersAdapter(SectionIndexingStickyListHeadersAdapter innerAdapter) {
		super(innerAdapter);
	}

	@Override
	public Object[] getSections() {
		return getAdapter().getSections();
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		return getAdapter().getPositionForSection(sectionIndex);
	}

	@Override
	public int getSectionForPosition(int position) {
		return getAdapter().getSectionForPosition(position);
	}

	private SectionIndexingStickyListHeadersAdapter getAdapter() {
		return ((SectionIndexingStickyListHeadersAdapter) mInnerAdapter);
	}
}
