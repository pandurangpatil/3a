/*******************************************************************************
 * Copyright (c) 2014 Agnie Technologies.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Agnie Technologies - initial API and implementation
 ******************************************************************************/
package com.agnie.gwt.common.client.rpc;

import java.util.List;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class AsyncDP<T> extends AsyncDataProvider<T> {
	private Range			lastrange;
	private DataFetcher<T>	dataFetcher;

	public AsyncDP(DataFetcher<T> dataFetcher) {
		this.dataFetcher = dataFetcher;
	}

	@Override
	protected void onRangeChanged(final HasData<T> display) {
		final Range range = display.getVisibleRange();

		dataFetcher.fire(range, new ListReceiver(range, display));
	}

	public static interface DataFetcher<T> {
		void fire(Range range, Receiver<List<T>> reciever);
	}

	private class ListReceiver extends Receiver<List<T>> {

		private Range		range;

		private HasData<T>	display;

		ListReceiver(Range range, HasData<T> display) {
			this.range = range;
			this.display = display;
		}

		@Override
		public void onSuccess(List<T> response) {
			if ((lastrange == null || lastrange.equals(new Range(0, range.getLength()))) && (response == null || response.size() == 0)) {
				updateRowCount(0, true);
			} else if (response != null && response.size() > 0) {
				updateRowData(range.getStart(), response);
				lastrange = range;
				if (response.size() < range.getLength()) {
					updateRowCount(range.getStart() + response.size(), true);
				}
			} else if (display.getRowCount() > 0) {
				updateRowCount(display.getRowCount(), true);
				display.setVisibleRange(lastrange);
			} else {
				updateRowCount(0, true);
			}

		}
	}

	/**
	 * @return the dataFetcher
	 */
	public DataFetcher<T> getDataFetcher() {
		return dataFetcher;
	};

}
