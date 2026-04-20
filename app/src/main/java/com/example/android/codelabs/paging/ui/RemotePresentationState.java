package com.example.android.codelabs.paging.ui;

import androidx.paging.CombinedLoadStates;
import androidx.paging.LoadState;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowKt;

public enum RemotePresentationState {
    INITIAL, REMOTE_LOADING, SOURCE_LOADING, PRESENTED;

    public static Flow<RemotePresentationState> asRemotePresentationState(Flow<CombinedLoadStates> flow) {
        return FlowKt.distinctUntilChanged(
            FlowKt.scan(flow, RemotePresentationState.INITIAL, (state, loadState, continuation) -> {
                switch (state) {
                    case PRESENTED:
                        if (loadState.getMediator() != null && loadState.getMediator().getRefresh() instanceof LoadState.Loading) {
                            return RemotePresentationState.REMOTE_LOADING;
                        }
                        return state;
                    case INITIAL:
                        if (loadState.getMediator() != null && loadState.getMediator().getRefresh() instanceof LoadState.Loading) {
                            return RemotePresentationState.REMOTE_LOADING;
                        }
                        return state;
                    case REMOTE_LOADING:
                        if (loadState.getSource().getRefresh() instanceof LoadState.Loading) {
                            return RemotePresentationState.SOURCE_LOADING;
                        }
                        return state;
                    case SOURCE_LOADING:
                        if (loadState.getSource().getRefresh() instanceof LoadState.NotLoading) {
                            return RemotePresentationState.PRESENTED;
                        }
                        return state;
                    default:
                        return state;
                }
            })
        );
    }
}
