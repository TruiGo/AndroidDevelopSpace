package com.example.android.basicmediadecoder;

import android.support.v7.media.MediaRouteProvider;
import android.support.v7.media.MediaRouteProviderService;


/**
 * Demonstrates how to register a custom media route provider service using the support library.
 * 
 * @see com.example.android.mediarouter.provider.SampleMediaRouteProvider
 */
public class SampleMediaRouteProviderService extends MediaRouteProviderService {
	@Override
	public MediaRouteProvider onCreateMediaRouteProvider() {
		return new SampleMediaRouteProvider(this);
	}
}
