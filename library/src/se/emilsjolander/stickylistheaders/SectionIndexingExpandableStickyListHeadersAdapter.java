package se.emilsjolander.stickylistheaders;

import android.widget.SectionIndexer;

public class SectionIndexingExpandableStickyListHeadersAdapter extends ExpandableStickyListHeadersAdapter implements SectionIndexer {

	SectionIndexingExpandableStickyListHeadersAdapter(StickyListHeadersAdapter innerAdapter) {
		super(innerAdapter);
		if (!(innerAdapter instanceof SectionIndexer)) throw new IllegalArgumentException("Wrapped adapter must implement SectionIndexer");
	}

	@Override
	public Object[] getSections() {
		return ((SectionIndexer) mInnerAdapter).getSections();
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		return ((SectionIndexer) mInnerAdapter).getPositionForSection(sectionIndex);
	}

	@Override
	public int getSectionForPosition(int position) {
		return ((SectionIndexer) mInnerAdapter).getSectionForPosition(position);
	}
}
